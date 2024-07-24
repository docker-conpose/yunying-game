package com.joolun.system.service.impl;

import java.util.List;
import com.joolun.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.joolun.system.mapper.PublishPackageLogMapper;
import com.joolun.system.domain.PublishPackageLog;
import com.joolun.system.service.IPublishPackageLogService;

import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author www.joolun.com
 * @date 2024-03-28
 */
@Service
public class PublishPackageLogServiceImpl implements IPublishPackageLogService 
{
    @Autowired
    private PublishPackageLogMapper publishPackageLogMapper;

    @Override
    public List<PublishPackageLog> selectPublishPackageLogList(PublishPackageLog publishPackageLog)
    {
        return publishPackageLogMapper.selectPublishPackageLogList(publishPackageLog);
    }

    @Override
    public int uploadingPublishPackage(PublishPackageLog publishPackageLog)
    {
        publishPackageLog.setCreateTime(DateUtils.getNowDate());
        return publishPackageLogMapper.uploadingPublishPackage(publishPackageLog);
    }

    @Override
    public int publishPackage(PublishPackageLog publishPackageLog)
    {

        publishPackageLog.setUpdateTime(DateUtils.getNowDate());

        return publishPackageLogMapper.publishPackage(publishPackageLog);
    }
}
