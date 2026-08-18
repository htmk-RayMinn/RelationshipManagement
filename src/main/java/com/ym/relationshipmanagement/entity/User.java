package com.ym.relationshipmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 系统用户实体类（仅本人，表中只存放一条记录）
 **/

@Data//Lombok注解，编译时自动生成方法
@TableName("users")//映射数据库表名
public class User {

    @TableId(type = IdType.AUTO)//标记主键字段，type = IdType.AUTO 表示使用数据库自增策略
    private Long id;

    private String name;//用户本人姓名
    private String nickname;//昵称
    private LocalDate birthDate;//出生日期
    private Gender gender;//性别，使用枚举类型映射数据库 ENUM('M','F','其他')
    private String email;//电子邮箱
    private String phone;//手机号
    private String address;//住址
    private String notes;//备注（数据库类型为 TEXT，可存长文本）

    @TableField(fill = FieldFill.INSERT)//表示仅在插入时自动填充
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)//表示插入和更新时都会自动填充
    private LocalDateTime updatedAt;

    //性别枚举，与数据库 ENUM('M','F','其他') 对应
    public enum Gender {
        M, F, 其他
    }
}

