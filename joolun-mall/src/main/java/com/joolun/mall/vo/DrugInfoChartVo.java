package com.joolun.mall.vo;

import lombok.Data;

@Data
public class DrugInfoChartVo {
    /**
     *未使用
     */
    private Integer unusedDrug;
    /**
     *正在使用
     */
    private Integer inUseDrug;
    /**
     * 已用完
     */
    private Integer usedUpDrug;
}
