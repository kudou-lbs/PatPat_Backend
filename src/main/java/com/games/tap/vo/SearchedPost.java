package com.games.tap.vo;

import com.games.tap.domain.Item;
import lombok.Data;

@Data
public class SearchedPost implements Item {
    private Long pId;
    private Integer likeNum;
    private Integer replyNum;
    private String title;
    private String forumName;
    private String content;
    private String picture;

    @Override
    public String getId() {
        return pId.toString();
    }
}
