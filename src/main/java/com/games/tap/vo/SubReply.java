package com.games.tap.vo;

import lombok.Data;

@Data
public class SubReply {

    private String nickname;
    private String avatar;
    private Integer level;
    private Long rid;
    private Long uid;
    private Long r2id;
    private Integer likeNum;
    private Boolean isLike;
    private String content;
    private String postTime;
    private String r2name;
}
