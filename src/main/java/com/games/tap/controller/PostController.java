package com.games.tap.controller;

import com.games.tap.domain.Game;
import com.games.tap.service.LACService;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import com.games.tap.util.RetCode;
import com.games.tap.vo.UserPostInfo;
import io.swagger.v3.oas.annotations.Operation;
import com.games.tap.domain.Post;
import com.games.tap.mapper.ForumMapper;
import com.games.tap.mapper.PostMapper;
import com.games.tap.mapper.UserMapper;
import com.games.tap.service.ImageService;
import com.games.tap.util.DateUtil;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import com.games.tap.util.RetCode;
import com.games.tap.vo.PostInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
        long fId=Long.parseLong(fid),uId=Long.parseLong(uid);
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
        if(postMapper.insertPost(post)==0)return Echo.fail("新增帖子失败");
        if(forumMapper.addPostNum(fId)==0)return Echo.fail("论坛更新失败");
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "获得详细帖子信息",description = "通过pid获取帖子,登录时需要传入uid判断是否喜欢")
    @RequestMapping(value = "/post",method = RequestMethod.GET)
    public Echo getPost(String pid,String uid){
        if(pid==null||pid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Long fId=Long.parseLong(pid),uId=null;
        if(postMapper.getPostByPId(fId)==null)return Echo.fail("帖子不存在");
        if(uid!=null&&!uid.equals("")){
            if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            uId=Long.parseLong(uid);
            if(userMapper.getUserById(uId)==null)return Echo.define(RetCode.USER_NOT_EXIST);
        }
        PostInfo postInfo=postMapper.getPostInfo(fId,uId);
        if(postInfo==null)return Echo.fail();
        return Echo.success(postInfo);
    }

    @PassToken
    @Operation(summary = "",description = "")
    @RequestMapping(value = "/post/reply",method = RequestMethod.GET)
    public Echo getPostReply(){
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "帖子阅读量加1",description = "通过id增加帖子阅读量，当用户点击帖子时调用")
    @RequestMapping(value = "/post/read",method = RequestMethod.POST)
    public Echo readPost(String pid){
        if(pid==null||pid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long id=Long.parseLong(pid);
        if(postMapper.getPostByPId(id)==null)return Echo.fail("帖子不存在");
        if(postMapper.updateReadingNum(id)!=0)return Echo.success();
        return Echo.fail();
    }

    @Operation(summary = "删除帖子",description = "通过id删除帖子，注意：删除帖子其对应的回复也会删除")
    @RequestMapping(value = "/post",method = RequestMethod.DELETE)
    public Echo deletePost(String pid){
        if(pid==null||pid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long id=Long.parseLong(pid);
        Long fid= postMapper.getFIdByPId(id);
        if(fid==null)return Echo.fail("帖子不存在");
        if(postMapper.deleteByPId(id)==0) return Echo.fail("帖子删除失败");
        if(forumMapper.subPostNum(fid)==0)return Echo.fail("论坛更新失败");
        return Echo.success();
    }
    @Resource
    LACService lacService;

    @PassToken
    @Operation(summary = "帖子点赞")
    @RequestMapping(value = "/post/like",method = RequestMethod.POST)
    public Echo postLike(String uid,String pid){
        if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long uId=Long.parseLong(uid),pId=Long.parseLong(pid);
        if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (postMapper.getPostByPId(pId) == null) return Echo.fail("帖子不存在");
        if (lacService.isExited(uId,pId) != null) return Echo.fail("已经点赞");

        Integer like= lacService.postLike(uId,pId);
        lacService.addLikeNum(pId);
        if (like == 0) return Echo.fail("点赞失败");
        return Echo.success("点赞成功");
    }

    @PassToken
    @Operation(summary = "取消帖子点赞")
    @RequestMapping(value = "/post/cancel",method = RequestMethod.DELETE)
    @RequestMapping(value = "/post/unlike",method = RequestMethod.POST)
    public Echo postCancelLike(String uid,String pid){
        if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if (!StringUtils.isNumeric(pid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Long uId=Long.parseLong(uid);
        Long pId=Long.parseLong(pid);
        if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (postMapper.getPostByPId(pId) == null) return Echo.fail("帖子不存在");
        if (lacService.isExited(uId,pId) == null) return Echo.fail("还没有点赞");

        Integer like= lacService.postCancelLike(uId,pId);
        lacService.subLikeNum(pId);
        if (like == 0) return Echo.fail("取消点赞失败");
        return Echo.success("取消点赞成功");
    }

    @PassToken
    @Operation(summary = "根据点赞获取帖子内容")
    @RequestMapping(value = "/post/like/getposts",method = RequestMethod.GET)
    public Echo getPostByLike(String uid,String offset, String pageSize){
        if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Long uId=Long.parseLong(uid);
        if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);

        if (offset == null && pageSize == null) {
            List<UserPostInfo> postInfos=lacService.getUserPostList(uId,null,null);
            if (postInfos == null || postInfos.isEmpty()) return Echo.fail();
            return Echo.success(postInfos);
        } else if (pageSize == null) {
            return Echo.fail("pageSize不能为空");
        }else {
            if (!StringUtils.isNumeric(pageSize)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            if (Long.parseLong(pageSize) <= 0) return Echo.define(RetCode.PARAM_IS_INVALID);
            long size=Long.parseLong(pageSize),start;
            if (offset == null)start=0L;
            else {
                if (!StringUtils.isNumeric(offset)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
                if (Long.parseLong(offset) < 0) return Echo.define(RetCode.PARAM_IS_INVALID);
                start=Long.parseLong(offset);
            }
            List<UserPostInfo> postInfos=lacService.getUserPostList(uId,start,size);
            if (postInfos == null || postInfos.isEmpty()) return Echo.fail();
            return Echo.success(postInfos);
        }
    }

}
