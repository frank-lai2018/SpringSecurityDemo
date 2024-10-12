package com.frank.SpringSecurity.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frank.SpringSecurity.entity.User;
import com.frank.SpringSecurity.mapper.UserMapper;

import jakarta.annotation.Resource;


@Component//有此註解可以不用再寫Config去創建Bean蓋掉
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
    
    @Resource
    private UserMapper userMapper;

    
    /**
     * 從資料庫中獲取用戶訊息
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            //使用者-角色-資源
//        	Collection<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new GrantedAuthority() {
//				
//				@Override
//				public String getAuthority() {
//					return "USER_LIST";
//				}
//			});
//            authorities.add(() -> "USER_ADD");
//            return new org.springframework.security.core.userdetails.User(
//                    user.getUsername(),
//                    user.getPassword(),
//                    user.getEnabled(),
//                    true, //使用者帳號是否過期
//                    true, //使用者憑證是否過期
//                    true, //使用者是否未被鎖定
//                    authorities); //權限列表
        	//使用者-角色-權限-資源
        	return org.springframework.security.core.userdetails.User
        			.withUsername(user.getUsername())
        			.password(user.getPassword())
        			.roles("ADMIN")
        			.build();
        }
        	
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails userDetails) {
    	User user = new User();
    	user.setEnabled(true);
    	user.setUsername(userDetails.getUsername());
    	user.setPassword(userDetails.getPassword());
    	userMapper.insert(user);
    	
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}