package com.joolun.mall.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@EqualsAndHashCode
public class InputQueryVo {

    private String number;

    private LocalDateTime createTime;
}
