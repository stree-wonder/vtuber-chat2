package com.xhq.userservice.controller;

import com.xhq.common.domain.Result;
import com.xhq.userservice.config.JwtProperties;
import com.xhq.userservice.domain.User;
import com.xhq.userservice.domain.UserLoginVO;
import com.xhq.userservice.service.UserService;
import com.xhq.userservice.util.JwtTool;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController()
@RequestMapping("/api/user")
@Api(tags = "用户有关的的controller")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    /**
     * 用户登录并且进行JWT校验
     * @param user
     * @return
     */
    @PostMapping("/loginJwt")
    @ApiOperation("用户登录")
    public Result<UserLoginVO> userLogin(@RequestBody User user){
        System.out.println(user.getUsername()+user.getPassword());

        userService.userLogin(user);

        user=userService.getUserInfoByUsername(user);


        // 5.生成TOKEN
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());

        UserLoginVO userLoginVO=UserLoginVO.builder()
                .username(user.getUsername())
                .token(token)
                .sex(user.getSex())
                .id(user.getId()).build();
        return Result.success(userLoginVO);
    }

}
