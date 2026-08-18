package com.ym.relationshipmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 联系人与分组的关联实体类（多对多中间表）
 *
 * 这张表只有两个外键，它们一起组成联合主键：
 *   一个联系人可以被分到多个分组
 *   一个分组可以包含多个联系人
 *
 * 注意：
 * - 没有自增主键，用 contactId + groupId 联合主键保证不重复
 * - 没有创建时间/更新时间字段
 * - 不需要 @TableId 注解
 */

@Data//Lombok注解，编译时自动生成方法
@TableName("contact_group")//映射数据库表名
public class ContactGroup {

    private Long contactId;//联系人ID，对应 contacts 表的主键
    private Long groupId;//分组ID，对应 groups 表的主键
}
