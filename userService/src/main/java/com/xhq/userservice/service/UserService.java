package com.xhq.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhq.userservice.domain.User;


/**
* @author mytx
* @description 针对表【user】的数据库操作Service
* @createDate 2024-03-18 21:09:31
*/
public interface UserService extends IService<User> {
        void userLogin(User user);

        User getUserInfoByUsername(User user);

        User getUserById(Integer userId);


}
