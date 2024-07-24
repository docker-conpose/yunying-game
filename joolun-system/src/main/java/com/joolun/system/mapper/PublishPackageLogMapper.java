package com.joolun.system.mapper;

import java.util.List;
import com.joolun.system.domain.PublishPackageLog;

/**
 * PublishPackageLogMapper接口
 * 
 * @author lx
 * @date 2024-03-28
 */
public interface PublishPackageLogMapper 
{
    public List<PublishPackageLog> selectPublishPackageLogList(PublishPackageLog publishPackageLog);

    /**
     * 上传发行包
     */
    public int uploadingPublishPackage(PublishPackageLog publishPackageLog);

    /**
     * 发布安装包
     */
    public int publishPackage(PublishPackageLog publishPackageLog);
}
