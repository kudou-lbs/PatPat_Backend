package com.games.tap.controller;

import com.games.tap.domain.UserInfo;
import com.games.tap.domain.UserLogin;
import com.games.tap.service.LoginService;
import com.games.tap.service.UserService;
import com.games.tap.util.JwtUtil;
import com.games.tap.util.PassToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Resource
    LoginService mLoginService;
    @Resource
    UserService mUserService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) return "字段不能为空";
        UserLogin userLogin = mLoginService.getUserLoginByName(username);
        if (userLogin == null) return "账户名不存在";
        UserInfo user;
        if (password.equals(userLogin.getPassword())) {
            user = mUserService.getUserById(userLogin.getUId());
            // 先验证用户的账号密码,账号密码验证通过之后，生成Token
            return JwtUtil.createToken(user);
        }
        return "登录失败";
    }

    @PostMapping("/testToken")
    public String testToken(@RequestHeader("token") String token) {
        if (token.isEmpty()) return "请求失败";
        JwtUtil.parseToken(token);
        return "请求成功";
    }

    @Operation(summary = "通过id更改密码")
    @RequestMapping(value = "/UserLogin/update")
    public int update(Integer id,String password){
        return loginService.update(id,password);
    }

    @Operation(summary = "通过id删除账号")
    @RequestMapping(value = "/UserLogin/deleteById")
    public int deleteById(Integer id){
        return loginService.deleteById(id);
    }

    @Operation(summary = "通过id查找账号、密码")
    @RequestMapping(value = "/UserLogin/getById")
    public UserLogin getById(Integer id){
        return loginService.getById(id);
    };

}
