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
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long pId;

    @Column(name = "uid", columnDefinition = "bigint(20)")
    @Schema(description = "用户Id")
    private Long uId;

    @ManyToOne
    @JoinColumn(name = "fid",referencedColumnName = "fid")
    @Schema(description = "论坛Id",required = true)
    private Forum fId;

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

    @Column(name = "reply_num",columnDefinition = "int(11) default 0")
    @Schema(description = "回复数量")
    private Integer replyNum;

    @Column(name = "reading_num",columnDefinition = "int(11) default 1")
    @Schema(description = "阅读数量")
    private Integer readingNum;

    @OneToMany(mappedBy = "pId",cascade = CascadeType.ALL)
    @ToString.Exclude
    @Hidden
    List<Reply>replyList=new ArrayList<>();

    @OneToMany(mappedBy = "pId",cascade = CascadeType.ALL)
    @ToString.Exclude
    @Hidden
    List<PostLike>postLikeList=new ArrayList<>();

}
