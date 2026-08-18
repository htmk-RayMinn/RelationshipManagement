package com.ym.relationshipmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.relationshipmanagement.entity.Contact;
import com.ym.relationshipmanagement.entity.ContactGroup;
import com.ym.relationshipmanagement.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ContactService extends IService<Contact> {

    List<Contact> searchByName(String keyword);
    //使用searchByName方法，传入keyword变量
}
