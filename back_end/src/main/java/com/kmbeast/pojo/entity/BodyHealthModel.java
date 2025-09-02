package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体，关联数据库 body_health_model 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "body_health_model")
public class BodyHealthModel {
    private Integer id; // 主键ID，主键自增
    private String name; //模型名
    private String detail; //模型简介
    private String icon_url; //图标
    private String unit;//单位
    private String symbol; //符号
    private String normalValue; //正常值范围，以英文逗号“,”进行分割
    private Integer userId; //用户ID
    private Boolean is_global; //是否是全局模型

    /*记录时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 注册时间
}
