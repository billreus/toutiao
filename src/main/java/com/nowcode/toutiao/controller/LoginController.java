package com.nowcode.toutiao.controller;


import com.nowcode.toutiao.async.EventModel;
import com.nowcode.toutiao.async.EventProducer;
import com.nowcode.toutiao.async.EventType;
import com.nowcode.toutiao.service.UserService;
import com.nowcode.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
//@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    EventProducer eventProducer;

    //注册
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})//注册
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password);

            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");//全站有效
                if (rememberme > 0) {//加长有效时间
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
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

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})//登陆
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);

            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);//添加以后自动刷新页面才会返回登陆后的界面，没有相当于刷新界面
                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
                        .setExt("username", username).setExt("email", "text.com"));//异步
                return ToutiaoUtil.getJSONString(0, "登陆成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            //  logger.error("注册异常" + e.getMessage());
            //System.out.println(e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登陆异常");
        }

    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})//登陆
    //@ResponseBody
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";//返回首页
    }
}
