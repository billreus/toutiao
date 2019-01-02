package com.nowcode.toutiao.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {//存储该次访问的用户
    private  static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }
    public void setUsers(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
