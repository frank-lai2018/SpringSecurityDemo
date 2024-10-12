package com.frank.SpringSecurity.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomizeAuthenticationFailure implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
HashMap hashMap = new HashMap();
		
 		String localizedMessage = exception.getLocalizedMessage();

		hashMap.put("code",-1);
		hashMap.put("message", "登入失敗");
		hashMap.put("localizedMessage", localizedMessage);
			
		
		String json = JSON.toJSONString(hashMap);
		
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().println(json);
	}

}
