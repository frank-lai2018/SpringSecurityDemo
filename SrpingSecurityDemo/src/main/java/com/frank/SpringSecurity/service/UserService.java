package com.frank.SpringSecurity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.frank.SpringSecurity.entity.User;

public interface UserService extends IService<User> {

	void saveUserDetails(User user);

}
