package com.joolun.mall.service;

import com.joolun.mall.vo.NoticeInfoVo;

import java.util.List;

public interface INoticeInfoService {
    /**
     * 获取所有的通知
     * @return 通知、公告列表
     */
    List<NoticeInfoVo> findNoticeInfoAll();
}
