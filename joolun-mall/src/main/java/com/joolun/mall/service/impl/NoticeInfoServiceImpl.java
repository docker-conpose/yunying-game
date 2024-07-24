package com.joolun.mall.service.impl;

import com.joolun.mall.mapper.NoticeInfoMapper;
import com.joolun.mall.service.INoticeInfoService;
import com.joolun.mall.vo.NoticeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeInfoServiceImpl implements INoticeInfoService {

    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    @Override
    public List<NoticeInfoVo> findNoticeInfoAll() {

        return noticeInfoMapper.findNoticeInfoList();
    }
}
