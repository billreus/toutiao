package com.nowcode.toutiao.controller;


import com.nowcode.toutiao.model.News;
import com.nowcode.toutiao.model.ViewObject;
import com.nowcode.toutiao.service.NewsService;
import com.nowcode.toutiao.service.UserService;
import com.nowcode.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme) {
        try {
            Map<String, Object> map = userService.register(username, password);
/*
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }
*/
            if(map.isEmpty()){
                return ToutiaoUtil.getJSONString(0, "注册成功");
            }else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            //  logger.error("注册异常" + e.getMessage());
            System.out.println(e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme) {
        try {
            Map<String, Object> map = userService.register(username, password);
/*
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }
*/
            if(map.isEmpty()){
                return ToutiaoUtil.getJSONString(0, "注册成功");
            }else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            //  logger.error("注册异常" + e.getMessage());
            System.out.println(e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }
}
