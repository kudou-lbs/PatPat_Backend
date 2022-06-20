package com.games.tap.mapper;

import com.games.tap.domain.Concern;
import com.games.tap.vo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConMapper {

    List<UserInfo> getPartFanList(@Param("id")Long id,@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    List<UserInfo> getPartFollowList(@Param("id")Long id,@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    List<UserInfo> getFanList(Long id);

    List<UserInfo> getFollowList(Long id);

    int follow(@Param("following")Long following,@Param("followed")Long followed);

    int unfollow(@Param("following")Long following,@Param("followed")Long followed);

    Concern isFollow(@Param("following")Long following,@Param("followed")Long followed);

}
