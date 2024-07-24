package com.joolun.mall.vo;

import lombok.Data;

@Data
public class TotalDataInfo {
    /**
     * 用户数据
     */
    private Integer userTotal;

    /**
     * 设备数据
     */
    private Integer equipmentTotal;

    /**
     * 药包数据
     */
    private Integer drugTotal;

    /**
     * 订单数据
     */
    private Integer orderTotal;
}
