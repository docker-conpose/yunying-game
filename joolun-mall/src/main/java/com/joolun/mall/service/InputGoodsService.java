package com.joolun.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.joolun.mall.dto.InputQueryDTO;
import com.joolun.mall.vo.InputHomeVo;
import com.joolun.mall.vo.InputQueryVo;

import java.util.List;

public interface InputGoodsService {
    IPage<InputQueryVo> page(InputQueryDTO inputQueryDTO);

    InputHomeVo home();
}
