package com.games.tap.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchedUser extends UserInfo{
    private Boolean isFollow;
}
