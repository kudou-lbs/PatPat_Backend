package com.games.tap.domain;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "post",indexes = {
        @Index(name ="fl_idx",columnList = "fid"),
        @Index(name = "fl_idx",columnList ="last_date"),
        @Index(name = "pu_idx",columnList = "uid"),
        @Index(name = "prl_idx",columnList = "reading_num"),
        @Index(name = "prl_idx",columnList = "like_num")
})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long pId;

    @Column(name = "uid", columnDefinition = "bigint(20)")
    @Schema(description = "用户Id")
    private Long uId;

    @Column(name = "fid",columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "论坛Id",required = true)
    private Long fId;

    @Column(name = "title",columnDefinition = "varchar(100)")
    @Schema(description = "帖子标题")
    private String title;

    @Column(name = "content",columnDefinition = "varchar(500)")
    @Schema(description = "发布内容")
    private String content;

    @Column(name = "post_time",columnDefinition = "varchar(50)")
    @Schema(description = "发布时间")
    private String postTime;

    @Column(name = "last_date",columnDefinition = "varchar(50)")
    @Schema(description = "最后回复时间")
    private String lastDate;

    @Column(name = "like_num",columnDefinition = "int(11) default 0")
    @Schema(description = "点赞数量")
    private Integer likeNum;

    @Column(name = "collect_num",columnDefinition = "int(11) default 0")
    @Schema(description = "收藏数量")
    private Integer collectNum;

    @Column(name = "picture",columnDefinition = "varchar(200)")
    @Schema(description = "帖子图片路径")
    private String picture;

    @Column(name = "reply_num",columnDefinition = "int(11) default 0")
    @Schema(description = "回复数量")
    private Integer replyNum;

    @Column(name = "reading_num",columnDefinition = "int(11) default 1")
    @Schema(description = "阅读数量")
    private Integer readingNum;

    public Post(){}

    public Post(Long uid,Long fid,String title){
        this.uId=uid;
        this.fId=fid;
        this.title=title;
    }
}
