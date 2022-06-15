package com.games.tap.controller;

import com.games.tap.domain.UserInfo;
import com.games.tap.service.UserService;
import com.games.tap.util.Echo;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "数据库操作测试")
@RestController
public class DbTestController {
    @Autowired
    private UserService userService;

    @Operation(summary = "主页",description = "null")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String hello(){
        return "Hello";
    }

    @Operation(summary = "获取所有用户信息")
    @RequestMapping(value = "/UserInfo/getAll",method = RequestMethod.GET)
    @ResponseBody
    public List<UserInfo> getAllUserInfo(){
        return userService.getAllUserInfo();
    }
}
