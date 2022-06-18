package com.games.tap.controller;

import com.games.tap.domain.UserInfo;
import com.games.tap.service.UserService;
import com.games.tap.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "user", description = "用户接口，提供基本信息修改，背景图，登录注册，互相关注等功能")
public class UserController {

    @Resource
    UserService userService;
    @Resource
    PasswordEncoder encoder;

    @PassToken
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Operation(summary = "用户登录", description = "返回token，与用户相关的需要header携带token才能进行操作", parameters = {
            @Parameter(name = "username", description = "账户名", required = true),
            @Parameter(name = "password", description = "密码", required = true)
    })
    public Echo login(String username, String password) {
        // TODO 前后端使用密钥，报文加密传输
        if (username==null|| username.isEmpty() || password==null|| password.isEmpty())
            return Echo.define(RetCode.PARAM_IS_EMPTY);
        UserInfo saveUser = userService.getUserByUserName(username);
        if (saveUser == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (encoder.matches(password,saveUser.getPassword())) {// pwd与已加密的进行匹配
            // 先验证用户的账号密码,账号密码验证通过之后，生成Token
            Map<String, Object> map = new HashMap<>();
            String token = JwtUtil.createToken(saveUser);
            map.put("userInfo", saveUser);
            map.put("token", token);
            return Echo.success(map);
        }
        return Echo.define(RetCode.USER_LOGIN_ERROR);
    }

    @PassToken
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Operation(summary = "用户注册", description = "注册账号", parameters = {
            @Parameter(name = "username", description = "账户名", required = true),
            @Parameter(name = "password", description = "密码", required = true),
            @Parameter(name = "nickname", description = "昵称"),
            @Parameter(name = "gender", description = "性别")
    })
    public Echo register(@RequestBody UserInfo user) {
        // 合法性校验
        if(user.getUsername()==null||user.getUsername().equals(""))
            return Echo.fail("账户名不能为空");
        if(user.getPassword()==null||user.getPassword().equals(""))
            return Echo.fail("密码不能为空");
        if(!ToolUtil.checkPassword(user.getPassword()))
            return Echo.fail("密码格式错误");
        if(userService.getUserByUserName(user.getUsername())!=null)
            return Echo.define(RetCode.USER_HAS_EXISTED);
        if(user.getNickname()==null||user.getNickname().equals("")){
            String name="用户"+Long.toHexString(SnowFlake.nextId()).replace("0","");
            user.setNickname(name);
        }
        //  初始化设置
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getGender()==null)user.setGender(0);
        user.setRegisterTime(DateUtil.getCurrentTime());
        if(userService.insertUser(user)>0){
            log.info(user.getUsername()+"注册成功");
            return Echo.success();
        }
        else return Echo.fail("注册失败");
    }


    @PostMapping("/testToken")
    public String testToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token.isEmpty()) return "请求失败";
        JwtUtil.parseToken(token);
        return "请求成功";
    }

    @Operation(summary = "更改用户信息")
    @RequestMapping(value = "/user/update")
    public Echo updateUser(UserInfo user){
        if(userService.updateUser(user)!=0)return Echo.success();
        else return Echo.fail();
    }

    @Operation(summary = "通过id删除账号")
    @RequestMapping(value = "/user/deleteById",method = RequestMethod.DELETE)
    public Echo deleteById(Long id){
        if(userService.deleteUserById(id)!=0)return Echo.success();
        else return Echo.fail();
    }

    @Operation(summary = "通过id查找用户")
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public Echo getUserById(Long id){
        UserInfo user=userService.getUserById(id);
        if(user==null)return Echo.fail();
        return Echo.success(user);
    }
}
