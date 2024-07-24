package com.joolun.mall.entity;

import io.netty.channel.FileRegion;
import lombok.Data;

@Data
public class TCPBaseEntity {
    //包头
    private String packageHead;
    //包长
    private Integer packageLength;
    //包类型
    private Integer packageType;
    //IMEI,4G模块的唯一编号,共15字节
    private String imei;
    //ICCID,SIM卡的唯一编号，共20字节
    private String iccId;
    //确认位
    private Integer confirm;
    //CSQ 1-31
    private Integer csq;
    //保留位
    private String persist;
    //BCC校验
    private String verification;
    //包id
    private String packageId;
    //文件
    private String fileRegion;
    //包总数
    private Integer packageTotal;
}
