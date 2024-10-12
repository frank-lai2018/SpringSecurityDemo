package com.frank.SpringSecurity.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

//@Configuration
//@EnableResourceServer//開啟資源服務器
public class JdbcResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	@Autowired
    private DataSource dataSource;
	
	
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }
    
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
