package com.games.tap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.tap.domain.UserInfo;
import com.games.tap.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    @Resource
    public UserMapper mUserMapper;

    public List<UserInfo> getAllUserInfo() {
        return mUserMapper.getAllUserInfo();
    }

    public List<UserInfo> getPicByName(String name) {
        return mUserMapper.getPicByName(name);
    }

    public int insertAvatar(UserInfo userInfo) {
        return mUserMapper.insertAvatar(userInfo);
    }

    public int insertBackground(UserInfo userInfo){return mUserMapper.insertBackground(userInfo);}

    public UserInfo getUserById(Long id) {
        return mUserMapper.getUserById(id);
    }

}
