package com.games.tap.controller;

import com.games.tap.domain.Forum;
import com.games.tap.domain.ForumUser;
import com.games.tap.mapper.ForumMapper;
import com.games.tap.mapper.ForumUserMapper;
import com.games.tap.mapper.UserMapper;
import com.games.tap.service.ImageService;
import com.games.tap.util.ToolUtil;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import com.games.tap.util.RetCode;
import com.games.tap.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "forum", description = "论坛板块的接口，对不同游戏的论坛进行管理，包括用户权限管理")
public class ForumController {//TODO 加入权限校验

    @Resource
    ForumMapper forumMapper;
    @Resource
    ImageService imageService;
    @Resource
    UserMapper userMapper;
    @Resource
    ForumUserMapper forumUserMapper;

    @Operation(summary = "新建论坛", description = "输入论坛名和描述新建论坛", parameters = {
            @Parameter(name = "name", description = "论坛名", required = true),
            @Parameter(name = "intro", description = "介绍"),
            @Parameter(name = "icon", description = "论坛图标")
    })
    @RequestMapping(value = "/forum", method = RequestMethod.POST)
    public Echo addForum(String name, String intro, MultipartFile icon) {
        if (name == null || name.equals("")) return Echo.fail("论坛名不能为空");
        if (forumMapper.getForumByName(name) != null) return Echo.fail("论坛已经存在");
        Forum forum = new Forum();
        forum.setName(name);
        if (intro == null || intro.equals("")) forum.setIntro("感觉不如原神");
        else forum.setIntro(intro);
        if (icon != null && !icon.isEmpty()) {
            Map<String, String> map = imageService.uploadImage(icon);
            if (map.containsKey("path")) forum.setIcon(map.get("path"));
            else return Echo.fail(map.get("result"));
        }
        if (forumMapper.insertForum(forum) != 0) return Echo.success(forum.getFId());
        return Echo.fail("数据库操作失败");
    }

    @Operation(summary = "更新论坛", description = "id必填，其他修改项任选，但不能同时为空", parameters = {
            @Parameter(name = "name", description = "论坛名", required = true),
            @Parameter(name = "intro", description = "介绍"),
            @Parameter(name = "icon", description = "论坛图标")
    })
    @RequestMapping(value = "/forum/{id}", method = RequestMethod.PUT)
    public Echo updateForum(@PathVariable("id") String id, String name, String intro, MultipartFile icon) {
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Forum saveForum = forumMapper.getForumById(Long.parseLong(id));
        if (saveForum == null) return Echo.fail("论坛不存在");
        if ((name == null || name.equals("")) && (intro == null || intro.equals("")) && icon == null)
            return Echo.define(RetCode.PARAM_IS_EMPTY);
        else {
            if (name != null && !name.equals("") && !name.equals(saveForum.getName())) {
                if (forumMapper.getForumByName(name) != null) return Echo.fail("论坛名重复");
                saveForum.setName(name);
            }
            if (intro != null && !intro.equals("") && !intro.equals(saveForum.getIntro())) {
                saveForum.setIntro(intro);
            }
            if (icon != null && !icon.isEmpty()) {
                Map<String, String> map = imageService.uploadImage(icon);
                if (map.containsKey("path")) saveForum.setIcon(map.get("path"));
                else return Echo.fail(map.get("result"));
            }
            if (forumMapper.updateForum(saveForum) != 0) return Echo.success();
            return Echo.fail("数据库操作失败");
        }
    }

    @Operation(summary = "删除论坛", description = "通过id删除论坛")
    @RequestMapping(value = "/forum/{id}", method = RequestMethod.DELETE)
    public Echo deleteForum(@PathVariable("id") String id) {
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long fid=Long.parseLong(id);
        if(forumMapper.getForumById(fid)==null)return Echo.fail("论坛不存在");
        String icon=forumMapper.selectIconById(fid);
        if(imageService.deleteFiles(icon))return Echo.fail("删除图片失败");
        List<String>list=forumMapper.getPicsByFId(fid);
        boolean flag=true;
        for(String path:list){
            flag&= imageService.deleteFiles(path);
        }
        if(!flag)return Echo.fail("删除图片失败");
        if (forumMapper.deleteForumById(fid) != 0) return Echo.success();
        return Echo.fail();
    }

    @PassToken
    @Operation(summary = "获取论坛详细信息", description = "fid是论坛的id,传入uid判断是否传等级信息")
    @RequestMapping(value = "/forum/{id}", method = RequestMethod.GET)
    public Echo getForum(@PathVariable("id") String fid,String uid) {
        if (!StringUtils.isNumeric(fid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long fId=Long.parseLong(fid);
        if(uid!=null&&!uid.equals("")){
            if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            long uId=Long.parseLong(uid);
            if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
            if(forumUserMapper.getForumUser(uId,fId)!=null){
                UserForumInfo forum = forumMapper.getForumInfo(fId,uId);
                if (forum == null) return Echo.fail("论坛不存在");
                forum.setMaxExp(ToolUtil.maxExp(forum.getLevel()));
                return Echo.success(forum);
            }
        }
        Forum forum= forumMapper.getForumById(fId);
        if (forum == null) return Echo.fail("论坛不存在");
        return Echo.success(forum);
    }

    @PassToken
    @Operation(summary = "通过id获取关注该论坛的用户", description = "id是论坛的id")
    @RequestMapping(value = "/forum/user", method = RequestMethod.GET)
    public Echo getForumUser(String id) {
        if(id==null||id.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        List<ForumUser> forumUsers = forumUserMapper.getLikeForumUser(Long.parseLong(id));
        if (forumUsers == null||forumUsers.isEmpty()) return Echo.fail();
        return Echo.success(forumUsers);
    }

    @PassToken
    @Operation(summary = "获取所有论坛", description =
            "通过起始位置，数量获取列表，不带参数为返回全部信息，带参数时offset可选，pageSize必带，uid可选,未登录关注列默认为空，登录返回是否关注数据",parameters = {
            @Parameter(name = "uid",description = "用户id"),
            @Parameter(name = "pageSize",description = "返回数量",required = true),
            @Parameter(name = "offset",description = "起始位置")
    })
    @RequestMapping(value = "/forum", method = RequestMethod.GET)
    public Echo getForums(String offset, String pageSize, String uid) {
        Echo echo = ToolUtil.checkList(uid, offset, pageSize);
        if (echo != null) return echo;
        Long id, size = null, start = null;
        if (offset != null && !offset.equals("")) start = Long.parseLong(offset);
        if (pageSize != null && !pageSize.equals("")) size = Long.parseLong(pageSize);
        List<ForumInfo> list;
        if (uid != null && !uid.equals("")) {
            id = Long.parseLong(uid);
            if (userMapper.getUserById(id) == null) return Echo.define(RetCode.USER_NOT_EXIST);
            list = forumMapper.getForumListWithUserId(id, start, size);
        } else {
            list = forumMapper.getForumList(start, size);
        }
        if (list == null || list.isEmpty()) return Echo.fail();
        return Echo.success(list);
    }

    @Operation(summary = "获取用户关注的论坛", description = "取得列表")
    @RequestMapping(value = "/forum/like", method = RequestMethod.GET)
    public Echo getLikeForum(String uid,String offset, String pageSize) {
        if(uid==null||uid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        Echo echo= ToolUtil.checkList(uid,offset,pageSize);
        if(echo!=null)return echo;
        Long id = Long.parseLong(uid),start=null,size=null;
        if (userMapper.getUserById(id) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if(offset!=null)start=Long.parseLong(offset);
        if(pageSize!=null)size=Long.parseLong(pageSize);
        List<LikeForum>list= forumUserMapper.getLikeForum(id,start,size);
        if (list == null || list.isEmpty()) return Echo.fail("数据为空");
        for(LikeForum item:list){
            item.setMaxExp(ToolUtil.maxExp(item.getLevel()));
        }
        return Echo.success(list);
    }

    @PassToken
    @Operation(summary = "获取论坛的帖子", description = "取得帖子列表，order定义排序，0 回复时间排序，1 发布时间排序，2 回复数量排序，fid必选，其他可选")
    @RequestMapping(value = "/forum/post", method = RequestMethod.GET)
    public Echo getForumPost(String uid,String fid,String offset, String pageSize,String order) {
        if(fid==null||fid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        Echo echo=ToolUtil.checkList(fid,offset,pageSize);
        if(echo!=null)return echo;
        Long fId = Long.parseLong(fid),start=null,size=null,uId=null;
        if (userMapper.getUserById(fId) == null) return Echo.fail("论坛不存在");
        if(uid!=null&&!uid.equals("")){
            if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            uId=Long.parseLong(uid);
            if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        }
        int rank=0;
        if (order != null) {
            if (!StringUtils.isNumeric(order)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            rank=Integer.parseInt(order);
            if (rank<0||rank>2) return Echo.define(RetCode.PARAM_IS_INVALID);
        }
        if(offset!=null)start=Long.parseLong(offset);
        if(pageSize!=null)size=Long.parseLong(pageSize);

        List<PostBasicInfo>list= forumMapper.getForumPostList(fId,uId,start,size,rank);
        if (list == null || list.isEmpty()) return Echo.fail("数据为空");
        return Echo.success(list);
    }

    @Operation(summary = "用户关注论坛", description = "没有则新建论坛用户，有则更新")
    @RequestMapping(value = "forum/like", method = RequestMethod.POST)
    public Echo postLikeForum(String fid, String uid) {
        if(fid==null||fid.equals("")||uid==null||uid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(uid) || !StringUtils.isNumeric(fid))
            return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long fId = Long.parseLong(fid), uId = Long.parseLong(uid);
        if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        Forum forum= forumMapper.getForumById(fId);
        if (forum == null) return Echo.fail("论坛不存在");
        ForumUser forumUser = forumUserMapper.getForumUser(uId, fId);
        if (forumUser == null) {
            forumUser = new ForumUser(uId, fId, true,0,forum.getName());
            if (forumUserMapper.insertForumUser(forumUser) == 0) return Echo.fail();
            if (forumUserMapper.likeForum(uId, fId) != 0) return Echo.success();
        } else {
            if (forumUser.getIsLike()) return Echo.fail("已经关注过了");
            if (forumUserMapper.likeForum(uId, fId) != 0) return Echo.success();
        }
        return Echo.fail();
    }

    @Operation(summary = "修改用户论坛权限", description = "没有则新建论坛用户，有则设置权限")
    @RequestMapping(value = "forum/like", method = RequestMethod.PUT)
    public Echo updateForumRole(String fid, String uid, String role) {
        if(fid==null||uid==null||role==null)return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(uid) || !StringUtils.isNumeric(fid) || !StringUtils.isNumeric(role))
            return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long fId = Long.parseLong(fid), uId = Long.parseLong(uid);
        int identity = Integer.parseInt(role);
        if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        Forum forum= forumMapper.getForumById(fId);
        if (forum == null) return Echo.fail("论坛不存在");
        if (identity < 0 || identity > 2) return Echo.define(RetCode.PARAM_IS_INVALID);
        ForumUser forumUser = forumUserMapper.getForumUser(uId, fId);
        if (forumUser == null) {
            forumUser = new ForumUser(uId, fId, false,identity,forum.getName());
            if (forumUserMapper.insertForumUser(forumUser) != 0) return Echo.success();
        } else {
            if (forumUser.getIdentity() == identity) return Echo.fail("权限相同，无法更改");
            if (forumUserMapper.updateIdentity(uId, fId, identity) != 0) return Echo.success();
        }
        return Echo.fail();
    }

    @Operation(summary = "用户取消关注论坛", description = "用户的论坛信息没有删除，只是取消关注")
    @RequestMapping(value = "forum/like", method = RequestMethod.DELETE)
    public Echo deleteLikeForum(String fid, String uid) {
        if(fid==null||fid.equals("")||uid==null||uid.equals(""))return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(uid) || !StringUtils.isNumeric(fid))
            return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long fId = Long.parseLong(fid), uId = Long.parseLong(uid);
        if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (forumMapper.getForumById(fId) == null) return Echo.fail("论坛不存在");
        Boolean isLike = forumUserMapper.isLike(uId, fId);
        if (isLike == null) return Echo.fail("就没有关注过该论坛");
        if (!isLike)return Echo.fail("已经取关过了");
        if (forumUserMapper.unlikeForum(uId, fId) != 0) return Echo.success();
        return Echo.fail();
    }
}
