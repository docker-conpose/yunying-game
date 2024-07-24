package com.joolun.web.controller.tool;

import java.util.List;

import com.joolun.web.netty.NettyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.joolun.common.annotation.Log;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.enums.BusinessType;
import com.joolun.system.domain.PublishPackageLog;
import com.joolun.system.service.IPublishPackageLogService;
import com.joolun.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/system/ota")
public class OTAController extends BaseController
{
    @Autowired
    private IPublishPackageLogService publishPackageLogService;

    @Autowired
    private NettyService nettyService;


    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:ota:list')")
    @GetMapping("/list")
    public TableDataInfo list(PublishPackageLog publishPackageLog)
    {
        startPage();
        List<PublishPackageLog> list = publishPackageLogService.selectPublishPackageLogList(publishPackageLog);
        return getDataTable(list);
    }
    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:ota:add')")
    @Log(title = "上传", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PublishPackageLog publishPackageLog)
    {
        return toAjax(publishPackageLogService.uploadingPublishPackage(publishPackageLog));
    }

    @PreAuthorize("@ss.hasPermi('system:ota:edit')")
    @Log(title = "发布", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PublishPackageLog publishPackageLog)
    {
        nettyService.sendUpdateRequest();
        return toAjax(publishPackageLogService.publishPackage(publishPackageLog));
    }
}
