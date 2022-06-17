package com.games.tap.mapper;

import com.games.tap.domain.UserInfo;
import com.games.tap.domain.UserLogin;


public interface LoginMapper {
    UserLogin getUserLoginsByUId(Long uId);

    UserLogin getUserLoginByName(String userName);
}
