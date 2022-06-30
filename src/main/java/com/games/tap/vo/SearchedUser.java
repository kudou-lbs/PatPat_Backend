package com.games.tap.vo;

import com.games.tap.domain.Item;
import lombok.Data;

@Data
public class SearchedUser implements Item {
    private String intro;
    private String avatar;
    private String nickname;
    private Integer fansNum;
    private Long uid;
    private Boolean isFollow;

    @Override
    public String getId() {
        return uid.toString();
    }

    public SearchedUser(Long uid,String nickname,String avatar,String intro,int fansNum){
        this.uid=uid;
        this.nickname=nickname;
        this.avatar=avatar;
        this.intro=intro;
        this.fansNum=fansNum;
        this.isFollow=false;
    }

    public SearchedUser(){}
}
