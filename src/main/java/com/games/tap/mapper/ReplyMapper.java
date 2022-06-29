package com.games.tap.mapper;

import com.games.tap.domain.Reply;
import com.games.tap.vo.SubReply;
import com.games.tap.vo.UserReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReplyMapper {


    Reply getReplyById(Long rid);

    Reply getReplyByFloor(@Param("floor")Integer floor,@Param("pid")Long pid);

    Long getRIdByFloor(@Param("floor")Integer floor,@Param("pid")Long pid);

    Integer getMaxFloor(Long pid);

    List<SubReply> getSubReplyList(@Param("pid") Long pid,@Param("uid")Long uid,
                                   @Param("floor")Integer floor, @Param("offset") Long offset,
                                   @Param("pageSize") Long pageSize,@Param("rank") int rank);

    int subFloorReplyNum(@Param("floor")Integer floor,@Param("pid")Long pid);

    int addFloorReplyNum(Long rid);

    int insertReply(Reply reply);

    int deleteById(Long rid);

    int deleteByFloorNum(@Param("floor")Integer floor,@Param("pid")Long pid);
}
