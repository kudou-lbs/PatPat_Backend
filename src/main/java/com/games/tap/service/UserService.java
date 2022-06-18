package com.games.tap.service;

import com.games.tap.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    public UserMapper userMapper;

}
