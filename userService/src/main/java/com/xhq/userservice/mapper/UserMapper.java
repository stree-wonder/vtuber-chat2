package com.xhq.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhq.userservice.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
* @author mytx
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-03-18 21:09:31
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select count(*) from user where username=#{username} and password=#{password}")
    boolean login(@Param("username") String username, @Param("password") String password);

    @Select("select * from user  where username=#{username} and password=#{password}")
    User getUserinfoByUsernameAndPassword(String username, String password);


    @Select("select * from user where id=#{userId}")
    User getUserById(Integer userId);



}




