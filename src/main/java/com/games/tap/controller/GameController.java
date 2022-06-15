package com.games.tap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "game",description = "游戏数据接口，提供数据操作、排行榜、分类、搜索等功能")
public class GameController {
}
