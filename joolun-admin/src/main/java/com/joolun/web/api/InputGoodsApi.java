package com.joolun.web.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageHelper;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.utils.sql.SqlUtil;
import com.joolun.mall.dto.InputQueryDTO;
import com.joolun.mall.entity.DrugInfo;
import com.joolun.mall.entity.EquipmentInfo;
import com.joolun.mall.entity.GoodsCategory;
import com.joolun.mall.service.GoodsCategoryService;
import com.joolun.mall.service.IDrugInfoService;
import com.joolun.mall.service.IEquipmentInfoService;
import com.joolun.mall.service.InputGoodsService;
import com.joolun.weixin.constant.MyReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"/weixin/api/ma/inputgoods"})
@Api(value = "inputgoods", tags = {"录入API"})
public class InputGoodsApi extends BaseController {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Autowired
    private IDrugInfoService drugInfoService;

    @Autowired
    private InputGoodsService inputGoodsService;

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @ApiOperation("录入设备")
    @GetMapping({"/home"})
    public AjaxResult home() {
        return AjaxResult.success(inputGoodsService.home());
    }

    @ApiOperation("录入设备")
    @PostMapping({"/inputEquipment"})
    public AjaxResult inputEquipment(@RequestBody String equipmentNumber) {
        if (equipmentNumber.isEmpty()) {
            return AjaxResult.error(MyReturnCode.ERR_60003.getCode(), MyReturnCode.ERR_60003.getMsg());
        } else {
            EquipmentInfo equipmentInfo = new EquipmentInfo();
            equipmentInfo.setEquipmentNumber(equipmentNumber);
            equipmentInfo=equipmentInfoService.selectEquipmentInfoByEquipmentNumber(equipmentInfo);
            if (equipmentInfo.getId() != null) {
                return AjaxResult.error(MyReturnCode.ERR_60004.getCode(), MyReturnCode.ERR_60004.getMsg());
            } else {
                equipmentInfoService.inputEquipment(equipmentNumber);
                GoodsCategory goodsSpu = goodsCategoryService.getById(equipmentInfo.getClassifyId());
                goodsSpu.setStock(goodsSpu.getStock() + 1);
                goodsCategoryService.updateById(goodsSpu);
                return AjaxResult.success();
            }
        }
    }

    @ApiOperation("录入药包")
    @PostMapping({"/inputDrug"})
    public AjaxResult inputDrug(@RequestBody DrugInfo drugInfo) {
        if (drugInfo.getDrugNumber().isEmpty()) {
            return AjaxResult.error(MyReturnCode.ERR_60003.getCode(), MyReturnCode.ERR_60003.getMsg());
        } else {
            DrugInfo drug = drugInfoService.selectDrugInfoOne(drugInfo);
            if (drug != null) {
                return AjaxResult.error(MyReturnCode.ERR_60004.getCode(), MyReturnCode.ERR_60004.getMsg());
            } else {
                drugInfo.setDrugStatus(0);
                drugInfo.setWarehouseStatus(0);
                drugInfoService.insertDrugInfo(drugInfo);
                GoodsCategory goodsSpu = goodsCategoryService.getById(drugInfo.getClassifyId());
                goodsSpu.setStock(goodsSpu.getStock() + 1);
                goodsCategoryService.updateById(goodsSpu);
                return AjaxResult.success();
            }
        }
    }

    @ApiOperation("录入列表")
    @GetMapping({"/inputList"})
    public AjaxResult inputList(IPage<?> page, InputQueryDTO inputQuery) {
        String orderBy = SqlUtil.escapeOrderBySql("create_time");
        PageHelper.startPage((int)page.getCurrent(), (int)page.getSize(), orderBy);
        return  inputQuery.getAllStatus() != null ? AjaxResult.success(inputGoodsService.page(inputQuery)) :
                AjaxResult.error(MyReturnCode.ERR_60003.getCode(), MyReturnCode.ERR_60003.getMsg());
    }
}
