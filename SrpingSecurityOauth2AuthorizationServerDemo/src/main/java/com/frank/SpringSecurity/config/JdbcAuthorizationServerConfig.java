package com.frank.SpringSecurity.config;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

//@Configuration
//@EnableAuthorizationServer
public class JdbcAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public ClientDetailsService clientDetails() {
		JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
		jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
		return jdbcClientDetailsService;
	}
	
	/**
	 * 配置合法客戶端信息
	 * */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetails());
	}

	//配置令牌存儲 例如:Redis、JDBC、memory、jwt
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}
	
	/**
	 * 配置授權令牌信息
	 * */
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
//		 endpoints.authenticationManager(authenticationManager);//認證管理器，密碼模式用
		 endpoints.tokenStore(tokenStore());//設定令牌儲存為資料庫存儲，配置怎麼頒發令牌，存在哪
		 
		 // 設定TokenServices參數
		 DefaultTokenServices tokenServices = new DefaultTokenServices();//修改預設令牌產生服務
		 tokenServices.setTokenStore(endpoints.getTokenStore());//基於資料庫令牌生成
		 tokenServices.setSupportRefreshToken(true);//是否支援刷新令牌
		 tokenServices.setReuseRefreshToken(true);//是否重複使用刷新令牌（直到過期）
		 tokenServices.setClientDetailsService(endpoints.getClientDetailsService());//設定客戶端訊息
		 tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());//用來控制令牌儲存增強策略，加密用
	 
		 //存取令牌的預設有效期（以秒為單位）。過期的令牌為零或負數。
		 tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); // 30天
		 //刷新令牌的有效性（以秒為單位）。如果小於或等於零，則令牌將不會過期
		 tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(3)); //3天
		 endpoints.tokenServices(tokenServices);//使用設定令牌服務
	}

	
}
