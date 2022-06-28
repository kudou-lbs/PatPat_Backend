package com.games.tap.vo;

import lombok.Data;

@Data
public class UserForumInfo {
    private Long fid;
    private String name;
    private String icon;
    private String intro;
    private Integer followNum;
    private Integer postNum;
    private Integer level;
    private Integer exp;
    private Integer maxExp;
    private Boolean isLike;
}
