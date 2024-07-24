package com.joolun.web.netty;

import com.alibaba.fastjson.JSONObject;
import com.joolun.common.constant.NettyConstants;
import com.joolun.common.core.redis.RedisCache;
import com.joolun.framework.web.domain.server.Sys;
import com.joolun.mall.entity.DrugInfo;
import com.joolun.mall.entity.EquipmentInfo;
import com.joolun.mall.entity.TCPBaseEntity;
import com.joolun.mall.service.impl.DrugInfoServiceImpl;
import com.joolun.mall.service.impl.EquipmentInfoServiceImpl;
import com.joolun.mall.util.RedisKeys;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.joolun.web.netty.MyDecoder.bytesToHexString;
import static com.joolun.web.netty.MyDecoder.intToBytes;

@Component
@Slf4j
public class NettyService {
    @Autowired
    private  RedisCache redisCache;
    @Autowired
    private EquipmentInfoServiceImpl equipmentInfoService;
    @Autowired
    private DrugInfoServiceImpl drugInfoService;
    private static final String MEDICINAL_NUMBER="medicinal_number";
    private static final String TIME_REMAINING="time_remaining";
    private static final String PACKAGE_HEAD="E6";

    private static HashMap<String,Channel> channelMap=new HashMap<>();

    private static final int CHUNK_SIZE = 1024; // 1KB


    /**
     * 首次链接包
     * @param msg
     * @param channel
     */
    public void firstLinkDevice(TCPBaseEntity msg, Channel channel){
        String iccId = msg.getIccId();
        //设备编码
        String sendImei = msg.getImei();
        EquipmentInfo equipmentInfo=new EquipmentInfo();
        equipmentInfo.setEquipmentNumber(sendImei);
        equipmentInfo = equipmentInfoService.selectEquipmentInfoByEquipmentNumber(equipmentInfo);
        if (equipmentInfo != null){
            equipmentInfo.setEquipmentStatus(1L);
            equipmentInfoService.updateEquipmentInfo(equipmentInfo);
        } else {
            equipmentInfo=new EquipmentInfo();
            equipmentInfo.setEquipmentNumber(sendImei);
            equipmentInfo.setIccid(iccId);
            equipmentInfo.setClassifyId("1701168500961996802");
            equipmentInfo.setHardwareVersion(1L);
            equipmentInfo.setSoftwareVersion(1L);
            equipmentInfo.setEquipmentStatus(1L);
            equipmentInfo.setWarehouseStatus(0L);
            equipmentInfo.setDeleted(0L);
            equipmentInfoService.insertEquipmentInfo(equipmentInfo);
        }

        ChannelInboundHandler.imeiChannelId.put(channel.id().asLongText(),sendImei);
        //返回首次链接包
        msg.setPackageType(NettyConstants.FirstLinkPackReply);
        log.info("初始链接获取的key："+RedisKeys.getDrugInfoKey(sendImei));
        Object drugInfoTime = redisCache.getCacheMapValue(RedisKeys.getDrugInfoKey(sendImei), TIME_REMAINING);
        int drugTime = 0;
        if (drugInfoTime!=null){
            drugTime = Integer.parseInt(drugInfoTime.toString());
        }
        if (drugInfoTime!=null && drugTime <= 0){
            drugTime = 0;
            String drugNumber = redisCache.getCacheMapValue(RedisKeys.getDrugInfoKey(sendImei), MEDICINAL_NUMBER).toString();
            // 更新药包
            DrugInfo drugInfo = new DrugInfo();
            drugInfo.setDrugNumber(drugNumber);
            drugInfo = drugInfoService.selectDrugInfoOne(drugInfo);
            drugInfo.setTimeRemaining(0L);
            drugInfo.setDrugStatus(2);
            drugInfoService.updateDrugInfo(drugInfo);
            redisCache.deleteObject(RedisKeys.getDrugInfoKey(sendImei));
        }
        msg.setPackageHead(PACKAGE_HEAD);
        msg.setCsq(drugTime);
        channel.writeAndFlush(msg);
        channelMap.put(sendImei,channel);
    }

    /**
     * 心跳包
     * @param msg
     * @param channel
     */
    public void heartbeatDetection(TCPBaseEntity msg, Channel channel){
        String stateKey = RedisKeys.getEquipmentStateKey(msg.getImei());
        Object state = redisCache.getCacheObject(stateKey);
        if (state==null || state != msg.getCsq()){
            redisCache.setCacheObject(stateKey,msg.getCsq());
            int equipmentState;
            switch (msg.getCsq()){
                case 0:
                    equipmentState=1;
                    break;
                case 1:
                    equipmentState=3;
                    break;
                default:
                    equipmentState=msg.getCsq();
                    break;
            }
            equipmentInfoService.updateStateByEquipmentNumber(msg.getImei(),equipmentState);
        }
        msg.setPackageHead(PACKAGE_HEAD);
        msg.setPackageType(NettyConstants.HeartBeatReply);
        msg.setCsq(1);
        channel.writeAndFlush(msg);
        channelMap.put(msg.getImei(),channel);
    }

    /**
     * 药包信息,扫码时返回
     * @param msg
     * @param channel
     */
    //TODO 后期优化项
    public void gainDrugInfo(TCPBaseEntity msg, Channel channel){
        //药物剩余时间
        String imei = msg.getImei();
        String medicinalNumber = msg.getIccId();
        String drugInfoKey = RedisKeys.getDrugInfoKey(imei);
        log.info("drugInfoKey:"+drugInfoKey);
        Map<String, Object> timeRemaining = redisCache.getCacheMap(drugInfoKey);
        log.info("timeRemaining信息:"+ JSONObject.toJSONString(timeRemaining));
         if (timeRemaining == null || timeRemaining.keySet().isEmpty()){
            DrugInfo drugInfo = drugInfoService.selectDrugInfoByState(medicinalNumber);
            if (drugInfo == null || drugInfo.getDrugStatus()==1){
                log.warn("无效药包编码:{}", medicinalNumber);
                msg.setConfirm(3);
                msg.setCsq(0);
            }else {
                msg.setConfirm(1);
                //药包剩余时长
                int drugTimeRemaining = drugInfo.getTimeRemaining().intValue() / 10;
                msg.setCsq(drugTimeRemaining);

                EquipmentInfo equipmentInfo = new EquipmentInfo();
                equipmentInfo.setEquipmentNumber(msg.getImei());
                equipmentInfo = equipmentInfoService.selectEquipmentInfoByEquipmentNumber(equipmentInfo);
                drugInfo.setEquipmentId(equipmentInfo.getId());
                drugInfo.setDrugStatus(1);
                drugInfoService.updateDrugInfo(drugInfo);

                redisCache.setCacheMapValue(drugInfoKey,MEDICINAL_NUMBER,medicinalNumber);
                redisCache.setCacheMapValue(drugInfoKey,TIME_REMAINING,drugTimeRemaining);
                log.info("存入 Redis 中的数据:{},{}", medicinalNumber,drugTimeRemaining);
            }
        } else {
            // 药包状态 为在使用中
            Integer remaining = Integer.parseInt(timeRemaining.get(TIME_REMAINING).toString());
            if (remaining!=0){
                if (timeRemaining.get(TIME_REMAINING).toString().equals(medicinalNumber)){
                    msg.setConfirm(2);
                    msg.setCsq(remaining);
                    redisCache.setCacheMapValue(drugInfoKey,MEDICINAL_NUMBER,medicinalNumber);
                    redisCache.setCacheMapValue(drugInfoKey,TIME_REMAINING,remaining);
                }else {
                    DrugInfo drugInfo = drugInfoService.selectDrugInfoByState(medicinalNumber);
                    if (drugInfo == null){
                        log.warn("无效药包编码:{}", medicinalNumber);
                        msg.setConfirm(3);
                        msg.setCsq(0);
                    }else {
                        msg.setConfirm(1);
                        //药包剩余时长
                        int drugTimeRemaining = drugInfo.getTimeRemaining().intValue() / 10;
                        msg.setCsq(drugTimeRemaining);

                        EquipmentInfo equipmentInfo = new EquipmentInfo();
                        equipmentInfo.setEquipmentNumber(msg.getImei());
                        equipmentInfo = equipmentInfoService.selectEquipmentInfoByEquipmentNumber(equipmentInfo);
                        drugInfo.setEquipmentId(equipmentInfo.getId());
                        drugInfo.setDrugStatus(1);
                        drugInfoService.updateDrugInfo(drugInfo);
                        redisCache.setCacheMapValue(drugInfoKey,MEDICINAL_NUMBER,medicinalNumber);
                        redisCache.setCacheMapValue(drugInfoKey,TIME_REMAINING,drugTimeRemaining);
                    }
                }
            }else {
                //药包已使用完毕
                msg.setConfirm(3);
                msg.setCsq(0);
                redisCache.deleteObject(drugInfoKey);
            }


        }
        if (msg.getConfirm() != 3){
            equipmentInfoService.updateStateByEquipmentNumber(imei,2);
        }
        msg.setPackageHead(PACKAGE_HEAD);
        msg.setIccId("");
        msg.setPackageType(NettyConstants.ScanCodeReply);
        log.info("gainDrugInfoSendMessage:{}",JSONObject.toJSONString(msg));
        channel.writeAndFlush(msg);
    }

    /**
     * 给设备发送更新请求
     */
    public void sendUpdateRequest(){
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setEquipmentStatus(1L);
        equipmentInfo.setDeleted(0L);
        List<EquipmentInfo> equipmentInfos = equipmentInfoService.selectEquipmentInfoList(equipmentInfo);
        for (EquipmentInfo info : equipmentInfos) {
            Channel channel = channelMap.get(info.getEquipmentNumber());
            if (channel!=null){
                TCPBaseEntity msg =new TCPBaseEntity();
                msg.setPackageHead(PACKAGE_HEAD);
                msg.setImei(info.getEquipmentNumber());
                msg.setPackageType(9);
                //大版本号
                msg.setConfirm(0);
                //小版本号
                msg.setCsq(1);
                msg.setPersist("0000000000000000");
                channel.writeAndFlush(msg);
            }
        }
    }

    /**
     * 收到更新请求,并根据回复发送安装包
     * @param channel 通信
     * @param msg 消息：包含包头 1byte、包长 1byte、
     *            包类型 1byte、IMEI 15 byte、
     *            升级回复信息（01 通过、02 断点续传、03 拒绝） 1byte、
     *            包id 2byte、保留位 8byte、BCC校验 1byte
     *
     */
    public void receiveUpdateRequest(TCPBaseEntity msg, Channel channel){
        //升级请求的状态
        Integer updateRequestState = msg.getConfirm();
        //包id
        if (updateRequestState!=3){
            sendInstallationPackage(channel,msg);
        }

    }

    public void receivePushPackReply(TCPBaseEntity msg, Channel channel){
        //升级请求的状态
        Integer updateRequestState = msg.getConfirm();
        //包id
        if (updateRequestState!=3){
            sendInstallationPackage(channel,msg);
        }
    }


    /**
     * 发送安装包
     * @param channel: 当前链接通道
     * @param msg: 更新包ID，用于记录断点位置
     */
    public void sendInstallationPackage(Channel channel, TCPBaseEntity msg) {
        String filePath = "/home/work/boot/uploadPath/amt630h.bin";
//        String filePath = "D:\\home\\amt630h.bin";
        Integer updatePackageId = Integer.parseInt(msg.getPackageId(),16);
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(filePath, "r");

            long fileSize = file.length();
            long position = updatePackageId * CHUNK_SIZE < file.length() ?
                    updatePackageId * CHUNK_SIZE : file.length(); // 当前文件位置,断点续传位置
            byte[] buffer = new byte[CHUNK_SIZE];
            file.seek(position);

            int bytesRead;
            int remaining = (int) Math.min(fileSize - position, CHUNK_SIZE);
            bytesRead = file.read(buffer, 0, remaining);
            if (bytesRead > 0) {
                String region = bytesToHexString(buffer);
                msg.setPackageHead("E8");
                msg.setFileRegion(region);
                msg.setPackageId(String.format("%04X", updatePackageId+1).toUpperCase());
                msg.setPackageType(NettyConstants.FirmwarePushPack);
                msg.setConfirm(null);
                msg.setPackageTotal((int) fileSize / CHUNK_SIZE);
                channel.writeAndFlush(msg).addListener(future -> {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭文件
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void drugInfoUpdate(TCPBaseEntity msg, Channel channel){
        //设备id
        String imei = msg.getImei();
        String drugInfoKey = RedisKeys.getDrugInfoKey(imei);
        redisCache.setCacheMapValue(drugInfoKey,TIME_REMAINING,msg.getCsq());

        Object number = redisCache.getCacheMapValue(drugInfoKey, MEDICINAL_NUMBER);
        DrugInfo drugInfo = new DrugInfo();
        drugInfo.setDrugNumber(number.toString());
        drugInfo=drugInfoService.selectDrugInfoOne(drugInfo);
        drugInfo.setTimeRemaining((long)msg.getCsq() * 10);
        if(msg.getCsq()==0){
            drugInfo.setDrugStatus(2);
            redisCache.deleteObject(drugInfoKey);
        }
        //更新药包时长
        drugInfoService.updateDrugInfo(drugInfo);
        msg.setPackageHead(PACKAGE_HEAD);
        msg.setPackageType(NettyConstants.ChargeConfirmation);
        msg.setCsq(1);

        channel.writeAndFlush(msg);
    }

    /**
     * 设备脱机
     * @param imei 设备编码
     */
    public void equipmentOffline(String imei){
        redisCache.deleteObject(RedisKeys.getEquipmentStateKey(imei));
        equipmentInfoService.updateStateByEquipmentNumber(imei,0);
    }


}
