package com.java.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.java.interceptor.LoginRequestInterceptor;
import com.java.interceptor.PassportInterceptor;

@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter{
	
	@Autowired
	private PassportInterceptor passportInterceptor;
	
	@Autowired
	private LoginRequestInterceptor loginRequestInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(passportInterceptor);
		registry.addInterceptor(loginRequestInterceptor).addPathPatterns("/user/*");
		super.addInterceptors(registry);
	}
	
	

}
