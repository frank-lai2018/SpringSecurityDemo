package com.frank.SpringSecurity.config;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		 //建立結果對象
		 HashMap result = new HashMap();
		 result.put("code", -1);
		 result.put("message", "沒有權限");

		 //轉換成json字串
		 String json = JSON.toJSONString(result);

		 //回傳回應
		 response.setContentType("application/json;charset=UTF-8");
		 response.getWriter().println(json);

	}

}
