package com.games.tap.service;

import com.games.tap.domain.UserInfo;
import com.games.tap.domain.UserLogin;
import com.games.tap.mapper.LoginMapper;
import com.games.tap.util.JwtUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Resource
    private LoginMapper mLoginMapper;


    public UserLogin getUserLoginByName(String name){
        return mLoginMapper.getUserLoginByName(name);
    }

    public int update(Integer id,String password){
        return mLoginMapper.update(id,password);
    }

    public int deleteById(Integer id){
        return mLoginMapper.deleteById(id);
    }

    public UserLogin getById(Integer id){
        return mLoginMapper.getById(id);
    };
}
