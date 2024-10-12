package com.frank.SpringSecurity.config;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

public class CustomizeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		 //建立結果對象
		 HashMap result = new HashMap();
		 result.put("code", -1);
		 result.put("message", "該帳號已從其他裝置登入");

		 //轉換成json字串
		 String json = JSON.toJSONString(result);

		 HttpServletResponse response = event.getResponse();
		 //回傳回應
		 response.setContentType("application/json;charset=UTF-8");
		 response.getWriter().println(json);

	}

}
