package com.joolun.web.netty;

import com.joolun.common.constant.NettyConstants;
import com.joolun.mall.entity.TCPBaseEntity;
import com.joolun.mall.util.EncryptUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import static com.joolun.web.netty.MyDecoder.intToBytes;

@Slf4j
public class MyEncoder  extends MessageToByteEncoder<TCPBaseEntity> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TCPBaseEntity entity, ByteBuf byteBuf) throws Exception {
        String packageType = String.format("%02X", entity.getPackageType()).toUpperCase();
        int length=25;
        String csq = "";
        if (entity.getCsq() != null){
            csq = String.format("%02X", entity.getCsq() & 0xFF).toUpperCase();
            length++;
        }
        String iccId="";
        if (entity.getIccId() != null && entity.getPackageType() != 8){
            iccId = EncryptUtil.getHexString(entity.getIccId());

            length=length+(iccId.length()/2);
        }
        String confirm="";
        if (entity.getConfirm() !=null){
            confirm=String.format("%02X", entity.getConfirm() & 0xFF).toUpperCase();
            length++;
        }
        String packageId = "";
        String packageTotal = "";
        if (entity.getPackageType() == NettyConstants.FirmwarePushPack){
            length = 21;
            packageId = entity.getPackageId();
            packageTotal =   String.format("%04X", entity.getPackageTotal()).toUpperCase();
            log.info("总包长度:{}",packageTotal);
            length = length + (entity.getFileRegion().length() / 2);
        }
        String imei = EncryptUtil.getHexString(entity.getImei());
        String packageLength = String.format("%04X", length).toUpperCase();
        String sendMessage = "";
        if (entity.getPackageType() == NettyConstants.FirmwarePushPack){
            sendMessage = entity.getPackageHead() + packageLength + packageType + imei
                    + packageId + packageTotal + entity.getFileRegion();
        }else {
            sendMessage = entity.getPackageHead() + packageLength + packageType + imei +
                    iccId + confirm + csq + entity.getPersist();
        }
        String verification = EncryptUtil.getXor2String(sendMessage);

        //将16进制字符串转为数组
        byteBuf.writeBytes(hexString2Bytes(sendMessage + verification));
        log.info("发送数据：{}",hexString2Bytes(sendMessage + verification));
    }
    /**
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     * @param src 16进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String src) {

        int l = src.length() / 2;

        byte[] ret = new byte[l];

        for (int i = 0; i < l; i++) {

            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();

        }

        return ret;

    }
}