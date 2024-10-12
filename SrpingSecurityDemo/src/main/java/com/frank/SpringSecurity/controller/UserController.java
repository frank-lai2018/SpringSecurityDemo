package com.frank.SpringSecurity.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frank.SpringSecurity.entity.User;
import com.frank.SpringSecurity.service.UserService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    public UserService userService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public List<User> getList(){
        return userService.list();
    }
    
    @PostMapping("/add")
    public void addUser(@RequestBody User user){
    	userService.saveUserDetails(user);
    }
}
