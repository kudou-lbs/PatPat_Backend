package com.games.tap.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserPostInfo extends PostBasicInfo{
    private String forumName;
}
