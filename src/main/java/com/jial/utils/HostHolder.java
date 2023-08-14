package com.jial.utils;

import com.jial.pojo.User;
import org.springframework.stereotype.Component;


/**
 * 用于替换session
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
