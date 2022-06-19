package com.games.tap.controller;

import com.games.tap.domain.User;
import com.games.tap.mapper.UserMapper;
import com.games.tap.service.ImageService;
import com.games.tap.service.UserService;
import com.games.tap.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@Tag(name = "user", description = "用户接口，提供基本信息修改，背景图，登录注册，互相关注等功能")
public class UserController {

    @Resource
    UserMapper userMapper;
    @Resource
    UserService userService;
    @Resource
    PasswordEncoder encoder;
    @Resource
    ImageService imageService;

    @PassToken
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Operation(summary = "用户登录", description = "返回token，与用户相关的需要header携带token才能进行操作", parameters = {
            @Parameter(name = "username", description = "账户名", required = true),
            @Parameter(name = "password", description = "密码", required = true)
    })
    public Echo login(String username, String password) {
        // TODO 前后端使用密钥，报文加密传输
        if (username==null|| username.isEmpty() || password==null|| password.isEmpty())
            return Echo.define(RetCode.PARAM_IS_EMPTY);
        User saveUser = userMapper.getUserByUserName(username);
        if (saveUser == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (encoder.matches(password,saveUser.getPassword())) {// pwd与已加密的进行匹配
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
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Operation(summary = "用户注册", description = "注册账号", parameters = {
            @Parameter(name = "username", description = "账户名", required = true),
            @Parameter(name = "password", description = "密码", required = true),
            @Parameter(name = "nickname", description = "昵称"),
            @Parameter(name = "gender", description = "性别")
    })
    public Echo register(@RequestBody User user) {
        // 合法性校验
        Echo echo=userService.checkUser(user);
        if(echo!=null)return echo;
        if(user.getNickname()==null||user.getNickname().equals("")){
            String name="用户"+Long.toHexString(SnowFlake.nextId()).replace("0","");
            user.setNickname(name);
        }
        //  初始化设置
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getGender()==null)user.setGender(0);
        user.setRegisterTime(DateUtil.getCurrentTime());
        if(userMapper.insertUser(user)>0){
            log.info(user.getUsername()+"注册成功");
            return Echo.success();
        }
        else return Echo.fail("注册失败");
    }

    @Operation(summary = "登录校验",description = "携带参数为header中的token，登录获取，过期需要重新登录")
    @PostMapping("/testToken")
    public String testToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token.isEmpty()) return "请求失败";
        JwtUtil.parseToken(token);
        return "请求成功";
    }

    @Operation(summary = "获取所有用户信息",description = "TODO：加入权限验证")
    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public Echo getUsers(){
        List<User>list=userMapper.getAllUser();
        if(list==null|| list.isEmpty())return Echo.fail();
        return Echo.success(list);
    }

    @Operation(summary = "更改用户信息",description = "传入用户的完整信息,对单个用户进行更改")
    @RequestMapping(value = "/user/{id}",method = RequestMethod.PUT)
    public Echo updateUser(@PathVariable("id")Long id,@RequestBody User user){
        if(!Objects.equals(id, user.getUId()))return Echo.fail("请求id不一致，id不可更改");
        Echo echo=userService.checkUser(user);
        if(echo!=null)return echo;
        String pwd=userMapper.getPasswordById(id);
        if(!encoder.matches(user.getPassword(),pwd)&&!user.getPassword().equals(pwd)){
            user.setPassword(encoder.encode(user.getPassword()));
        }
        if(userMapper.updateUser(user)!=0)return Echo.success();
        else return Echo.fail();
    }

    @Operation(summary = "删除用户",description = "通过id删除，该方法应加入权限验证")
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public Echo deleteById(@PathVariable("id")Long id){// FIXME 用户只能注销自己的用户，管理员才有任意操作的权限
        if(userMapper.deleteUserById(id)!=0)return Echo.success();
        else return Echo.fail();
    }

    @Operation(summary = "通过id查找用户")
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public Echo getUserById(@PathVariable("id")Long id){
        User user= userMapper.getUserById(id);
        if(user==null)return Echo.fail();
        return Echo.success(user);
    }

    @Operation(summary = "获取用户头像",description = "目前返回的是路径")
    @RequestMapping(value = "/user/{id}/avatar",method = RequestMethod.GET)
    public Echo getAvatarPathById(@PathVariable("id")String id){
        String path= userMapper.getUserAvatarById(id);
        if(path==null||path.equals(""))return Echo.fail();
        return Echo.success(path);
    }

    @Operation(summary = "上传用户头像",description = "用户只能上传自己的头像")
    @RequestMapping(value = "/user/{id}/avatar",method = RequestMethod.POST)
    //文件上传
    public Echo uploadAvatar(@RequestParam("filename") MultipartFile file, @PathVariable("id") Long id) throws IOException {
        if(userMapper.getUserById(id)==null)return Echo.fail("用户不存在");
        Map<String,String>map=imageService.uploadImage(file);
        if(map.containsKey("path")){
            if(userMapper.updateUserAvatar(id,map.get("path"))!=0)
                return Echo.success();
            else return Echo.fail("数据库操作失败");
        }
        return Echo.fail(map.get("result"));
    }

    @Operation(summary = "获取用户空间背景",description = "目前返回的是路径")
    @RequestMapping(value = "/user/{id}/background",method = RequestMethod.GET)
    public Echo getImgPathByOwner(@PathVariable("id")String id){
        String path= userMapper.getBackgroundById(id);
        if(path==null||path.equals(""))return Echo.fail();
        return Echo.success(path);
    }

    @Operation(summary = "上传用户背景图",description = "用户只能上传自己的背景")
    @RequestMapping(value = "/user/{id}/background",method = RequestMethod.POST)
    //文件上传
    public Echo uploadBack(@RequestParam("filename") MultipartFile file, @PathVariable("id") Long id) throws IOException {
        if(userMapper.getUserById(id)==null)return Echo.fail("用户不存在");
        Map<String,String>map=imageService.uploadImage(file);
        if(map.containsKey("path")){
            if(userMapper.updateBackground(id,map.get("path"))!=0)
                return Echo.success();
            else return Echo.fail("数据库操作失败");
        }
        return Echo.fail(map.get("result"));
    }
}
