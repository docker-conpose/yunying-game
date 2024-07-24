package com.joolun.mall.mapper;

import com.joolun.mall.entity.DrugInfo;
import com.joolun.mall.vo.DrugInfoChartVo;
import com.joolun.mall.vo.DrugInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 药包管理Mapper接口
 *
 * @author lx
 * @date 2023-12-18
 */
@Mapper
public interface DrugInfoMapper
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
     * @param drugInfoVo 药包管理
     * @return 药包管理集合
     */
    public List<DrugInfoVo> selectDrugInfoList(DrugInfoVo drugInfoVo);

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
     * 删除药包管理
     *
     * @param id 药包管理ID
     * @return 结果
     */
    public int deleteDrugInfoById(Long id);

    /**
     * 批量删除药包管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDrugInfoByIds(Long[] ids);

    public DrugInfo selectDrugInfoOne(DrugInfo drugInfo);

    public DrugInfo selectDrugInfoByState(String medicinalNumber);


    Integer selectDrugInfoListCount(DrugInfo drugInfo);
}
