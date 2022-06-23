package com.games.tap.mapper;

import com.games.tap.domain.Reply;

public interface ReplyMapper {


    Reply getReplyById(Long rid);

    int deleteById(Long rid);

    int deleteByFloorNum(Integer num);
}
