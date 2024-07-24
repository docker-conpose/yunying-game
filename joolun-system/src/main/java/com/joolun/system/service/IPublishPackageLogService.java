package com.joolun.system.service;

import java.util.List;
import com.joolun.system.domain.PublishPackageLog;

public interface IPublishPackageLogService 
{
    public List<PublishPackageLog> selectPublishPackageLogList(PublishPackageLog publishPackageLog);

    /**
     * 上传发行包
     * @param publishPackageLog: 包数据
     * @return 是否成功
     */
    public int uploadingPublishPackage(PublishPackageLog publishPackageLog);

    /**
     * 发布安装包
     */
    public int publishPackage(PublishPackageLog publishPackageLog);

}
