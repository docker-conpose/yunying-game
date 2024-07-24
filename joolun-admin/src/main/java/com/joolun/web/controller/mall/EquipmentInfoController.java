package com.joolun.web.controller.mall;

import com.joolun.common.annotation.Log;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.core.page.TableDataInfo;
import com.joolun.common.enums.BusinessType;
import com.joolun.common.utils.poi.ExcelUtil;
import com.joolun.mall.entity.EquipmentInfo;
import com.joolun.mall.service.IEquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理Controller
 *
 * @author lx
 * @date 2023-12-18
 */
@RestController
@RequestMapping("/equipment")
public class EquipmentInfoController extends BaseController
{
    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    /**
     * 查询设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('equipment:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(EquipmentInfo equipmentInfo)
    {
        startPage();
        List<EquipmentInfo> list = equipmentInfoService.selectEquipmentInfoList(equipmentInfo);
        return getDataTable(list);
    }

    /**
     * 导出设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('equipment:info:export')")
    @Log(title = "设备管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(EquipmentInfo equipmentInfo)
    {
        List<EquipmentInfo> list = equipmentInfoService.selectEquipmentInfoList(equipmentInfo);
        ExcelUtil<EquipmentInfo> util = new ExcelUtil<>(EquipmentInfo.class);
        return util.exportExcel(list, "info");
    }

    /**
     * 获取设备管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('equipment:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(equipmentInfoService.selectEquipmentInfoById(id));
    }

    /**
     * 新增设备管理
     */
    @PreAuthorize("@ss.hasPermi('equipment:info:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EquipmentInfo equipmentInfo)
    {
        return toAjax(equipmentInfoService.insertEquipmentInfo(equipmentInfo));
    }

    /**
     * 修改设备管理
     */
    @PreAuthorize("@ss.hasPermi('equipment:info:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EquipmentInfo equipmentInfo)
    {
        return toAjax(equipmentInfoService.updateEquipmentInfo(equipmentInfo));
    }

    /**
     * 删除设备管理
     */
    @PreAuthorize("@ss.hasPermi('equipment:info:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(equipmentInfoService.deleteEquipmentInfoByIds(ids));
    }
}

