package com.games.tap.controller;

import com.games.tap.domain.UserInfo;
import com.games.tap.service.UserService;
import com.games.tap.util.Echo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DbTestController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String hello(){
        return "Hello";
    }

    @RequestMapping(value = "/UserInfo/getAll",method = RequestMethod.GET)
    public List<UserInfo> getAllUserInfo(){
        List<UserInfo> test=userService.getAllUserInfo();
        System.out.println(test);
        return test;
    }
}
