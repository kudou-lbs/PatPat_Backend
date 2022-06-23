package com.games.tap.mapper;

import com.games.tap.domain.Reply;
import org.apache.ibatis.annotations.Param;

public interface ReplyMapper {


    Reply getReplyById(Long rid);

    Long getRIdByFloor(@Param("floor")Integer floor,@Param("pid")Long pid);

    Integer getMaxFloor(Long pid);

    int subFloorReplyNum(@Param("floor")Integer floor,@Param("pid")Long pid);

    int addFloorReplyNum(Long rid);

    int insertReply(Reply reply);

    int deleteById(Long rid);

    int deleteByFloorNum(@Param("floor")Integer floor,@Param("pid")Long pid);

    Integer replyLike(Long uid,Long rid);

    Integer replyCancelLike(Long uid,Long rid);

    Integer isReplyExited(Long uid,Long rid);

    Integer addReplyLikeNum(Long rid);
    Integer subReplyLikeNum(Long rid);
}
