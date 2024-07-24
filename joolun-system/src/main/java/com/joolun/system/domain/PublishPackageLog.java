package com.joolun.system.domain;

import com.joolun.common.annotation.Excel;
import com.joolun.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 安装包记录
 * @author lx
 * @date 2024-03-28
 */
public class PublishPackageLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 版本号：大版本-小版本 */
    @Excel(name = "版本号：大版本-小版本")
    private String version;

    /** 发行状态：0-未发行、1-当前版本 */
    @Excel(name = "发行状态：0-未发行、1-当前版本")
    private Long publishState;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setVersion(String version) 
    {
        this.version = version;
    }

    public String getVersion() 
    {
        return version;
    }
    public void setPublishState(Long publishState) 
    {
        this.publishState = publishState;
    }

    public Long getPublishState() 
    {
        return publishState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("version", getVersion())
            .append("publishState", getPublishState())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
