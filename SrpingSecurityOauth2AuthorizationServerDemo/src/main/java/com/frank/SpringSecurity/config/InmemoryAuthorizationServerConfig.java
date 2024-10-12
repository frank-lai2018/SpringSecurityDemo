package com.frank.SpringSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

//@Configuration
//@EnableAuthorizationServer // 啟用授權伺服器
public class InmemoryAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private  PasswordEncoder passwordEncoder;
	
	@Autowired
	private  UserDetailsService userDetailsService;

	@Autowired
	private  AuthenticationManager authenticationManager;
	
	
	
	/**
	 * 注入進來 認證管理器
	 */
//	private final AuthenticationManager authenticationManager;

//	public AuthorizationServerConfig(PasswordEncoder passwordEncoder, UserDetailsS​​ervice userDetailsS​​ervice,
//			AuthenticationManager authenticationManager) {
//		this.passwordEncoder = passwordEncoder;
//		this.userDetailsS​​ervice = userDetailsS​​ervice;
//		this.authenticationManager = authenticationManager;
//	}

	/**
	 * 對授權客戶端的配置
	 *
	 * 用來設定授權伺服器可以為哪些客戶端授權 // clientId secret redirectURI 使用那種授權模式
	 *
	 * { "access_token": "becac041-faf4-4b2d-8c58-8b10a38f290f", "token_type":
	 * "bearer", "refresh_token": "4752515d-f528-4932-a255-d20cdb5e95c4",
	 * "expires_in": 43199, "scope": "read:user" }
	 */
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() // 表示基於內存
				.withClient("frankId") // clientId設定客戶端編號
				.secret(passwordEncoder.encode("secret")) // 設定客戶端密鑰 ,這裡官方規定secret必現加密 可以使用 passwordEncode
				.redirectUris("http://www.google.com") // 重定向URI
				// authorization_code 授權碼 refresh_token 刷新令牌 implicit 簡化模式 password 密碼模式
				// client_credentials 用戶端模式
				// 暫不支援簡化模式, 因為需要解析一段腳本來取得令牌
				.authorizedGrantTypes("authorization_code","refresh_token","password","client_credentials") // 表示使用那種授權模式 可以設定支援多種，授權碼模式
				.scopes("read:user");// 令牌允許取得資源權限
	}

	// 授權碼這種模式：
	// 1.請求使用者是否授權 /oauth/authorize 參數: client_id response_type redirect_uri (該階段還不用客戶端密鑰)
	// 完整路徑 :
	// http://localhost:8080/oauth/authorize?client_id=frankId&response_type=code&redirect_uri=http://www.google.com
	// 2.授權之後根據取得的授權碼取得令牌 /oauth/token 參數 ： client_id secret redirect_uri code
	// 完整路徑:curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d
	// 'grant_type=authorization_code&code=2qewq&redirect_uri=http://www.baidu.com'
	// "http:// client:secret@localhost:8080/oauth/token"

	// 3.支援令牌刷新 /oauth/token 參數 id secret 授權類型 :refresh_token 刷新的令牌:refresh_token
	// 完整路徑:curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d
	// 'grant_type=refresh_token&refresh_token=2qewq&redirect_uri=http://www.baidu.com'
	// "http:// client:secret@localhost:8080/oauth/token"

	/**
	 * 開啟刷新令牌功能 實現刷新令牌必現指定userDetailsS​​ervice ，有了它，刷新令牌時就不需要在獲取授權碼了 刷新令牌和
	 * 獲取令牌的路徑是一樣的 參數多了
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.userDetailsService(userDetailsService); // 開啟刷新令牌必現指定
		endpoints.authenticationManager(authenticationManager); // 密碼模式需要注入 authenticationManager
	}


}