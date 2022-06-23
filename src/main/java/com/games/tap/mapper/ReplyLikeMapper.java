package com.games.tap.mapper;

public interface ReplyLikeMapper {
    Integer replyLike(Long uid,Long rid);

    Integer replyCancelLike(Long uid,Long rid);

    Integer isReplyExited(Long uid,Long rid);

    Integer addReplyLikeNum(Long rid);

    Integer subReplyLikeNum(Long rid);
}
