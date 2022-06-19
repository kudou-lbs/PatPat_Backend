package com.games.tap.mapper;

import com.games.tap.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    List<User> getAllUser();

    User getUserById(Long id);

    User getUserByUserName(String name);

    String getUserAvatarById(Long id);

    String getBackgroundById(Long id);

    String getPasswordById(Long id);

    int updateUserAvatar(@Param("uId")Long uid,@Param("avatar")String avatar);

    int updatePassword(@Param("uId") Long uid, @Param("password")String password);

    int updateBackground(@Param("uId") Long uid, @Param("background")String background);

    int updateUser(User user);

    int insertUser(User user);

    int deleteUserById(Long id);
}
