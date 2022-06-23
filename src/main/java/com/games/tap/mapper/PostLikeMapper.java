package com.games.tap.mapper;

import com.games.tap.domain.PostLike;

import java.util.List;

public interface PostLikeMapper {
    Integer postLike(Long uid,Long pid);
    Integer postCancelLike(Long uid,Long pid);
}
