package com.games.tap.controller;

import com.games.tap.domain.Post;
import com.games.tap.mapper.ForumMapper;
import com.games.tap.mapper.PostMapper;
import com.games.tap.mapper.UserMapper;
import com.games.tap.service.ImageService;
import com.games.tap.util.DateUtil;
import com.games.tap.util.Echo;
import com.games.tap.util.RetCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@Tag(name = "post",description = "帖子接口，提供帖子的基本操作、点赞、收藏等功能")
public class PostController {

    @Resource
    ImageService imageService;
    @Resource
    PostMapper postMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ForumMapper forumMapper;

    @Operation(summary = "发布帖子",description = "帖子必须有标题，内容和图片不可同时为空")
    @RequestMapping(value = "/post",method = RequestMethod.POST)
    public Echo addPost(String fid, String uid, String title, String content, MultipartFile image){
        if(fid==null||fid.equals("")||uid==null||uid.equals("")||title==null||title.equals(""))
            return Echo.define(RetCode.PARAM_IS_EMPTY);
        if((content==null||content.equals(""))&&(image==null||image.isEmpty()))
            return Echo.fail("内容和图片不可同时为空");
        if (!StringUtils.isNumeric(uid) || !StringUtils.isNumeric(fid))
            return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Long fId=Long.parseLong(fid),uId=Long.parseLong(uid);
        if(userMapper.getUserById(uId)==null)return Echo.define(RetCode.USER_NOT_EXIST);
        if(forumMapper.getForumById(fId)==null)return Echo.fail("论坛不存在");
        Post post=new Post(uId,fId,title);
        if(content!=null&&!content.equals(""))post.setContent(content);
        if(image!=null&&!image.isEmpty()){
            Map<String, String> map = imageService.uploadImage(image);
            if (map.containsKey("path")) post.setPicture(map.get("path"));
            else return Echo.fail(map.get("result"));
        }
        String ctime= DateUtil.getCurrentTime();
        post.setPostTime(ctime);
        post.setLastDate(ctime);
        if(postMapper.insertPost(post)!=0)return Echo.success();
        return Echo.fail();
    }

    @Operation(summary = "帖子阅读量加1",description = "通过id增加帖子阅读量，当用户点击帖子时调用")
    @RequestMapping(value = "/post/read",method = RequestMethod.POST)
    public Echo readPost(String id){
        if(id==null||id.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Long pid=Long.parseLong(id);
        if(postMapper.getPostByPId(pid)==null)return Echo.fail("帖子不存在");
        if(postMapper.updateReadingNum(pid)!=0)return Echo.success();
        return Echo.fail();
    }

    @Operation(summary = "删除帖子",description = "通过id删除帖子，注意：删除帖子其对应的回复也会删除")
    @RequestMapping(value = "/post",method = RequestMethod.DELETE)
    public Echo deletePost(String pid){
        if(pid==null||pid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Long id=Long.parseLong(pid);
        if(postMapper.getPostByPId(id)==null)return Echo.fail("帖子不存在");
        if(postMapper.deleteByPId(id)!=0)return Echo.success();
        return Echo.fail();
    }
}
