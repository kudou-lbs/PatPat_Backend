package com.games.tap.controller;

import com.games.tap.domain.User;
import com.games.tap.mapper.ConMapper;
import com.games.tap.mapper.UserMapper;
import com.games.tap.service.ImageService;
import com.games.tap.util.ToolUtil;
import com.games.tap.util.*;
import com.games.tap.vo.UserInfo;
import com.games.tap.vo.UserPostInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
@Tag(name = "user", description = "用户接口，提供基本信息修改，背景图，登录注册，互相关注等功能")
public class UserController {

    @Resource
    UserMapper userMapper;
    @Resource
    PasswordEncoder encoder;
    @Resource
    ImageService imageService;
    @Resource
    ConMapper conMapper;

    @PassToken
    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    @Operation(summary = "用户登录", description = "返回token，与用户相关的需要header携带token才能进行操作", parameters = {
            @Parameter(name = "username", description = "账户名", required = true),
            @Parameter(name = "password", description = "密码", required = true)
    })
    public Echo login(String username, String password) {
        // TODO 前后端使用密钥，报文加密传输
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            return Echo.define(RetCode.PARAM_IS_EMPTY);
        User saveUser = userMapper.getUserByUserName(username);
        if (saveUser == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (encoder.matches(password, saveUser.getPassword())) {// pwd与已加密的进行匹配
            // 先验证用户的账号密码,账号密码验证通过之后，生成Token
            Map<String, Object> map = new HashMap<>();
            String token = JwtUtil.createToken(saveUser);
            map.put("user", saveUser);
            map.put("token", token);
            return Echo.success(map);
        }
        return Echo.define(RetCode.USER_LOGIN_ERROR);
    }

    @PassToken
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @Operation(summary = "用户注册", description = "注册账号", parameters = {
            @Parameter(name = "username", description = "账户名", required = true),
            @Parameter(name = "password", description = "密码", required = true),
            @Parameter(name = "nickname", description = "昵称"),
            @Parameter(name = "gender", description = "性别")
    })
    public Echo register(@RequestBody User user) {
        // 合法性校验
        Echo echo = ToolUtil.checkUser(user);
        if (echo != null) return echo;
        if (userMapper.getUserByUserName(user.getUsername()) != null)
            return Echo.define(RetCode.USER_HAS_EXISTED);
        //  初始化设置
        if (user.getNickname() == null || user.getNickname().equals("")) {
            String name = "用户" + Long.toHexString(SnowFlake.nextId()).replace("0", "");
            user.setNickname(name);
        }
        if (user.getIntro() == null || user.getIntro().equals("")) user.setIntro("签名是一种态度，我想我可以更酷");
        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getGender() == null) user.setGender(0);
        user.setRegisterTime(DateUtil.getCurrentTime());
        if (userMapper.insertUser(user) > 0) {
            log.info(user.getUsername() + "注册成功");
            return Echo.success();
        } else return Echo.fail("注册失败");
    }

    @Operation(summary = "登录校验", description = "携带参数为header中的token，登录获取，过期需要重新登录")
    @PostMapping("/user/test")
    public String testToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token.isEmpty()) return "请求失败";
        JwtUtil.parseToken(token);
        return "请求成功";
    }

    @PassToken
    @Operation(summary = "获取用户信息列表", description = "通过起始位置，数量获取列表，不带参数为返回全部信息，带参数时offset可选，pageSize必带")//TODO：加入权限验证
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Echo getUserList(String offset, String pageSize) {
        if (offset == null && pageSize == null) {
            List<User> list = userMapper.getAllUser();
            if (list == null || list.isEmpty()) return Echo.fail();
            return Echo.success(list);
        } else {
            Echo echo = ToolUtil.checkList(null, offset, pageSize);
            if (echo != null) return echo;
            Long start = null, size = Long.parseLong(pageSize);
            if (offset != null) start = Long.parseLong(offset);
            List<UserInfo> list = userMapper.getUserList(start, size);
            if (list == null || list.isEmpty()) return Echo.fail();
            return Echo.success(list);
        }
    }

    @Operation(summary = "更改用户信息", description = "传入用户的部分信息,对单个用户进行更改")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Echo updateUser(@PathVariable("id") String id, @RequestBody User user) {
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long uid = Long.parseLong(id);
//        if (!Objects.equals(uid, user.getUId())) return Echo.fail("请求id不一致，id不可更改");
        User saveUser = userMapper.getUserById(uid);
        if(user.getUsername()!=null){
            Long did = userMapper.getIdByUserName(user.getUsername());
            if (did != null && !did.equals(uid)) return Echo.fail("用户名已存在");
            else if (did == null) saveUser.setUsername(user.getUsername());
        }
        if (user.getIntro() != null) saveUser.setIntro(user.getIntro());
        if (user.getNickname() != null) saveUser.setNickname(user.getNickname());
        if (user.getGender() != null && user.getGender() >= 0 && user.getGender() < 3)
            saveUser.setGender(user.getGender());
        if (user.getRegisterTime()!=null&&!Objects.equals(saveUser.getRegisterTime(), user.getRegisterTime()))
            return Echo.fail("注册时间不可更改");
        if ((user.getFansNum()!=null&&!Objects.equals(saveUser.getFansNum(), user.getFansNum()))
                || (user.getFollowNum()!=null&&!Objects.equals(saveUser.getFollowNum(), user.getFollowNum())))
            return Echo.fail("关注和粉丝数不可显式修改");
        if (user.getPassword()!=null&&!encoder.matches(user.getPassword(), saveUser.getPassword())){
            if(ToolUtil.checkPassword(user.getPassword())){
                saveUser.setPassword(encoder.encode(user.getPassword()));
            }else
                return Echo.fail("密码格式错误");
        }
        if (userMapper.updateUser(saveUser) != 0) return Echo.success();
        else return Echo.fail();
    }

    @Operation(summary = "删除用户", description = "通过id删除，该方法应加入权限验证")//TODO 用户删除相应表也应该删除
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public Echo deleteById(@PathVariable("id") String id) {// FIXME 用户只能注销自己的用户，管理员才有任意操作的权限
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        String avatarPath=userMapper.selectAvatarById(Long.parseLong(id));
        imageService.deleteFiles(avatarPath);
        String picPath=userMapper.selectBackById(Long.parseLong(id));
        imageService.deleteFiles(picPath);
        if (userMapper.deleteUserById(Long.parseLong(id)) != 0) return Echo.success();
        else return Echo.fail();
    }

    @PassToken
    @Operation(summary = "通过id查找用户")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Echo getUserById(@PathVariable("id") String id) {
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        User user = userMapper.getUserById(Long.parseLong(id));
        if (user == null) return Echo.fail();
        return Echo.success(user);
    }

    @PassToken
    @Operation(summary = "获取用户头像", description = "目前返回的是路径")
    @RequestMapping(value = "/user/{id}/avatar", method = RequestMethod.GET)
    public Echo getAvatarPathById(@PathVariable("id") String id) {
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        String path = userMapper.getUserAvatarById(Long.parseLong(id));
        if (path == null || path.equals("")) return Echo.fail();
        return Echo.success(path);
    }

    @Operation(summary = "上传用户头像", description = "用户只能上传自己的头像")
    @RequestMapping(value = "/user/{id}/avatar", method = RequestMethod.POST)
    //文件上传
    public Echo uploadAvatar(@RequestParam("filename") MultipartFile file, @PathVariable("id") String id, HttpServletRequest request) {
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long uid = Long.parseLong(id);
        if (!Objects.equals(uid, ToolUtil.getIdByToken(request.getHeader("token"))))// TODO 最后改成 @RequestHeader的格式
            return Echo.define(RetCode.PERMISSION_NO_ACCESS);
        if (userMapper.getUserById(uid) == null) return Echo.fail("用户不存在");
        Map<String, String> map = imageService.uploadImage(file);
        if (map.containsKey("path")) {
            if (userMapper.updateUserAvatar(uid, map.get("path")) != 0)
                return Echo.success();
            else return Echo.fail("数据库操作失败");
        }
        return Echo.fail(map.get("result"));
    }

    @PassToken
    @Operation(summary = "获取用户空间背景", description = "目前返回的是路径")
    @RequestMapping(value = "/user/{id}/background", method = RequestMethod.GET)
    public Echo getImgPathByOwner(@PathVariable("id") String id) {
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long uid = Long.parseLong(id);
        String path = userMapper.getBackgroundById(uid);
        if (path == null || path.equals("")) return Echo.fail();
        return Echo.success(path);
    }

    @Operation(summary = "上传用户背景图", description = "用户只能上传自己的背景")
    @RequestMapping(value = "/user/{id}/background", method = RequestMethod.POST)
    //文件上传
    public Echo uploadBack(@RequestParam("filename") MultipartFile file, @PathVariable("id") String id, HttpServletRequest request) {
        if (!StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long uid = Long.parseLong(id);
        if (!Objects.equals(uid, ToolUtil.getIdByToken(request.getHeader("token"))))
            return Echo.define(RetCode.PERMISSION_NO_ACCESS);
        if (userMapper.getUserById(uid) == null) return Echo.fail("用户不存在");
        Map<String, String> map = imageService.uploadImage(file);
        if (map.containsKey("path")) {
            if (userMapper.updateBackground(uid, map.get("path")) != 0)
                return Echo.success();
            else return Echo.fail("数据库操作失败");
        }
        return Echo.fail(map.get("result"));
    }

    @PassToken
    @Operation(summary = "获取用户发布的帖子列表或随机帖子", description = "通过uid查找用户的帖子,order定义排序，0 最近发布时间，1 最早发布时间，2 回复数量排序，3 随机，默认0,随机时不能获取用户发帖")
    @RequestMapping(value = "user/post", method = RequestMethod.GET)
    public Echo getUserPostList(String uid, String offset, String pageSize, String order) {
        Echo echo = ToolUtil.checkList(uid, offset, pageSize);
        if (echo != null) return echo;
        Long start = null, size = null, id = null;
        if(uid!=null&&!uid.equals("")){
            id = Long.parseLong(uid);
            if (userMapper.getUserById(id) == null) return Echo.fail("论坛不存在");
        }
        int rank = 0;
        if (order != null) {
            if (!StringUtils.isNumeric(order)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            rank = Integer.parseInt(order);
            if (rank < 0 || rank > 3) return Echo.define(RetCode.PARAM_IS_INVALID);
        }
        if (offset != null) start = Long.parseLong(offset);
        if (pageSize != null) size = Long.parseLong(pageSize);
        List<UserPostInfo> list = userMapper.getUserPostList(id, start, size, rank);
        if (list == null || list.isEmpty()) return Echo.fail("数据为空");
        return Echo.success(list);
    }

    @Operation(summary = "粉丝列表", description = "获取关注该用户的粉丝列表")
    @RequestMapping(value = "/user/{id}/fan", method = RequestMethod.GET)
    public Echo getFanList(@PathVariable String id, String offset, String pageSize) {
        Echo echo = ToolUtil.checkList(id, offset, pageSize);
        if (echo != null) return echo;
        Long start = null, size = null, uid = Long.parseLong(id);
        if (userMapper.getUserById(uid) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (offset != null) start = Long.parseLong(offset);
        if (pageSize != null) size = Long.parseLong(pageSize);
        List<UserInfo> list = conMapper.getFanList(uid, start, size);
        if (list == null || list.isEmpty()) return Echo.fail("数据为空");
        return Echo.success(list);
    }

    @PassToken
    @Operation(summary = "关注列表", description = "获取该用户关注的用户列表")
    @RequestMapping(value = "/user/{id}/follow", method = RequestMethod.GET)
    public Echo getFollowList(@PathVariable String id, String offset, String pageSize) {
        Echo echo = ToolUtil.checkList(id, offset, pageSize);
        if (echo != null) return echo;
        Long start = null, size = null, uid = Long.parseLong(id);
        if (userMapper.getUserById(uid) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (offset != null) start = Long.parseLong(offset);
        if (pageSize != null) size = Long.parseLong(pageSize);
        List<UserInfo> list = conMapper.getFollowList(uid, start, size);
        if (list == null || list.isEmpty()) return Echo.fail("数据为空");
        return Echo.success(list);
    }

    @PassToken
    @Operation(summary = "关注用户", description = "关注其他用户")
    @RequestMapping(value = "/user/concern", method = RequestMethod.POST)
    public Echo follow(String followedId, String followingId, HttpServletRequest request) {
        if (!StringUtils.isNumeric(followedId) || !StringUtils.isNumeric(followingId))
            return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long ingId = Long.parseLong(followingId), edId = Long.parseLong(followedId);
        if (!Objects.equals(ingId, ToolUtil.getIdByToken(request.getHeader("token"))))
            return Echo.define(RetCode.PERMISSION_NO_ACCESS);
        if (conMapper.isFollow(ingId, edId) != null) return Echo.fail("已经关注过了");
        if (conMapper.follow(ingId, edId) > 0) {
            return Echo.success();
        } else
            return Echo.fail("关注失败");
    }

    @Operation(summary = "取消关注", description = "取消关注其他用户")
    @RequestMapping(value = "/user/concern", method = RequestMethod.DELETE)
    public Echo unfollow(String followedId, String followingId, HttpServletRequest request) {
        if (!StringUtils.isNumeric(followedId) || !StringUtils.isNumeric(followingId))
            return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long ingId = Long.parseLong(followingId), edId = Long.parseLong(followedId);
        if (!Objects.equals(ingId, ToolUtil.getIdByToken(request.getHeader("token"))))
            return Echo.define(RetCode.PERMISSION_NO_ACCESS);
        if (conMapper.isFollow(ingId, edId) == null) return Echo.fail("还没有关注");
        if (conMapper.unfollow(ingId, edId) > 0) {
            return Echo.success();
        } else
            return Echo.fail("取关失败");
    }

}
