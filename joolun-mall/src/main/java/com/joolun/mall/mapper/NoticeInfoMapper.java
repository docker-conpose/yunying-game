package com.joolun.mall.mapper;

import com.joolun.mall.vo.NoticeInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeInfoMapper {

    @Select("select  dict_label noticeTitle,notice_title noticeDetails from sys_notice INNER JOIN " +
            "sys_dict_data on dict_type='sys_notice_type' and sys_dict_data.dict_value=sys_notice.notice_type")
    List<NoticeInfoVo> findNoticeInfoList();
}
