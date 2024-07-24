package com.joolun.web.controller.mall;

import com.joolun.common.annotation.Log;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.core.page.TableDataInfo;
import com.joolun.common.enums.BusinessType;
import com.joolun.common.utils.poi.ExcelUtil;
import com.joolun.mall.entity.DrugInfo;
import com.joolun.mall.service.IDrugInfoService;
import com.joolun.mall.vo.DrugInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 药包管理Controller
 *
 * @author lx
 * @date 2023-12-18
 */
@RestController
@RequestMapping("/mall/drug")
public class DrugInfoController extends BaseController
{
    @Autowired
    private IDrugInfoService drugInfoService;

    /**
     * 查询药包管理列表
     */
    @PreAuthorize("@ss.hasPermi('mall:drug:list')")
    @GetMapping("/list")
    public TableDataInfo list(DrugInfoVo drugInfo)
    {
        startPage();
        List<DrugInfoVo> list = drugInfoService.selectDrugInfoList(drugInfo);
        return getDataTable(list);
    }

    /**
     * 导出药包管理列表
     */
    @PreAuthorize("@ss.hasPermi('mall:drug:export')")
    @Log(title = "药包管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DrugInfoVo drugInfo)
    {
        List<DrugInfoVo> list = drugInfoService.selectDrugInfoList(drugInfo);
        ExcelUtil<DrugInfoVo> util = new ExcelUtil<>(DrugInfoVo.class);
        return util.exportExcel(list, "drug");
    }

    /**
     * 获取药包管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mall:drug:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(drugInfoService.selectDrugInfoById(id));
    }

    /**
     * 新增药包管理
     */
    @PreAuthorize("@ss.hasPermi('mall:drug:add')")
    @Log(title = "药包管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DrugInfo drugInfo)
    {
        return toAjax(drugInfoService.insertDrugInfo(drugInfo));
    }

    /**
     * 修改药包管理
     */
    @PreAuthorize("@ss.hasPermi('mall:drug:edit')")
    @Log(title = "药包管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DrugInfo drugInfo)
    {
        return toAjax(drugInfoService.updateDrugInfo(drugInfo));
    }

    /**
     * 删除药包管理
     */
    @PreAuthorize("@ss.hasPermi('mall:drug:remove')")
    @Log(title = "药包管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(drugInfoService.deleteDrugInfoByIds(ids));
    }
}