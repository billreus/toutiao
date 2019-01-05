package com.nowcode.toutiao.interceptor;

import com.nowcode.toutiao.dao.LoginTicketDAO;
import com.nowcode.toutiao.dao.UserDAO;
import com.nowcode.toutiao.model.HostHolder;
import com.nowcode.toutiao.model.LoginTicket;
import com.nowcode.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override//登陆setting页面如果没有登陆用户返回主页
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(hostHolder.getUser() == null){
            response.sendRedirect("/?pop=1");
            return false;
        }
        return true;
    }

    //渲染之前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

