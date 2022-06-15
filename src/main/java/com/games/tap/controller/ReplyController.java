package com.games.tap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "reply",description = "回复接口，包含主题贴回复、楼中楼回复的基本操作和点赞功能")
public class ReplyController {
}
