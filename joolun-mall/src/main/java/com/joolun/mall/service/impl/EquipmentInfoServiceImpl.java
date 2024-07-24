package com.joolun.mall.service.impl;

import com.joolun.common.utils.DateUtils;
import com.joolun.mall.entity.EquipmentInfo;
import com.joolun.mall.mapper.EquipmentInfoMapper;
import com.joolun.mall.service.IEquipmentInfoService;
import com.joolun.mall.vo.EquipmentChartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备管理Service业务层处理
 *
 * @author lx
 * @date 2023-12-18
 */
@Service
public class EquipmentInfoServiceImpl implements IEquipmentInfoService
{
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;

    /**
     * 查询设备管理
     *
     * @param id 设备管理ID
     * @return 设备管理
     */
    @Override
    public EquipmentInfo selectEquipmentInfoById(Long id)
    {
        return equipmentInfoMapper.selectEquipmentInfoById(id);
    }

    /**
     * 查询设备管理列表
     *
     * @param equipmentInfo 设备管理
     * @return 设备管理
     */
    @Override
    public List<EquipmentInfo> selectEquipmentInfoList(EquipmentInfo equipmentInfo)
    {
        return equipmentInfoMapper.selectEquipmentInfoList(equipmentInfo);
    }

    @Override
    public EquipmentInfo selectEquipmentInfoByEquipmentNumber(EquipmentInfo equipmentInfo) {
        return equipmentInfoMapper.selectEquipmentInfoByEquipmentNumber(equipmentInfo);
    }

    /**
     * 新增设备管理
     *
     * @param equipmentInfo 设备管理
     * @return 结果
     */
    @Override
    public int insertEquipmentInfo(EquipmentInfo equipmentInfo)
    {
        equipmentInfo.setUpdateTime(DateUtils.getNowDate());
        equipmentInfo.setCreateTime(DateUtils.getNowDate());
        return equipmentInfoMapper.insertEquipmentInfo(equipmentInfo);
    }

    /**
     * 修改设备管理
     *
     * @param equipmentInfo 设备管理
     * @return 结果
     */
    @Override
    public int updateEquipmentInfo(EquipmentInfo equipmentInfo)
    {
        equipmentInfo.setUpdateTime(DateUtils.getNowDate());
        return equipmentInfoMapper.updateEquipmentInfo(equipmentInfo);
    }

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理ID
     * @return 结果
     */
    @Override
    public int deleteEquipmentInfoByIds(Long[] ids)
    {
        return equipmentInfoMapper.deleteEquipmentInfoByIds(ids);
    }

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理ID
     * @return 结果
     */
    @Override
    public int deleteEquipmentInfoById(Long id)
    {
        return equipmentInfoMapper.deleteEquipmentInfoById(id);
    }

    /**
     * 根据设备编码修改设备信息
     * @param equipmentNumber 设备编码
     * @param equipmentState 设备状态
     */
    @Override
    public void updateStateByEquipmentNumber(String equipmentNumber, int equipmentState) {
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setEquipmentStatus((long) equipmentState);
        equipmentInfo.setEquipmentNumber(equipmentNumber);
        equipmentInfo.setUpdateTime(DateUtils.getNowDate());
        equipmentInfoMapper.updateStateByEquipmentNumber(equipmentInfo);
    }

    @Override
    public void inputEquipment(String equipmentNumber) {

    }

    @Override
    public EquipmentChartVo getEquipmentChartData(String userId) {
        EquipmentChartVo vo = new EquipmentChartVo();
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setMerchantId(userId);
        equipmentInfo.setEquipmentStatus(1L);
        vo.setEquipmentOnline(equipmentInfoMapper.selectEquipmentInfoListCount(equipmentInfo));
        equipmentInfo.setEquipmentStatus(0L);
        vo.setEquipmentOffline(equipmentInfoMapper.selectEquipmentInfoListCount(equipmentInfo));
        equipmentInfo.setEquipmentStatus(2L);
        vo.setEquipmentInUse(equipmentInfoMapper.selectEquipmentInfoListCount(equipmentInfo));
        return vo;
    }

    @Override
    public Integer getEquipmentTotal(EquipmentInfo equipmentInfo) {

        return equipmentInfoMapper.selectEquipmentInfoListCount(equipmentInfo);
    }
}
