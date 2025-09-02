package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体，关联数据库 recipe 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "recipe")
public class Recipe {
    private Integer id; // 主键ID，主键自增
    private String name; //食谱名
    private String cover; //封面
    private Integer typeId; //食谱类别ID，外键，通过后端枚举类进行管理
    private String content; //内容
    private Integer userId;//用户ID，外键，关联的是用户表
    private Boolean isAudit; //是否已经审核

    /*记录时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 注册时间
}
