package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体，关联数据库 body_health_news 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "body_health_news")
public class BodyHealthNews {
    private Integer id; // 主键ID，主键自增
    private String cover; //资讯封面
    private String title; //标题
    private String content; //内容
    private String summary;//摘要
    private Integer typeId; //资讯分类ID，外键，资讯分类通过后端枚举进行管理

    /*记录时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 注册时间
}
