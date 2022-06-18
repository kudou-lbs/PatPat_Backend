package com.games.tap.mapper;

import com.games.tap.domain.UserInfo;

import java.util.List;

public interface UserMapper {

    List<UserInfo> getAllUser();

    List<UserInfo> getPicByUserName(String name);

    int insertAvatar(UserInfo userInfo);
    int insertBackground(UserInfo userInfo);

    int insertUser(UserInfo user);

    UserInfo getUserById(Long id);

    UserInfo getUserByUserName(String name);

    int deleteUserById(Long id);
    /**改**/
    int updateUser(UserInfo user);
}
