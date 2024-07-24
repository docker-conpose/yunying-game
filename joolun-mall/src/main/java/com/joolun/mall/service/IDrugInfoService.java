package com.joolun.mall.service;

import com.joolun.mall.entity.DrugInfo;
import com.joolun.mall.vo.DrugInfoChartVo;
import com.joolun.mall.vo.DrugInfoVo;

import java.util.List;

/**
 * 药包管理Service接口
 *
 * @author lx
 * @date 2023-12-18
 */
public interface IDrugInfoService
{
    /**
     * 查询药包管理
     *
     * @param id 药包管理ID
     * @return 药包管理
     */
    public DrugInfo selectDrugInfoById(Long id);

    /**
     * 查询药包管理列表
     *
     * @param drugInfo 药包管理
     * @return 药包管理集合
     */
    public List<DrugInfoVo> selectDrugInfoList(DrugInfoVo drugInfo);

    /**
     * 新增药包管理
     *
     * @param drugInfo 药包管理
     * @return 结果
     */
    public int insertDrugInfo(DrugInfo drugInfo);

    /**
     * 修改药包管理
     *
     * @param drugInfo 药包管理
     * @return 结果
     */
    public int updateDrugInfo(DrugInfo drugInfo);

    /**
     * 批量删除药包管理
     *
     * @param ids 需要删除的药包管理ID
     * @return 结果
     */
    public int deleteDrugInfoByIds(Long[] ids);


    /**
     * 根据某个条件查询单个药包信息
     * @param drugInfo
     * @return
     */
    public DrugInfo selectDrugInfoOne(DrugInfo drugInfo);


    public DrugInfo selectDrugInfoByState(String medicinalNumber);


    DrugInfoChartVo getDrugInfoChartData(String userId);

    /**
     *
     * @return 获取所有的药包总数
     */
    int getDrugTotal(DrugInfo drugInfo);
}
