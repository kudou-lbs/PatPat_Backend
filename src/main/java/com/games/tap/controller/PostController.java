package com.games.tap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "post",description = "帖子接口，提供帖子的基本操作、点赞、收藏等功能")
public class PostController {
}
