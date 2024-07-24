package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.joolun.common.annotation.Excel;
import com.joolun.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备管理对象 equipment_info
 *
 * @author lx
 * @date 2023-12-18
 */
public class EquipmentInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String equipmentNumber;

    /** SIM卡卡号 */
    @Excel(name = "SIM卡卡号")
    private String iccid;

    /** 分类Id */
    @Excel(name = "分类Id")
    private String classifyId;

    /** 购买商户id */
    @Excel(name = "购买商户id")
    private String merchantId;

    /** 硬件版本 */
    @Excel(name = "硬件版本")
    private Long hardwareVersion;

    /** 软件版本 */
    @Excel(name = "软件版本")
    private Long softwareVersion;

    /** 设备状态：0-离线，1-在线，2-使用中 */
    @Excel(name = "设备状态：0-离线，1-在线，2-使用中")
    private Long equipmentStatus;

    /** 仓储状态，是否售出 */
    @Excel(name = "仓储状态，是否售出")
    private Long warehouseStatus;

    /** $column.columnComment */
    @Excel(name = "仓储状态，是否售出")
    private Long deleted;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setEquipmentNumber(String equipmentNumber)
    {
        this.equipmentNumber = equipmentNumber;
    }

    public String getEquipmentNumber()
    {
        return equipmentNumber;
    }
    public void setIccid(String iccid)
    {
        this.iccid = iccid;
    }

    public String getIccid()
    {
        return iccid;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setHardwareVersion(Long hardwareVersion)
    {
        this.hardwareVersion = hardwareVersion;
    }

    public Long getHardwareVersion()
    {
        return hardwareVersion;
    }
    public void setSoftwareVersion(Long softwareVersion)
    {
        this.softwareVersion = softwareVersion;
    }

    public Long getSoftwareVersion()
    {
        return softwareVersion;
    }
    public void setEquipmentStatus(Long equipmentStatus)
    {
        this.equipmentStatus = equipmentStatus;
    }

    public Long getEquipmentStatus()
    {
        return equipmentStatus;
    }
    public void setWarehouseStatus(Long warehouseStatus)
    {
        this.warehouseStatus = warehouseStatus;
    }

    public Long getWarehouseStatus()
    {
        return warehouseStatus;
    }
    public void setDeleted(Long deleted)
    {
        this.deleted = deleted;
    }

    public Long getDeleted()
    {
        return deleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("equipmentNumber", getEquipmentNumber())
                .append("iccid", getIccid())
                .append("hardwareVersion", getHardwareVersion())
                .append("softwareVersion", getSoftwareVersion())
                .append("equipmentStatus", getEquipmentStatus())
                .append("warehouseStatus", getWarehouseStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("deleted", getDeleted())
                .toString();
    }
}
