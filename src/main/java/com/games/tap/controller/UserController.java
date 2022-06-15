package com.games.tap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "user",description = "用户接口，提供基本信息修改，背景图，登录注册，互相关注等功能")
public class UserController {
}
