package com.games.tap.service;

import com.games.tap.domain.UserInfo;
import com.games.tap.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    public UserMapper mUserMapper;

    public List<UserInfo> getAllUserInfo(){
        return mUserMapper.getAllUserInfo();
    }

    public List<UserInfo> getPicByName(String name) {
        return mUserMapper.getPicByName(name);
    }

    public int insertPic(UserInfo userInfo) {
        return mUserMapper.insertPic(userInfo);
    }
}
