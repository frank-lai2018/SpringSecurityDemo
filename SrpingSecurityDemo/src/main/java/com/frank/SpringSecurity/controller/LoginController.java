package com.frank.SpringSecurity.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
	
    @GetMapping("/login")
    public String getList(){
        return "login";
    }

}
