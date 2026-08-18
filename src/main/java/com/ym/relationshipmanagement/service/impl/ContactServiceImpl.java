package com.ym.relationshipmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.relationshipmanagement.entity.Contact;
import com.ym.relationshipmanagement.mapper.ContactMapper;
import com.ym.relationshipmanagement.service.ContactService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements ContactService {

    @Override
    public List<Contact> searchByName(String keyword) {
        LambdaQueryWrapper<Contact> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Contact::getName, keyword);
        wrapper.orderByDesc(Contact::getCreatedAt);
        return this.list(wrapper);
    }
    //具体实现中，重写searchByName方法，传入keyword变量
    //创建一个新的LambdaQueryWrapper<>()对象，规定泛型是Contact，对象名为wrapper。（这个LambdaQueryWrapper是什么，为什么要用这个？）用 LambdaQueryWrapper，用 Java 代码拼条件
    //调用对象方法，like（表示模糊查询？），后面的看不懂了这个Contact::getName什么意思？::这个符号没见过。这是 Java 8 的方法引用语法，:: 就是"引用某个方法"的意思。
    //调用对象方法，order，后面也看不懂。desc降序，asc升序
    //返回list方法，传入wrapper。this.list是ServiceImpl提供的方法，等于select * from contacts。this.list(wrapper)就是带条件。
}

