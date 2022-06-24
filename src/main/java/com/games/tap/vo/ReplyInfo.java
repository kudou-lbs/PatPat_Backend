package com.games.tap.vo;

import lombok.Data;

@Data
public class ReplyInfo {

    private String nickname;
    private String avatar;
    private Integer level;
    private Long rid;
    private Long uid;
    private Integer floorNum;
    private Integer replyNum;
    private Integer likeNum;
    private Boolean isLike;
    private String content;
    private String postTime;

}
