package com.example.express.config;

import com.example.express.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TokenInterceptor())
      .addPathPatterns("/**") // 拦截所有请求
      .excludePathPatterns("/error")
      .excludePathPatterns("/account/**")
      .excludePathPatterns("/fileUpdataApi/upload","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    // 排除 /account/** 以下的请求
  }
}
