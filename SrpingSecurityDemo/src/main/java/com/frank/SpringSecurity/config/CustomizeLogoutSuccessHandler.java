package com.frank.SpringSecurity.config;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		 //建立結果對象
		 HashMap result = new HashMap();
		 result.put("code", 0);
		 result.put("message", "註銷成功");

		 //轉換成json字串
		 String json = JSON.toJSONString(result);

		 //回傳回應
		 response.setContentType("application/json;charset=UTF-8");
		 response.getWriter().println(json);
		 }


}
