package com.joolun.web.netty;

import com.alibaba.fastjson.JSONObject;
import com.joolun.common.constant.NettyConstants;
import com.joolun.mall.entity.TCPBaseEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MyDecoder extends ByteToMessageDecoder {
    // 包头长度位置
    private static final int HEADER_LENGTH = 1;
    private static final String HEADER_PACKAGE = "E5";
    private static final String DOWNLOAD_HEADER_PACKAGE = "E7";
    private ByteBuf byteBuf;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //可读取字节数
        int readableBytes = byteBuf.readableBytes();
        if (readableBytes<=HEADER_LENGTH){
            //对读指针进行标记
//            byteBuf.markReaderIndex();
            return;
        }
        byte b1 = byteBuf.readByte();
        String header = Integer.toHexString(0xFF & b1).toUpperCase();
        log.info("转换后的{}",header);

        if (!header.equals(HEADER_PACKAGE) && !header.equals(DOWNLOAD_HEADER_PACKAGE)){
            return;
        }

        byte lengthByte = byteBuf.readByte();
        String length = String.valueOf(lengthByte);
        log.debug("length: {} ,readableBytes: {} ",length,readableBytes);
        //        byteBuf.resetReaderIndex();
        if (Integer.parseInt(length)>readableBytes-2) {
            log.warn("数据不完整，等待下次读取! ");
            return;
        }
        //创建字节数组,buffer.readableBytes可读字节长度
        byte[] b = new byte[Integer.parseInt(length)];
        //复制内容到字节数组b
        ByteBuf newByteBuf=Unpooled.buffer(b.length);
        byteBuf.readBytes(newByteBuf);

        newByteBuf.readBytes(b);
        newByteBuf.release();

        TCPBaseEntity tcpBaseEntity=new TCPBaseEntity();
        tcpBaseEntity.setPackageHead(header);
        tcpBaseEntity.setPackageLength(Integer.parseInt(length));
        int type = Integer.parseInt(String.valueOf(b[0]));
        tcpBaseEntity.setPackageType(type);
        tcpBaseEntity.setImei(charChange(bytesToHexString(Arrays.copyOfRange(b, 1, 16))));
        log.info("接收到的消息:{}",Arrays.toString(b));
        switch (type){
            case NettyConstants.FirstLinkPack:
                tcpBaseEntity.setIccId(charChange(bytesToHexString(Arrays.copyOfRange(b, 16, 36))));
                tcpBaseEntity.setCsq(Integer.parseInt(String.valueOf(b[36])));
                tcpBaseEntity.setPersist(bytesToHexString(Arrays.copyOfRange(b, 37, 45)));
                tcpBaseEntity.setVerification(Integer.toHexString(0xFF & b[45]).toUpperCase());
                break;
            case NettyConstants.HeartBeat:
            case NettyConstants.ChargeTime:
                tcpBaseEntity.setCsq(Integer.parseInt(String.valueOf(b[16])));
                tcpBaseEntity.setPersist(bytesToHexString(Arrays.copyOfRange(b, 17, 25)));
                tcpBaseEntity.setVerification(Integer.toHexString(0xFF & b[25]).toUpperCase());
                break;
            case NettyConstants.ScanCode:
                tcpBaseEntity.setIccId(charChange(bytesToHexString(Arrays.copyOfRange(b, 16, 36))));
                tcpBaseEntity.setPersist(bytesToHexString(Arrays.copyOfRange(b, 36, 44)));
                tcpBaseEntity.setVerification(Integer.toHexString(0xFF & b[44]).toUpperCase());
                break;
            case NettyConstants.PushRequestPackReply:
                // 升级回复信息
                tcpBaseEntity.setConfirm(Integer.parseInt(String.valueOf(b[16])));
                //包id
                tcpBaseEntity.setPackageId(bytesToHexString(Arrays.copyOfRange(b, 17, 19)));
                tcpBaseEntity.setPersist(bytesToHexString(Arrays.copyOfRange(b, 19, 27)));
                tcpBaseEntity.setVerification(Integer.toHexString(0xFF & b[27]).toUpperCase());
                break;
            case NettyConstants.FirmwarePushPackReply:
                //包id
                tcpBaseEntity.setPackageId(bytesToHexString(Arrays.copyOfRange(b, 16, 18)));
                tcpBaseEntity.setConfirm(Integer.parseInt(String.valueOf(b[18])));
                tcpBaseEntity.setVerification(Integer.toHexString(0xFF & b[19]).toUpperCase());
                break;
        }
        log.info("msg:{}", JSONObject.toJSONString(tcpBaseEntity));
        list.add(tcpBaseEntity);
    }

    private static String charChange(String hexString){
        StringBuilder result=new StringBuilder();
        if (hexString==null){
            return "";
        }
        for (int i = 0; i < hexString.length(); i += 2) {
            String hex = hexString.substring(i, i + 2);
            int hexValue = Integer.parseInt(hex, 16);
            result.append((char) hexValue);
        }
        return result.toString();
    }

    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
    }
}
