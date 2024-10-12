package com.frank.SpringSecurity.service.impl;


import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frank.SpringSecurity.config.DBUserDetailsManager;
import com.frank.SpringSecurity.entity.User;
import com.frank.SpringSecurity.mapper.UserMapper;
import com.frank.SpringSecurity.service.UserService;

import jakarta.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Resource
	private DBUserDetailsManager dbUserDetailsManager;
	
	@Override
	public void saveUserDetails(User user) {
		Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
//		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
		
		
		 UserDetails userDetails = org.springframework.security.core.userdetails.User
				 .withDefaultPasswordEncoder()
				 .username(user.getUsername()) //自訂使用者名稱
				 .password(user.getPassword()) //自訂密碼
				 .build();
		
		dbUserDetailsManager.createUser(userDetails);
		
	}


}
