package com.joolun.mall.mapper;

import com.joolun.mall.entity.EquipmentInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备管理Mapper接口
 *
 * @author lx
 * @date 2023-12-18
 */
@Mapper
public interface EquipmentInfoMapper
{
    /**
     * 查询设备管理
     *
     * @param id 设备管理ID
     * @return 设备管理
     */
    public EquipmentInfo selectEquipmentInfoById(Long id);

    /**
     * 查询设备管理列表
     *
     * @param equipmentInfo 设备管理
     * @return 设备管理集合
     */
    public List<EquipmentInfo> selectEquipmentInfoList(EquipmentInfo equipmentInfo);

    public Integer selectEquipmentInfoListCount(EquipmentInfo equipmentInfo);

    /**
     * 查询设备信息
     * @param equipmentInfo 设备信息
     * @return
     */
    public EquipmentInfo selectEquipmentInfoByEquipmentNumber(EquipmentInfo equipmentInfo);

    /**
     * 新增设备管理
     *
     * @param equipmentInfo 设备管理
     * @return 结果
     */
    public int insertEquipmentInfo(EquipmentInfo equipmentInfo);

    /**
     * 修改设备管理
     *
     * @param equipmentInfo 设备管理
     * @return 结果
     */
    public int updateEquipmentInfo(EquipmentInfo equipmentInfo);

    /**
     * 删除设备管理
     *
     * @param id 设备管理ID
     * @return 结果
     */
    public int deleteEquipmentInfoById(Long id);

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEquipmentInfoByIds(Long[] ids);

    /**
     * 根据设备编码修改设备信息
     */
    void updateStateByEquipmentNumber(EquipmentInfo equipmentInfo);
}
