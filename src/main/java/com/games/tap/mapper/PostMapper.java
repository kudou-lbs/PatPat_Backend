package com.games.tap.mapper;

import com.games.tap.domain.Post;
import com.games.tap.vo.PostInfo;
import com.games.tap.vo.ReplyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper {

    Post getPostByPId(Long pid);

    Long getFIdByPId(Long pid);

    PostInfo getPostInfo(@Param("pid")Long pid,@Param("uid")Long uid);

    List<ReplyInfo> getPostReplyList(@Param("pid") Long pid, @Param("uid")Long uid,@Param("offset") Long offset,
                                    @Param("pageSize") Long pageSize, @Param("rank") int rank);

    int updateReadingNum(Long pid);

    int addReplyNum(Long pid);

    int subReplyNum(Long pid,Integer num);

    int updateLastDate(@Param("pid")Long pid,@Param("lastDate")String lastDate);

    int insertPost(Post post);

    int deleteByPId(Long pid);

    String getPicById(Long pid);
}
