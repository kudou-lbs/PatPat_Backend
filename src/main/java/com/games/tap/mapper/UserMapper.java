package com.games.tap.mapper;

import com.games.tap.domain.UserInfo;

import java.util.List;

public interface UserMapper {

    List<UserInfo> getAllUserInfo();

    List<UserInfo> getPicByName(String name);

    int insertAvatar(UserInfo userInfo);
    int insertBackground(UserInfo userInfo);

    UserInfo getUserById(Long id);
}
