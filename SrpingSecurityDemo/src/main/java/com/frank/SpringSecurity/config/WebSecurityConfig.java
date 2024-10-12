package com.frank.SpringSecurity.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;

@Configuration
@EnableWebSecurity // 開啟Security的自訂義配置(在SpringBoot項目中可以省略此配置)
@EnableMethodSecurity//開啟方法授權
public class WebSecurityConfig {

	/**
	 * 使用memory紀錄的帳密登入
	 */
	// @Bean
//	public UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(
//				User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
//		manager.createUser(
//				User.withDefaultPasswordEncoder().username("user1").password("password1").roles("USER").build());
//		manager.createUser(
//				User.withDefaultPasswordEncoder().username("user2").password("password2").roles("USER").build());
//		return manager;
//	}

	/**
	 * 使用DB資料登入，可以不用寫這個，直接在DBUserDetailsManager類上加上註解即可
	 */
	/*
	 * @Bean public UserDetailsService userDetailsService() { DBUserDetailsManager
	 * manager = new DBUserDetailsManager(); return manager; }
	 */

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// authorizeRequests()：開啟授權保護
		// anyRequest()：對所有請求開啟授權保護
		// authenticated()：已認證請求會自動被授權

		// 開啟授權配置
		
		//使用者-角色-資源
		http.authorizeHttpRequests((authorize) ->
			authorize
				// 具有USER_LIST權限的使用者可以存取/user/list
//				.requestMatchers("/user/list").hasAuthority("USER_LIST")
//				// 具有USER_ADD權限的使用者可以存取/user/add
//				.requestMatchers("/user/add").hasAuthority("USER_ADD")
				// 對所有請求開啟授權保護
				.anyRequest()
				// 已認證的請求會被自動授權
				.authenticated()

		);
		
		//使用者-角色-權限-資源
//		http.authorizeHttpRequests(
//			 authorize -> authorize
//			 //具有管理員角色的使用者可以存取/user/**
//			 .requestMatchers("/user/**").hasRole("ADMIN")
//			 //對所有請求開啟授權保護
//			 .anyRequest()
//			 //已認證的請求會被自動授權
//			 .authenticated()
//				);

		http.formLogin(form -> {
			form.loginPage("/login").permitAll().successHandler(new CustomizeAuthenticationSuccess())
					.failureHandler(new CustomizeAuthenticationFailure());
		});// 使用自定義的login頁面，參數是controller的GET連結
		// .httpBasic(Customizer.withDefaults())//表單授權方式
//			.formLogin(Customizer.withDefaults());//基本授權方式(使用瀏覽器的輸入框)

		http.csrf(csrf -> csrf.disable());

		// 註銷成功時的處理
		http.logout(logout -> logout.logoutSuccessHandler(new CustomizeLogoutSuccessHandler()));

		// 請求未認證的接口
		http.exceptionHandling(exception -> {
			//請求未認證的接口
			exception.authenticationEntryPoint(new CustomizeAuthenticationEntryPoint());
			
			//請求未授權的接口，也可以跟其他接口一樣使用類去實現
//			exception.accessDeniedHandler(new CustomizeAccessDeniedHandler());
			 exception.accessDeniedHandler((request, response, e)->{ //請求未授權的接口

				 //建立結果對象
				 HashMap result = new HashMap();
				 result.put("code", -1);
				 result.put("message", "沒有權限");

				 //轉換成json字串
				 String json = JSON.toJSONString(result);

				 //回傳回應
				 response.setContentType("application/json;charset=UTF-8");
				 response.getWriter().println(json);
				 });
		});

		// 會話管理
		http.sessionManagement(session -> {
			session.maximumSessions(1).expiredSessionStrategy(new CustomizeSessionInformationExpiredStrategy());
		});

		return http.build();
	}

	// 配置跨域請求
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("https://example.com"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("PUT"));
		configuration.setExposedHeaders(Arrays.asList("custom"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails userDetails = User.withDefaultPasswordEncoder()
//			.username("user")
//			.password("password")
//			.roles("USER")
//			.build();
//
//		return new InMemoryUserDetailsManager(userDetails);
//	}

}
