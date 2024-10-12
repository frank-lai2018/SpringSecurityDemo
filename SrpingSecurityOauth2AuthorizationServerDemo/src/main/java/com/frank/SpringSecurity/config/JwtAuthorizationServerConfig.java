package com.frank.SpringSecurity.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class JwtAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


	@Autowired
	private DataSource dataSource;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 配置使用 jwt 方式頒發令牌,同時配置 jwt 轉換器
	 *
	 * @param endpoints
	 * @throws Exception
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore())//使用JWT的令牌存儲
				.accessTokenConverter(jwtAccessTokenConverter());//使用JWT轉匯器去轉換另排
//				.authenticationManager(authenticationManager);//認證通過後告訴authenticationManager
	}

	/**
	 * 使用JWT方式產生令牌
	 *
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		//要把一些用戶信息物件放到JWT令牌裡，因為JWT包含 header payload sing ，所以需要使用轉換器來幫忙轉換
		return new JwtTokenStore(jwtAccessTokenConverter()); 
	}

	/**
	 * JWT轉換器，把認證訊息轉換成JWT，之後再ResourceServer裡還要轉換回來，使用同一個金鑰來編碼JWT中的OAuth2令牌
	 *
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		// 可以採用屬性注入方式 生產中建議加密
		converter.setSigningKey("123");
		return converter;
	}

	/**
	 * 聲明ClientDetails實現
	 *
	 * @return
	 */
	@Bean
	public ClientDetailsService clientDetails() {
		JdbcClientDetailsService jdbcClientDetailsS​​ervice = new JdbcClientDetailsService(dataSource);
		jdbcClientDetailsS​​ervice.setPasswordEncoder(passwordEncoder);
		return jdbcClientDetailsS​​ervice;
	}


	/**
	 * 使用資料庫方式存儲合法客戶端
	 *
	 * @param clients
	 * @throws Exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetails());
	}
	
	
	
}
