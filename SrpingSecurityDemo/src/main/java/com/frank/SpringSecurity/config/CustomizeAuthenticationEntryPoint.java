package com.frank.SpringSecurity.config;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		 //取得錯誤訊息
		 String localizedMessage = authException.getLocalizedMessage();

		 //建立結果對象
		 HashMap result = new HashMap();
		 result.put("code", -1);
		 result.put("message", "需要登入");
		 result.put("localizedMessage", localizedMessage);

		 //轉換成json字串
		 String json = JSON.toJSONString(result);

		 //回傳回應
		 response.setContentType("application/json;charset=UTF-8");
		 response.getWriter().println(json);

	}

}
