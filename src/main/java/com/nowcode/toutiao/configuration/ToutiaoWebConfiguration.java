package com.nowcode.toutiao.configuration;

import com.nowcode.toutiao.interceptor.LoginRequiredInterceptor;
import com.nowcode.toutiao.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ToutiaoWebConfiguration implements WebMvcConfigurer {//注册拦截器

    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");//只拦截setting页面
    }
}
