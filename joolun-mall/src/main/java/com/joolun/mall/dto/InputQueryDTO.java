package com.joolun.mall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "录入请求参数")
public class InputQueryDTO {
    /**
     * 1：设备录入
     * 0：药包录入
     */
    @ApiModelProperty("查询类型")
    private Integer status;
    /**
     * 1：查询当天录入
     * 0：查询所有的录入
     */
    @ApiModelProperty("是否查询全部")
    private Integer allStatus;

    @ApiModelProperty("是否售出")
    private Integer warehouseStatus;

    @ApiModelProperty("查询编码")
    private String number;
}
