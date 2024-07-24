package com.joolun.mall.vo;

import lombok.Data;

@Data
public class OrderChartVo {
    /**
     *等待发货数量
     */
    private Integer waitForSending;
    /**
     *等待收货数量
     */
    private Integer waitForDelivery;
    /**
     * 等待确认数量
     */
    private Integer waitForConfirmation;
}
