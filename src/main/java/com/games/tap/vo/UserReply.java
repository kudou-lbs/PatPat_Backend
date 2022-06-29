package com.games.tap.vo;

import lombok.Data;

@Data
public class UserReply {
    private String nickname;
    private String avatar;
    private Integer level;
    private Long rid;
    private Long fid;
    private Long r2uid;
    private Integer likeNum;
    private Integer replyNum;
    private Integer floorNum;
    private Boolean isLike;
    private String content;
    private String r2title;
    private String r2content;
    private String forumName;
    private String postTime;
    private String r2name;
}
