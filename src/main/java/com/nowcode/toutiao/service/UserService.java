package com.nowcode.toutiao.service;

import com.nowcode.toutiao.dao.UserDAO;
import com.nowcode.toutiao.model.User;
import com.nowcode.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    //public void addUser(User user){ userDAO.addUser(user);}
    public Map<String, Object> register(String username, String password){

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
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登陆
        return map;
    }

    public Map<String, Object> login(String username, String password){

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
        user = new User();
        user.setName(username);
        //user.setPassword(password);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登陆
        return map;
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
