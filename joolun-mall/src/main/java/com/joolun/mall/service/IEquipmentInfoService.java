package com.joolun.mall.service;

import com.joolun.mall.entity.EquipmentInfo;
import com.joolun.mall.vo.EquipmentChartVo;

import java.util.List;

/**
 * 设备管理Service接口
 *
 * @author lx
 * @date 2023-12-18
 */
public interface IEquipmentInfoService
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

    /**
     * 查询设备信息
     *
     * @param equipmentInfo 设备管理
     * @return 设备管理集合
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
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理ID
     * @return 结果
     */
    public int deleteEquipmentInfoByIds(Long[] ids);

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理ID
     * @return 结果
     */
    public int deleteEquipmentInfoById(Long id);

    /**
     * 根据设备编码修改设备信息
     * @param equipmentNumber 设备编码
     * @param equipmentState 设备状态
     */
    void updateStateByEquipmentNumber(String equipmentNumber, int equipmentState);

    /**
     * 录入设备
     * @param equipmentNumber 设备编码
     */
    void inputEquipment(String equipmentNumber);

    /**
     * 小程序页面展示图数据
     * @param userId
     * @return
     */
    EquipmentChartVo getEquipmentChartData(String userId);

    Integer getEquipmentTotal(EquipmentInfo equipmentInfo);
}
