package com.games.tap.mapper;

import com.games.tap.domain.PostLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostLikeMapper {
    Integer postLike(Long uid,Long pid);
    Integer postIsLiked(Long uid,Long pid);
    Integer postCancelLike(Long uid,Long pid);
    Integer deletePostLikeByPid(Long pid);

    List<Long> getPidByUid(Long uid);
    List<Long> getPidList(Long uid, @Param("offset")Long offset, @Param("pageSize")Long pageSize);

    Integer getPostLikes(Long pid);
}
