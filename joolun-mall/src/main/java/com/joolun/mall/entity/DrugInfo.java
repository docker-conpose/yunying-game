package com.joolun.mall.entity;


import com.joolun.common.annotation.Excel;
import com.joolun.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 药包管理对象 drug_info
 *
 * @author lx
 * @date 2023-12-18
 */
public class DrugInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 药包编号 */
    @Excel(name = "药包编号")
    private String drugNumber;

    /** 药包剩余时间 */
    @Excel(name = "药包剩余时间")
    private Long timeRemaining;

    /** 设备id */
    @Excel(name = "设备id")
    private Long equipmentId;

    /** 分类id */
    @Excel(name = "分类id")
    private Long classifyId;

    @ApiModelProperty("购买商户id")
    private String merchantId;

    /** 仓储状态，是否售出 */
    @Excel(name = "仓储状态，是否售出")
    private Integer warehouseStatus;

    /** 药包状态：0-未使用，1-已使用，2-已用完 */
    @Excel(name = "药包状态：0-未使用，1-已使用，2-已用完")
    private Integer drugStatus;

    /** $column.columnComment */
    private Integer deleted;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDrugNumber(String drugNumber)
    {
        this.drugNumber = drugNumber;
    }

    public String getDrugNumber()
    {
        return drugNumber;
    }
    public void setTimeRemaining(Long timeRemaining)
    {
        this.timeRemaining = timeRemaining;
    }

    public Long getTimeRemaining()
    {
        return timeRemaining;
    }
    public void setEquipmentId(Long equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public Long getEquipmentId()
    {
        return equipmentId;
    }
    public void setClassifyId(Long classifyId)
    {
        this.classifyId = classifyId;
    }

    public Long getClassifyId()
    {
        return classifyId;
    }
    public void setWarehouseStatus(Integer warehouseStatus)
    {
        this.warehouseStatus = warehouseStatus;
    }

    public Integer getWarehouseStatus()
    {
        return warehouseStatus;
    }
    public void setDrugStatus(Integer drugStatus)
    {
        this.drugStatus = drugStatus;
    }

    public Integer getDrugStatus()
    {
        return drugStatus;
    }
    public void setDeleted(Integer deleted)
    {
        this.deleted = deleted;
    }

    public Integer getDeleted()
    {
        return deleted;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("drugNumber", getDrugNumber())
                .append("timeRemaining", getTimeRemaining())
                .append("equipmentId", getEquipmentId())
                .append("classifyId", getClassifyId())
                .append("warehouseStatus", getWarehouseStatus())
                .append("drugStatus", getDrugStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("deleted", getDeleted())
                .toString();
    }
}
