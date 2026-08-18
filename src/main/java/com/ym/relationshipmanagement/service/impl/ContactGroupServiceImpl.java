package com.ym.relationshipmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.relationshipmanagement.entity.ContactGroup;
import com.ym.relationshipmanagement.mapper.ContactGroupMapper;
import com.ym.relationshipmanagement.service.ContactGroupService;
import com.ym.relationshipmanagement.service.ContactService;
import org.springframework.stereotype.Service;

@Service
public class ContactGroupServiceImpl extends ServiceImpl<ContactGroupMapper, ContactGroup> implements ContactGroupService {
}
