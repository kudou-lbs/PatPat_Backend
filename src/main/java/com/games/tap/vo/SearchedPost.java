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

    public SearchedPost(Long pId, int likeNum, int replyNum, String title,
                        String content, String forumName, String picture) {
        this.pId = pId;
        this.likeNum = likeNum;
        this.replyNum = replyNum;
        this.title = title;
        this.forumName = forumName;
        this.picture = picture;
        this.content = content;
    }

    public SearchedPost(){}
}
