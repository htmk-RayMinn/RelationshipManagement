package com.ym.relationshipmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.relationshipmanagement.entity.ContactAccount;
import com.ym.relationshipmanagement.mapper.ContactAccountMapper;
import com.ym.relationshipmanagement.service.ContactAccountService;
import com.ym.relationshipmanagement.service.ContactService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactAccountServiceImpl extends ServiceImpl<ContactAccountMapper, ContactAccount> implements ContactAccountService {
    @Override
    public List<ContactAccount> searchByKeyword(String keyword) {
        LambdaQueryWrapper<ContactAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(ContactAccount::getPlatformName, keyword)
                .or()
                .like(ContactAccount::getAccount, keyword));
        wrapper.orderByDesc(ContactAccount::getCreatedAt);
        return this.list(wrapper);
    }
}
