package com.games.tap.mapper;

import com.games.tap.domain.PostLike;
import com.games.tap.vo.UserPostInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostLikeMapper {
    Integer postLike(Long uid,Long pid);

    Integer postCancelLike(Long uid,Long pid);

    Integer isPostExited(Long uid, Long pid);

    Integer getPostLikes(Long pid);

    List<UserPostInfo> getUserLikePostList(@Param("uid")Long uid, @Param("offset")Long offset, @Param("pageSize")Long pageSize);

    Integer addLikeNum(Long pid);

    Integer subLikeNum(Long pid);
}
