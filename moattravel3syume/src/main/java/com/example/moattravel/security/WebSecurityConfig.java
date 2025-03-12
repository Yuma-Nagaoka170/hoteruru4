package com.example.moattravel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFileterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf
	            .ignoringRequestMatchers("/batch/daily-logins") // ここで無効化
	        )
		.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/batch/daily-logins") .permitAll()//全てのユーザーにアクセスを許可するURL
				.requestMatchers("/admin/**").hasRole("ADMIN")//管理者にのみアクセスを許可する
				.anyRequest().authenticated() //上記以外のURLはログインが必要（会員または管理者のどちらでもOK）
				)
		.formLogin((form) -> form
				.loginPage("/login")//ログインページのURL
				.loginProcessingUrl("/login")//ログインフォームの送信先URL
				.defaultSuccessUrl("/?loggedIn") //ログイン成功時のリダイレクト先URL
				.failureUrl("/login?error") //ログイン失敗時のリダイレクト先
				.permitAll()
				)
		.logout((logout) -> logout
				.logoutSuccessUrl("/?loggedOut") //ログアウト時のリダイレクト先
				.permitAll()
				);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
