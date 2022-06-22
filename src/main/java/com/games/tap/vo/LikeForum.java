package com.games.tap.vo;

import lombok.Data;

@Data
public class LikeForum {
    private Long fid;
    private String name;
    private String icon;
    private Integer level;
    private String lastTitle;
}
