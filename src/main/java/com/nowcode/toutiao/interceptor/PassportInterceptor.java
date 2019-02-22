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

@Component//拦截器 ctrl+n重载handlerInterceptor
//用于判断用户是否登录过的，
public class PassportInterceptor implements HandlerInterceptor {
//
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override//找用户,先于control层
    //preHandle拦截controller之前的
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String ticket = null;
        //在cookies中找ticket
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket != null){//用户登陆过，判断时间是否有效
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if(loginTicket == null || loginTicket.getExpired().before(new Date())
                    || loginTicket.getStatus() != 0){//时间过期
                return true;
            }
            //记录登陆过用户的id
            User user = userDAO.selectById(loginTicket.getUserId());
            //存放登录过的用户
            hostHolder.setUsers(user);

        }
        return true;
    }

    //controller之后，渲染之前。把用户加入
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && hostHolder.getUser() !=null){
            modelAndView.addObject("user", hostHolder.getUser());//前端直接访问user
        }
    }
    //所有活动结束清除该用户
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
