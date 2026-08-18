package com.ym.relationshipmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 联系人社交账号实体类
 * 一个联系人可以有多个账号（手机号、QQ号、微信号、小红书等）
 * 平台名由用户自行填写，灵活性强
 */

@Data//Lombok注解，编译时自动生成方法
@TableName("contacts_account")//映射数据库表名
public class ContactAccount {

    @TableId(type = IdType.AUTO)//标记主键字段，自增策略
    private Long id;

    private Long contactId;//联系人id，对用contacts表的主键，通过外键连接，数据库设置了 ON DELETE CASCADE：删除联系人时，其所有账号自动删除
    private String platformName;//平台名称
    private String account;//账号
    private Integer isPrimary;//是否为主要联系方式
    private String notes;//账号备注说明

    @TableField(fill = FieldFill.INSERT)//创建时间，插入时自动填充
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)//更新时间，插入和更新时自动填充
    private LocalDateTime updatedAt;
}
