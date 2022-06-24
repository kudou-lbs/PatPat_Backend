package com.games.tap.vo;

import lombok.Data;

@Data
public class PostBasicInfo {
    private Long pid;
    private Long uid;
    private String nickname;
    private String avatar;
    private Integer level;
    private String postTime;
    private Integer likeNum;
    private Boolean isLike;
    private Integer replyNum;
    private String title;
    private String content;
    private String picture;
}
