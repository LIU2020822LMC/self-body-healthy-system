package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体，关联数据库 body_health_record 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "body_health_record")
public class BodyHealthRecord {
    private Integer id; // 主键ID，主键自增
    private Integer userId; //用户ID,外键，关联的是用户表
    private Integer bodyHealthModelId; //健康模型ID,外键，关联的是健康模型表
    private Double value; //记录的值

    /*记录时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 注册时间
}
