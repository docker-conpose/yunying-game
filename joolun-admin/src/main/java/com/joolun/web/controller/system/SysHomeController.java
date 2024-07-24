package com.joolun.web.controller.system;

import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.DrugInfo;
import com.joolun.mall.entity.EquipmentInfo;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.service.IDrugInfoService;
import com.joolun.mall.service.IEquipmentInfoService;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.vo.DrugInfoChartVo;
import com.joolun.mall.vo.EquipmentChartVo;
import com.joolun.mall.vo.OrderChartVo;
import com.joolun.mall.vo.TotalDataInfo;
import com.joolun.weixin.service.WxUserService;
import com.joolun.weixin.service.impl.WxUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/system/home")
public class SysHomeController {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private IDrugInfoService drugInfoService;

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 获取各个总数的数据
     * @return TotalDataInfo
     */
    @GetMapping("/getTotalDataInfo")
    public TotalDataInfo getTotalDataInfo()
    {
        TotalDataInfo totalDataInfo = new TotalDataInfo();
        totalDataInfo.setUserTotal(wxUserService.getWxUserTotal());
        totalDataInfo.setEquipmentTotal(equipmentInfoService.getEquipmentTotal(new EquipmentInfo()));
        totalDataInfo.setDrugTotal(drugInfoService.getDrugTotal(new DrugInfo()));
        totalDataInfo.setOrderTotal(orderInfoService.orderTotal(new OrderInfo()));
        return totalDataInfo;
    }
    /**
     * 获取设备的数据
     * 0-离线，1-在线，2-使用中
     * @return EquipmentChartVo
     */
    @GetMapping("/getEquipmentChart")
    public EquipmentChartVo getEquipmentChartVo()
    {
        EquipmentChartVo vo = new EquipmentChartVo();
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setEquipmentStatus(2L);
        vo.setEquipmentInUse(equipmentInfoService.getEquipmentTotal(equipmentInfo));
        equipmentInfo.setEquipmentStatus(1L);
        vo.setEquipmentOnline(equipmentInfoService.getEquipmentTotal(equipmentInfo));
        equipmentInfo.setEquipmentStatus(0L);
        vo.setEquipmentOffline(equipmentInfoService.getEquipmentTotal(equipmentInfo));
        return vo;
    }

    /**
     * 获取药包的数据
     * 药包状态：0-未使用，1-已使用，2-已用完
     * @return EquipmentChartVo
     */
    @GetMapping("/getDrugInfoChartVo")
    public DrugInfoChartVo getDrugInfoChartVo()
    {
        DrugInfoChartVo vo = new DrugInfoChartVo();
        DrugInfo drugInfo = new DrugInfo();
        drugInfo.setDrugStatus(2);
        vo.setUsedUpDrug(drugInfoService.getDrugTotal(drugInfo));
        drugInfo.setDrugStatus(1);
        vo.setInUseDrug(drugInfoService.getDrugTotal(drugInfo));
        drugInfo.setDrugStatus(0);
        vo.setUnusedDrug(drugInfoService.getDrugTotal(drugInfo));
        return vo;
    }

    /**
     * 获取订单的数据
     * 状态0、待确认 1、待发货 2、待收货 3、已完成 4、已关闭
     * @return EquipmentChartVo
     */
    @GetMapping("/getOrderChartVo")
    public OrderChartVo getOrderInfoChartVo()
    {
        OrderChartVo vo = new OrderChartVo();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus("0");
        vo.setWaitForConfirmation(orderInfoService.orderTotal(orderInfo));
        orderInfo.setStatus("1");
        vo.setWaitForSending(orderInfoService.orderTotal(orderInfo));
        orderInfo.setStatus("2");
        vo.setWaitForDelivery(orderInfoService.orderTotal(orderInfo));
        return vo;
    }

}
