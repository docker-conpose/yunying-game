package com.joolun.web.netty;

import com.alibaba.fastjson.JSONObject;
import com.joolun.common.constant.NettyConstants;
import com.joolun.mall.entity.TCPBaseEntity;
import com.joolun.mall.util.EncryptUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;


@Component
public class ChannelInboundHandler extends SimpleChannelInboundHandler<TCPBaseEntity> {
    private static final Logger logger = LoggerFactory.getLogger(ChannelInboundHandler.class);
    private static ChannelInboundHandler handler;

    public static HashMap<String,String> imeiChannelId=new HashMap<>();
    @PostConstruct
    //因为是new出来的handler,没有托给spring容器,所以一定要先初始化,否则autowired失效
    public void init() {
        handler = this;
        logger.info("ChannelInboundHandler 加入");
    }

    //定义一个channel 组，管理所有的channel
    //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Resource
    private NettyService nettyService;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;
            switch (event.state()) {
                case READER_IDLE: //客户端在一定时间内未向服务端发送任何的数据
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE: //接收到数据，但在一定时间内未写入数据
                    eventType = "写空闲";
                    break;
                case ALL_IDLE: //未发送数据，也未写入数据
                    eventType = "读写空闲";
                    break;
            }
            logger.warn(ctx.channel().remoteAddress() + "超时事件：" + eventType);
            ctx.channel().close();
            String imei = imeiChannelId.get(ctx.channel().id().asLongText());
            handler.nettyService.equipmentOffline(imei);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TCPBaseEntity msg) throws Exception {
        Channel channel = ctx.channel();
        String packageLength = Integer.toHexString(0xFF & msg.getPackageLength()).toUpperCase();

        String packageType = String.format("%02X", msg.getPackageType() & 0xFF);
        String csq = "";
        String imei = "";
        String iccId = "";
        String packageId = "";
        String updateState = "";
        String persist = msg.getPersist()!= null ? msg.getPersist() : "";
        if (msg.getCsq() != null){
            csq = String.format("%02X", msg.getCsq() & 0xFF).toUpperCase();
        }
        if (msg.getPackageId() != null){
            packageId = msg.getPackageId();
            updateState = String.format("%02X", msg.getConfirm() & 0xFF).toUpperCase();
        }
        imei=msg.getImei() != null? EncryptUtil.getHexString(msg.getImei()):"";

        iccId=msg.getIccId() != null?EncryptUtil.getHexString(msg.getIccId()):"";

        String xor2String = EncryptUtil.getXor2String(msg.getPackageHead() + packageLength + packageType +
                imei + iccId + csq + persist + packageId + updateState).toUpperCase();
        if (xor2String.equals(msg.getVerification())){
            switch (msg.getPackageType()){
                case NettyConstants.FirstLinkPack://首次链接包
                    handler.nettyService.firstLinkDevice(msg,channel);
                    break;
                case NettyConstants.HeartBeat://心跳包
                    handler.nettyService.heartbeatDetection(msg,channel);
                    break;
                case NettyConstants.ScanCode://扫码信息获取
                    handler.nettyService.gainDrugInfo(msg,channel);
                    break;
                case NettyConstants.ChargeTime://药包时间更新
                    handler.nettyService.drugInfoUpdate(msg,channel);
                    break;
                case NettyConstants.PushRequestPackReply://推送请求回复
                    handler.nettyService.receiveUpdateRequest(msg,channel);
                    break;
                case NettyConstants.FirmwarePushPackReply://推送请求回复
                    handler.nettyService.receivePushPackReply(msg,channel);
                    break;
                default:
                    logger.warn("错误数据!");
                    break;
            }
        }else {
            logger.info("校验失败，数据: {}",JSONObject.toJSONString(msg));
        }
    }

    /**
     * 表示channel 处于不活动状态
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
