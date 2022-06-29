package com.games.tap.vo;

import lombok.Data;

@Data
public class UserMessage {
    private Long uid;
    private Long lid;//like
    private Long jid;//rid,pid
    private String nickname;
    private String avatar;
    private String type;
    private String content;
    private String uContent;
    private String uTitle;
    private String postTime;
}
