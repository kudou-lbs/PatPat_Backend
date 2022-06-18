package com.games.tap.controller;

import com.games.tap.mapper.UserMapper;
import com.games.tap.service.ImageService;


import com.games.tap.util.Echo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Tag(name = "数据库操作测试")
@RequestMapping("/test")
@RestController
public class DbTestController {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ImageService imageService;

    @Operation(summary = "主页",description = "null")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }

    @Operation(summary = "上传图片",description = "测试用")
    @RequestMapping(value = "/uploadImg",method = RequestMethod.GET)
    //文件上传
    public Echo uploadImg(@RequestParam("fileName") MultipartFile file) throws IOException {
        Map<String,String>map=imageService.uploadImage(file);
        if(map.containsKey("path")){
            return Echo.success(map.get("path"));
        }
        return Echo.fail(map.get("result"));
    }



}
