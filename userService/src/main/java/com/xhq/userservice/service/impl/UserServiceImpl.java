package com.xhq.userservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhq.common.Exception.Exception401;
import com.xhq.userservice.domain.User;
import com.xhq.userservice.mapper.UserMapper;
import com.xhq.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
* @author mytx
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-03-18 21:09:31
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public void userLogin(User user) {
        boolean success=userMapper.login(user.getUsername(), user.getPassword());
        if(!success){
            throw new Exception401("用户登录失败,密码或用户名不对");
        }
    }

    @Override
    public User getUserInfoByUsername(User user) {
        User user1=userMapper.getUserinfoByUsernameAndPassword(user.getUsername(),user.getPassword());
        return user1;
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.getUserById(userId);
    }



}




