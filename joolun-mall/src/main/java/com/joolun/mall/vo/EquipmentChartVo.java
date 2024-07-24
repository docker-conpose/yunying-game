package com.joolun.mall.vo;

import lombok.Data;

@Data
public class EquipmentChartVo {
    /**
     * 在线设备
     */
    private Integer equipmentOnline;

    /**
     * 在使用设备
     */
    private Integer equipmentInUse;

    /**
     * 离线设备
     */
    private Integer equipmentOffline;
}
