package com.ym.relationshipmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ym.relationshipmanagement.entity.Contact;
import org.apache.ibatis.annotations.Mapper;

import java.util.stream.BaseStream;

@Mapper
public interface ContactMapper extends BaseMapper<Contact> {
}
