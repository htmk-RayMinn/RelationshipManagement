package com.ym.relationshipmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ym.relationshipmanagement.entity.ContactAccount;

import java.util.List;

public interface ContactAccountService extends IService<ContactAccount> {
    List<ContactAccount> searchByKeyword(String keyword);
}
