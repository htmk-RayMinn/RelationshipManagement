package com.ym.relationshipmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 联系人实体类
 * 存储联系人的基本信息，联系方式拆分到 contacts_account 表
**/

@Data//Lombok注解，编译时自动生成方法
@TableName("contacts")//映射数据库表名
public class Contact {

    @TableId(type = IdType.AUTO)//标记主键字段，自增策略
    private Long id;

    private String name;//联系人姓名
    private LocalDate birthDate;//出生日期
    private Gender gender;//性别
    private String address;//地址
    private String notes;//备注

    @TableField(fill = FieldFill.INSERT)//表字段，插入时自动生成
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)//表字段，插入和更新时自动生成
    private LocalDateTime updatedAt;

    //性别枚举
    public enum Gender {
        M, F, 其他
    }
}
