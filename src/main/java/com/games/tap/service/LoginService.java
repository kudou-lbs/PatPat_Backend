package com.games.tap.service;

import com.games.tap.domain.UserInfo;
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

    public Map<String, Object> login(UserInfo user) {

        UserInfo selectUser = new UserInfo();
        //将userId存入token中
        String token = JwtUtil.createToken(selectUser);
        Map<String, Object> map = new HashMap<>();
        map.put("user", selectUser);
        map.put("token", token);
        return map;
    }

    public int update(Integer id){
        return mLoginMapper.update(id);
    }

    public void deleteById(Integer id){
        mLoginMapper.deleteById(id);
    };
}
