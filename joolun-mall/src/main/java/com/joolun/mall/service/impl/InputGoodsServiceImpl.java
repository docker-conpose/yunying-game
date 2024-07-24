package com.joolun.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.mall.dto.InputQueryDTO;
import com.joolun.mall.entity.EquipmentInfo;
import com.joolun.mall.service.IEquipmentInfoService;
import com.joolun.mall.service.InputGoodsService;
import com.joolun.mall.vo.DrugInfoVo;
import com.joolun.mall.vo.InputHomeVo;
import com.joolun.mall.vo.InputQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InputGoodsServiceImpl implements InputGoodsService {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private DrugInfoServiceImpl drugInfoService;

    public IPage<InputQueryVo> page(InputQueryDTO inputQuery) {
        List<InputQueryVo> vos=new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();

        List<EquipmentInfo> equipmentInfoList = new ArrayList<>();
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setEquipmentNumber(inputQuery.getNumber());

        List<DrugInfoVo> drugInfoVos = new ArrayList<>();
        DrugInfoVo drugInfo = new DrugInfoVo();
        drugInfo.setDrugNumber(inputQuery.getNumber());
        IPage<InputQueryVo> iPage=new Page<>();
        // 查询当天的录入情况
        if (inputQuery.getAllStatus() == 1) {
            // Status:1为设备,2为药包
            if (inputQuery.getStatus() == 1) {
                //当天查询
                equipmentInfo.setCreateTime(new Date());
            }else {
                drugInfo.setCreateTime(new Date());
            }
        }
        if (inputQuery.getStatus() == 1){
            equipmentInfoList = equipmentInfoService.selectEquipmentInfoList(equipmentInfo);
            equipmentInfoList.forEach( info -> {
                InputQueryVo vo = new InputQueryVo();
                vo.setNumber(info.getEquipmentNumber());
                vo.setCreateTime(info.getCreateTime().toInstant().atZone(zoneId).toLocalDateTime());
                vos.add(vo);
            });
        }else {
            drugInfoVos = drugInfoService.selectDrugInfoList(drugInfo);
            drugInfoVos.forEach(info->{
                InputQueryVo vo = new InputQueryVo();
                vo.setNumber(info.getDrugNumber());
                vo.setCreateTime(info.getCreateTime().toInstant().atZone(zoneId).toLocalDateTime());
                vos.add(vo);
            });
        }
        iPage.setTotal(vos.size());
        return iPage.setRecords(vos);
    }

    public InputHomeVo home() {
        InputHomeVo inputHomeVo = new InputHomeVo();

        DrugInfoVo drugInfo = new DrugInfoVo();
        drugInfo.setWarehouseStatus(0);
        inputHomeVo.setDueOutDrug(0);
        inputHomeVo.setDueOutDrug(0);
        inputHomeVo.setStockDrug(drugInfoService.selectDrugInfoList(drugInfo).size());
        drugInfo.setCreateTime(new Date());
        inputHomeVo.setTodayEnteringDrug(drugInfoService.selectDrugInfoList(drugInfo).size());


        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setWarehouseStatus(0L);
        inputHomeVo.setDueOutEquipment(2);
        inputHomeVo.setStockEquipment(equipmentInfoService.selectEquipmentInfoList(equipmentInfo).size());
        equipmentInfo.setCreateTime(new Date());
        inputHomeVo.setTodayEnteringEquipment(equipmentInfoService.selectEquipmentInfoList(equipmentInfo).size());
        return inputHomeVo;
    }
}
