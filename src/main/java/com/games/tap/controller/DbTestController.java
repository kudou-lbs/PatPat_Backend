package com.games.tap.controller;

import com.games.tap.mapper.UserMapper;


import com.games.tap.util.PassToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

@Tag(name = "test")
@Controller
public class DbTestController {
    @Resource
    private UserMapper userMapper;

    @PassToken
    @Operation(summary = "主页",description = "null")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String hello(){
        return "acg.gy_22.jpg";
    }

    @PassToken
    @Operation(summary = "展示用户头像")
    @RequestMapping(value = "/showImage",method = RequestMethod.GET)
    public String getAvatarPathById(Long id){
        return userMapper.getUserAvatarById(id);
    }
}
