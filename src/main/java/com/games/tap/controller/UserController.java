package com.games.tap.controller;

import com.games.tap.domain.UserInfo;
import com.games.tap.util.JwtUtil;
import com.games.tap.util.PassToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@RestController
@Tag(name = "user",description = "用户接口，提供基本信息修改，背景图，登录注册，互相关注等功能")
public class UserController {
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestBody UserInfo user){
        // 先验证用户的账号密码,账号密码验证通过之后，生成Token
        log.warn(user.toString());
        return JwtUtil.createToken(user);
    }

    @PostMapping("/testToken")
    public String testToken(HttpServletRequest request){
        String token = request.getHeader("token");
        JwtUtil.parseToken(token);
        return "请求成功";
    }
}
