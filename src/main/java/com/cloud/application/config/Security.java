package com.cloud.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

//@Component
@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter{
	
	@Autowired
	  private UserDetailsService userDetailsService;

	  @Autowired
	  private PasswordEncoder passwordEncoder;

	  @Override
	  public void configure(WebSecurity web) throws Exception {
	  }

	  @Override
	  protected void configure(HttpSecurity httpSecurity) throws Exception {
	    httpSecurity
	            .csrf()
	            .disable()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and()
	            .authorizeRequests()
	            .antMatchers(HttpMethod.POST, "/v1/user").permitAll()
	            .antMatchers(HttpMethod.GET, "/v1/verifyUserEmail").permitAll()
	            .antMatchers("/healthz").permitAll()
	            .anyRequest()
	            .authenticated()
	            .and()
	            .httpBasic();
	  }

	  @Override
	  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.authenticationProvider(authProvider());
	  }

	  @Bean
	  public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder);
	    return authProvider;
	  }



}
