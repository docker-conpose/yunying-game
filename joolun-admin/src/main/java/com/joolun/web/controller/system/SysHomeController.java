package com.joolun.web.controller.system;

import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.vo.DrugInfoChartVo;
import com.joolun.mall.vo.EquipmentChartVo;
import com.joolun.mall.vo.OrderChartVo;
import com.joolun.mall.vo.TotalDataInfo;
import com.joolun.weixin.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/home")
public class SysHomeController {


    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private OrderInfoService orderInfoService;


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
