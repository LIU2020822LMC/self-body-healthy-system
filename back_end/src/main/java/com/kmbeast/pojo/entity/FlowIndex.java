package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体，关联数据库 flow_index 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "flow_index")
public class FlowIndex {
    private Integer id; // 主键ID，主键自增
    private Integer type; //流量类型（1：展现；2：浏览；3：点赞；4：收藏；5：分享；6：停留时长）
    private String contentModule; //内容模块
    private Integer contentId; // 内容ID，外键,关联所在内容模块下面的ID
    private Long times; //停留时长

    /*记录时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 注册时间
}
