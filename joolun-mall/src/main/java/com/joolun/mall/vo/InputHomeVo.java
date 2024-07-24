package com.joolun.mall.vo;

import lombok.Data;

@Data
public class InputHomeVo {

    /**
     * 待发药包数量
     */
    private int dueOutDrug;

    /**
     * 待发设备数量
     */
    private int dueOutEquipment;

    /**
     * 今日录入设备数量
     */
    private int todayEnteringEquipment;

    /**
     * 今日录入药包数量
     */
    private int todayEnteringDrug;

    /**
     * 仓储药包数量
     */
    private int stockDrug;

    /**
     * 仓储设备数量
     */
    private int stockEquipment;
}
