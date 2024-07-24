package com.joolun.mall.service.impl;

import com.joolun.common.utils.DateUtils;
import com.joolun.mall.entity.DrugInfo;
import com.joolun.mall.mapper.DrugInfoMapper;
import com.joolun.mall.service.IDrugInfoService;
import com.joolun.mall.vo.DrugInfoChartVo;
import com.joolun.mall.vo.DrugInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 药包管理Service业务层处理
 *
 * @author lx
 * @date 2023-12-18
 */
@Service
public class DrugInfoServiceImpl implements IDrugInfoService
{
    @Autowired
    private DrugInfoMapper drugInfoMapper;

    /**
     * 查询药包管理
     *
     * @param id 药包管理ID
     * @return 药包管理
     */
    @Override
    public DrugInfo selectDrugInfoById(Long id)
    {
        return drugInfoMapper.selectDrugInfoById(id);
    }

    /**
     * 查询药包管理列表
     *
     * @param drugInfo 药包管理
     * @return 药包管理
     */
    @Override
    public List<DrugInfoVo> selectDrugInfoList(DrugInfoVo drugInfo)
    {
        return drugInfoMapper.selectDrugInfoList(drugInfo);
    }

    /**
     * 新增药包管理
     *
     * @param drugInfo 药包管理
     * @return 结果
     */
    @Override
    public int insertDrugInfo(DrugInfo drugInfo)
    {
        drugInfo.setCreateTime(DateUtils.getNowDate());
        return drugInfoMapper.insertDrugInfo(drugInfo);
    }

    /**
     * 修改药包管理
     *
     * @param drugInfo 药包管理
     * @return 结果
     */
    @Override
    public int updateDrugInfo(DrugInfo drugInfo)
    {
        drugInfo.setUpdateTime(DateUtils.getNowDate());
        return drugInfoMapper.updateDrugInfo(drugInfo);
    }

    /**
     * 批量删除药包管理
     *
     * @param ids 需要删除的药包管理ID
     * @return 结果
     */
    @Override
    public int deleteDrugInfoByIds(Long[] ids)
    {
        return drugInfoMapper.deleteDrugInfoByIds(ids);
    }


    @Override
    public DrugInfo selectDrugInfoOne(DrugInfo drugInfo) {
        return drugInfoMapper.selectDrugInfoOne(drugInfo);
    }

    @Override
    public DrugInfo selectDrugInfoByState(String medicinalNumber) {
        return drugInfoMapper.selectDrugInfoByState(medicinalNumber);
    }

    @Override
    public DrugInfoChartVo getDrugInfoChartData(String userId) {
        DrugInfo drugInfo = new DrugInfo();
        drugInfo.setMerchantId(userId);
        DrugInfoChartVo vo = new DrugInfoChartVo();
        drugInfo.setDrugStatus(1);
        vo.setInUseDrug(drugInfoMapper.selectDrugInfoListCount(drugInfo));
        drugInfo.setDrugStatus(0);
        vo.setUnusedDrug(drugInfoMapper.selectDrugInfoListCount(drugInfo));
        drugInfo.setDrugStatus(2);
        vo.setUsedUpDrug(drugInfoMapper.selectDrugInfoListCount(drugInfo));
        return vo;
    }

    @Override
    public int getDrugTotal(DrugInfo drugInfo) {
        return drugInfoMapper.selectDrugInfoListCount(drugInfo);
    }
}
