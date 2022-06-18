package com.games.tap.service;

import com.games.tap.domain.UserInfo;
import com.games.tap.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    public UserMapper userMapper;

    public List<UserInfo> getAllUser() {
        return userMapper.getAllUser();
    }

    public List<UserInfo> getPicByUserName(String name) {
        return userMapper.getPicByUserName(name);
    }

    public int insertAvatar(UserInfo userInfo) {
        return userMapper.insertAvatar(userInfo);
    }

    public int insertBackground(UserInfo userInfo) {
        return userMapper.insertBackground(userInfo);
    }

    public int insertUser(UserInfo user){
        return userMapper.insertUser(user);
    }

    public UserInfo getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    public UserInfo getUserByUserName(String username){
        return userMapper.getUserByUserName(username);
    }

    public int updateUser(UserInfo user){
        return userMapper.updateUser(user);
    }

    public int deleteUserById(Long id){
        return userMapper.deleteUserById(id);
    }
}
