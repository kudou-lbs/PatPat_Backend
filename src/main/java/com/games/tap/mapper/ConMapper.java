package com.games.tap.mapper;

import com.games.tap.domain.Concern;
import com.games.tap.vo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConMapper {

    List<UserInfo> getFanList(@Param("id")Long id,@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    List<UserInfo> getFollowList(@Param("id")Long id,@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    List<Boolean>getLikeList(@Param("uid")Long id,@Param("list")List<Long>list);

    int follow(@Param("following")Long following,@Param("followed")Long followed);
    // FIXME:这两个写了多个SQL语句，可能有问题
    int unfollow(@Param("following")Long following,@Param("followed")Long followed);

    Concern isFollow(@Param("following")Long following,@Param("followed")Long followed);

}
