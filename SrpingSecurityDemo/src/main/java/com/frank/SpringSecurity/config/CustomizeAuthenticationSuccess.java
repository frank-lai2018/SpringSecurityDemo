package com.frank.SpringSecurity.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomizeAuthenticationSuccess implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		HashMap hashMap = new HashMap();
		
		//獲取用戶身分信息
		Object principal = authentication.getPrincipal();
		//獲取用戶憑證信息
		Object credentials = authentication.getCredentials();
		//獲取用戶權限信息
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		hashMap.put("code", 0);
		hashMap.put("message", "登入成功");
		hashMap.put("data", authentication.getPrincipal());
		hashMap.put("Credentials", authentication.getCredentials());
		hashMap.put("authorities", authorities);
			
		
		String json = JSON.toJSONString(hashMap);
		
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().println(json);
	}

}
