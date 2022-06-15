package com.games.tap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "forum",description = "论坛板块的接口，对不同游戏的论坛进行管理，包括用户权限管理")
public class ForumController {
}
