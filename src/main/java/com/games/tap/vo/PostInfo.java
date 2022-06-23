package com.games.tap.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostInfo extends PostBasicInfo{
    private Integer readingNum;
    private Long fid;
    private String forumName;
}