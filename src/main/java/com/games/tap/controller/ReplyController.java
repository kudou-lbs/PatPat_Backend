package com.games.tap.controller;

import com.games.tap.domain.Reply;
import com.games.tap.mapper.ReplyMapper;
import com.games.tap.util.Echo;
import com.games.tap.util.RetCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Tag(name = "reply",description = "回复接口，包含主题贴回复、楼中楼回复的基本操作和点赞功能")
public class ReplyController {

    @Resource
    ReplyMapper replyMapper;

    @Operation(summary = "删除回复",description = "通过rid删除回复")
    @RequestMapping(value = "reply",method = RequestMethod.DELETE)
    public Echo deleteReply(String rid){
        if(rid==null||rid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(rid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Reply reply=replyMapper.getReplyById(Long.parseLong(rid));
        if(reply==null)return Echo.fail("回复Id不存在");
        if(reply.getIsFloor()){

        }else{

        }
        if(replyMapper.deleteByFloorNum(reply.getFloorNum())!=0)return Echo.success();
        return Echo.fail();
    }
}
