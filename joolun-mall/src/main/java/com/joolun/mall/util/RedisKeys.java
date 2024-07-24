package com.joolun.mall.util;

/**
 * Redis Key管理
 */
public class RedisKeys {

    /**
     * 验证码Key
     */
    public static String getCaptchaKey(String key) {
        return "sys:captcha:" + key;
    }

    /**
     * accessToken Key
     */
    public static String getAccessTokenKey(String accessToken) {
        return "sys:access:" + accessToken;
    }

    /**
     * 根据Imei获取设备状态
     */
    public static String getEquipmentStateKey(String imei) {
        return "equipment:state:" + imei;
    }
    /**
     * 根据Imei和药包code获取药包的时间
     */
    public static String getDrugInfoKey(String imei) {
        return String.format("equipment:drugInfo:%s" ,imei);
    }

    /**
     * 根据Imei获取设备链接
     */
    public static String getChannelKey(String imei) {
        return String.format("equipment:channel:%s" ,imei);
    }
}
