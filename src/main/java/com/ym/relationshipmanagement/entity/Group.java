package com.ym.relationshipmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 联系人分组实体类
 * 用户自定义分组，比如：家人、同事、同学、讨厌的上司 等
 *
 * 注意：groups 是 SQL 保留关键字，所以 @TableName 中必须用反引号 `` `groups` `` 包裹
 */

@Data//Lombok注解，编译时自动生成方法
@TableName("`groups`")// 反引号包裹，避免 SQL 语法错误
public class Group {

    @TableId(type = IdType.AUTO)//标记主键字段，自增策略
    private Long id;

    private String name;//组名，唯一不可重复，数据库设置了 UNIQUE 约束
    private String description;//分组说明描述

    @TableField(fill = FieldFill.INSERT)//创建时间，插入时自动生成
    private LocalDateTime createdAt;
}
