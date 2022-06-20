package com.games.tap.controller;

import com.games.tap.domain.Game;
import com.games.tap.mapper.UserMapper;
import com.games.tap.service.GameService;
import com.games.tap.service.ImageService;


import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Tag(name = "test")
@RequestMapping("/test")
@Controller
public class DbTestController {
    @Resource
    private UserMapper userMapper;

    @PassToken
    @Operation(summary = "主页",description = "null")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @Operation(summary = "展示用户头像")
    @RequestMapping(value = "/showImage",method = RequestMethod.GET)
    public String getAvatarPathById(Long id){
        return userMapper.getUserAvatarById(id);
    }
}
