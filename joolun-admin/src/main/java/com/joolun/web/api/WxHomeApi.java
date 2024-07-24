package com.joolun.web.api;

import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.service.GoodsCategoryService;
import com.joolun.mall.service.IDrugInfoService;
import com.joolun.mall.service.IEquipmentInfoService;
import com.joolun.mall.service.INoticeInfoService;
import com.joolun.mall.service.impl.EquipmentInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping({"/weixin/api/ma/home"})
@Api(
        value = "home",
        tags = {"小程序首页API"}
)
public class WxHomeApi {
    private static final Logger log = LoggerFactory.getLogger(WxHomeApi.class);

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @Autowired
    private IEquipmentInfoService equipmentService;

    @Autowired
    private IDrugInfoService drugInfoService;

    @Autowired
    private INoticeInfoService iNoticeInfoService;

    @ApiOperation("获取 banner")
    @GetMapping({"/banner"})
    public AjaxResult goodsBanner() {
        return AjaxResult.success(goodsCategoryService.getBannerList());
    }

    @ApiOperation("获取公告滚动条")
    @GetMapping({"/getNoticeBoard"})
    public AjaxResult getNoticeBoard() {
        return AjaxResult.success(iNoticeInfoService.findNoticeInfoAll());
    }


    @ApiOperation("获取首页商品列表")
    @GetMapping({"/goods"})
    public AjaxResult goodsList() {
        return AjaxResult.success(goodsCategoryService.getGoodsSpuList());
    }

    @ApiOperation("获取用户旗下的设备")
    @GetMapping({"/equipmentChart/{userId}"})
    public AjaxResult equipmentChartByUserId(@PathVariable("userId") String userId) {
        return AjaxResult.success(this.equipmentService.getEquipmentChartData(userId));
    }

    @ApiOperation("获取用户旗下的药包")
    @GetMapping({"/drugInfoChart/{userId}"})
    public AjaxResult drugInfoChartByUserId(@PathVariable("userId") String userId) {
        return AjaxResult.success(this.drugInfoService.getDrugInfoChartData(userId));
    }

}

