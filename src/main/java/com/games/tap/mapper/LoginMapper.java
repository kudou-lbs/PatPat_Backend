package com.games.tap.mapper;

import com.games.tap.domain.UserLogin;

public interface LoginMapper {
    /**增**/
    int insert(UserLogin user);
    /**删**/
    int deleteById(Integer id);
    /**改**/
    int update(Integer id,String password);
    /**查**/
    UserLogin getById(Integer id);
    /**登录**/
    UserLogin login(String username, String password);

    UserLogin getUserLoginByUId(Long uId);

    UserLogin getUserLoginByName(String userName);
}
