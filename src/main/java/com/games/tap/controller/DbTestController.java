package com.games.tap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.tap.domain.UserInfo;
import com.games.tap.service.UserService;
import com.games.tap.service.UploadImg;


import com.games.tap.util.PassToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Tag(name = "数据库操作测试")
@RequestMapping("/test")
@RestController
public class DbTestController {
    @Autowired
    private UserService userService;
    @Resource
    private UploadImg uploadImg;
    private ObjectMapper mapper = new ObjectMapper();

    @Operation(summary = "主页",description = "null")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }

    @Operation(summary = "获取所有用户信息" )
    @RequestMapping(value = "/user/getAll",method = RequestMethod.GET)
    public List<UserInfo> getAllUser(){
        return userService.getAllUser();
    }

    @Operation(summary = "上传图片")
    @RequestMapping(value = "/uploadImg",method = RequestMethod.GET)
    @ResponseBody
    //文件上传
    public String uploadImg(@RequestParam("fileName") MultipartFile file, @RequestParam("nickname") String nickName,HttpServletRequest request) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(nickName);

        return uploadImg.uploadAvatar(file);
    }

    @Operation(summary = "获取图片路径")
    @RequestMapping(value = "/getImgPath",method = RequestMethod.GET)
    @ResponseBody
    public String getImgPathByOwner(@RequestParam("username")String username){
        List<UserInfo> imgs= userService.getPicByUserName(username);
        HashMap<String, List> map = new HashMap<>();
        ArrayList<String> paths = new ArrayList<>();
        if(imgs!=null && !imgs.isEmpty()){
            for(UserInfo i:imgs){
                paths.add(i.getAvatar());
            }
        }
        map.put("paths", paths);

        String result;
        try {
            result = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }

        return result;
    }



}
