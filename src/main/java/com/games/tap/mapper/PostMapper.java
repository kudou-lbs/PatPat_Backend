package com.games.tap.mapper;

import com.games.tap.domain.Post;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper {

    Post getPostByPId(Long pid);

    List<Post> getUserPostList(@Param("uid")Long uid,@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    int updateReadingNum(Long pid);

    int updateLastDate(Long pid);

    int insertPost(Post post);

    int deleteByPId(Long pid);

}
