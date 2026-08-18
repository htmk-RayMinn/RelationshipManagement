package com.ym.relationshipmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.relationshipmanagement.entity.Group;
import com.ym.relationshipmanagement.mapper.GroupMapper;
import com.ym.relationshipmanagement.service.GroupService;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

}
