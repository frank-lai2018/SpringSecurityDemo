package com.frank.SpringSecurity.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.frank.SpringSecurity.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User>{

}
