package com.games.tap.vo;

import lombok.Data;

@Data
public class UserInfo {
    private String intro;
    private String avatar;
    private String nickname;
    private Integer fansNum;
    private Long uId;
}
