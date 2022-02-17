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
	
//	@Autowired
//	DataSource dataSource;
//	
//	
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//	System.out.println("in password encoder");
//		//return NoOpPasswordEncoder.getInstance();
//		return new BCryptPasswordEncoder();
//	}
//	// Enable jdbc authentication
//	@Autowired
//	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder());
//	}
//	
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//	    web.ignoring().antMatchers("/v1/user/**").antMatchers("/v2/user/**");
//	}				
//	
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//            .antMatchers("/v1/verifyUserEmail").permitAll();
//            
//    }
	@Autowired
	  private UserDetailsService userDetailsService;

	  @Autowired
	  private PasswordEncoder passwordEncoder;

	  @Override
	  public void configure(WebSecurity web) throws Exception {
	    //web.ignoring().antMatchers(HttpMethod.POST, "/v1/user").antMatchers("/healthz");
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
