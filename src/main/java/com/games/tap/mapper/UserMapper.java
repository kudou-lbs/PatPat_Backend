package com.games.tap.mapper;

import com.games.tap.domain.User;
import com.games.tap.vo.UserInfo;
import com.games.tap.vo.UserPostInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    List<User> getAllUser();

    List<UserInfo> getUserList(@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    List<UserPostInfo> getUserPostList(@Param("uid")Long uid, @Param("offset")Long offset,
                                       @Param("pageSize")Long pageSize, @Param("rank")Integer rank);

    User getUserById(Long id);

    User getUserByUserName(String name);

    Long getIdByUserName(String name);

    String getUsernameById(Long id);

    String getUserAvatarById(Long id);

    String getBackgroundById(Long id);

    String getPasswordById(Long id);

    int updateUserAvatar(@Param("uId")Long uid,@Param("avatar")String avatar);

    int updatePassword(@Param("uId") Long uid, @Param("password")String password);

    int updateBackground(@Param("uId") Long uid, @Param("background")String background);

    int updateUser(User user);

    int insertUser(User user);

    int deleteUserById(Long id);

    Integer postCollection(Long uid,Long pid);

    Integer postCancelCollection(Long uid,Long pid);

    Integer isCollectionExited(Long uid,Long pid);

    Integer addCollectionNum(Long pid);
    Integer subCollectionNum(Long pid);

    List<UserPostInfo> getUserCollectList(@Param("uid")Long uid, @Param("offset")Long offset, @Param("pageSize")Long pageSize);
}
