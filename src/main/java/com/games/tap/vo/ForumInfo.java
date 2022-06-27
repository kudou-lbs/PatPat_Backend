package com.games.tap.vo;

import lombok.Data;

@Data
public class ForumInfo {
    private Long fid;
    private String name;
    private String icon;
    private Boolean isLike;
    private Integer followNum;
    private Integer postNum;
}
