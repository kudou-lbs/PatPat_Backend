package com.games.tap.mapper;

import com.games.tap.domain.Post;
import com.games.tap.vo.PostInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper {

    Post getPostByPId(Long pid);

    Long getFIdByPId(Long pid);

    PostInfo getPostInfo(@Param("pid")Long pid,@Param("uid")Long uid);

    int updateReadingNum(Long pid);

    int addReplyNum(Long pid);

    int subReplyNum(Long pid,Integer num);

    int updateLastDate(@Param("pid")Long pid,@Param("lastDate")String lastDate);

    int insertPost(Post post);

    int deleteByPId(Long pid);

}
