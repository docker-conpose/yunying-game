package com.joolun.mall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joolun.common.annotation.Excel;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class DrugInfoVo {
    private Long id;

    /** 药包编号 */
    @Excel(name = "药包编号")
    private String drugNumber;

    /** 药包剩余时间 */
    @Excel(name = "药包剩余时间")
    private Long timeRemaining;

    /** 设备id */
    @Excel(name = "设备编码")
    private Long equipmentNumber;

    /** 分类 */
    @Excel(name = "分类")
    private String classifyName;

    /** 仓储状态，是否售出 */
    @Excel(name = "仓储状态")
    private Integer warehouseStatus;

    /** 药包状态：0-未使用，1-已使用，2-已用完 */
    @Excel(name = "药包状态")
    private Integer drugStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
