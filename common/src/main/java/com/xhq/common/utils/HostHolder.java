package com.xhq.common.utils;

import org.springframework.stereotype.Component;


/**
 *持有用户信息，用于代替session对象
 */

public class HostHolder {

    //ThreadLocal本质是以线程为key存储元素
    private static final ThreadLocal<Long> users = new ThreadLocal<>();

    public static void setUser(Long userId){
        users.set(userId);
    }

    public static Long getUser(){
        return users.get();
    }

    public static void clear(){
        users.remove();
    }
}
