package com.games.tap.mapper;

import com.games.tap.domain.UserInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    List<UserInfo> getAllUserInfo();
}
