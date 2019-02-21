package com.nowcode.toutiao.service;

import com.nowcode.toutiao.dao.LoginTicketDAO;
import com.nowcode.toutiao.dao.UserDAO;
import com.nowcode.toutiao.model.LoginTicket;
import com.nowcode.toutiao.model.User;
import com.nowcode.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    //public void addUser(User user){ userDAO.addUser(user);}
    public Map<String, Object> register(String username, String password){//注册

        Map<String, Object> map = new HashMap<String, Object>();
        if(username == null||username.length()==0){
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if(password == null||password.length()==0){
            map.put("messagepassword", "密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user!=null) {
            map.put("messagename", "用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        //user.setPassword(password);
        //随机生成序列保存到salt用于加密
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> login(String username, String password){//登陆

        Map<String, Object> map = new HashMap<String, Object>();
        if(username == null||username.length()==0){
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if(password == null||password.length()==0){
            map.put("messagepassword", "密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user == null) {
            map.put("messagename", "用户名不存在");
            return map;
        }

        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        //登陆
        return map;
    }

    private String addLoginTicket(int userId){//token
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
