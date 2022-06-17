package com.games.tap.controller;

import com.games.tap.domain.UserInfo;
import com.games.tap.service.LoginService;
import com.games.tap.service.UserService;
import com.games.tap.util.JwtUtil;
import com.games.tap.util.PassToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "user",description = "用户接口，提供基本信息修改，背景图，登录注册，互相关注等功能")
public class UserController {
    @Autowired
    private LoginService loginService;

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

    @Operation(summary = "通过id更改密码")
    @RequestMapping(value = "UserLogin/update",method = RequestMethod.POST)
    public int update(Integer id){
        return loginService.update(id);
    }

    @Operation(summary = "通过id删除账号")
    @RequestMapping(value = "UserLogin/deleteById",method = RequestMethod.DELETE)
    public void deleteById(Integer id){
        loginService.deleteById(id);
    };

}
