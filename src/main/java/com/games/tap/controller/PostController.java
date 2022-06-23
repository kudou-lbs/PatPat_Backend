package com.games.tap.controller;

import com.games.tap.service.LACService;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import com.games.tap.util.RetCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Tag(name = "post",description = "帖子接口，提供帖子的基本操作、点赞、收藏等功能")
public class PostController {
    @Resource
    LACService lacService;

    @PassToken
    @Operation(summary = "帖子点赞（这里没有判断两个id是否存在）")
    @RequestMapping(value = "/post/like",method = RequestMethod.POST)
    public Echo postLike(String uid,String pid){
        if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Integer like= lacService.postLike(Long.parseLong(uid),Long.parseLong(pid));
        if (like == 0) return Echo.fail("点赞失败");
        return Echo.success("点赞成功");
    }

    @PassToken
    @Operation(summary = "取消帖子点赞（这里没有判断两个id是否存在）")
    @RequestMapping(value = "/post/cancel",method = RequestMethod.POST)
    public Echo postCancelLike(String uid,String pid){
        if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Integer like= lacService.postCancelLike(Long.parseLong(uid),Long.parseLong(pid));
        if (like == 0) return Echo.fail("取消点赞失败");
        return Echo.success("取消点赞成功");
    }
}
