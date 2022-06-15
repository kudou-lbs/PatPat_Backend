package com.games.tap.domain;

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
    private Long pId;

    @Column(name = "uid", columnDefinition = "bigint(20)")
    private Long uId;

    @ManyToOne
    @JoinColumn(name = "cid",referencedColumnName = "cid")
    private Community cId;

    @Column(name = "title",columnDefinition = "varchar(100)")
    private String title;

    @Column(name = "content",columnDefinition = "varchar(500)")
    private String content;

    @Column(name = "post_time",columnDefinition = "varchar(50)")
    private String postTime;

    @Column(name = "last_date",columnDefinition = "varchar(50)")
    private String lastDate;

    @Column(name = "like_num",columnDefinition = "int(11)")
    private Integer likeNum;

    @Column(name = "reply_num",columnDefinition = "int(11)")
    private Integer replyNum;

    @Column(name = "reading_num",columnDefinition = "int(11)")
    private Integer readingNum;

    @OneToMany(mappedBy = "pId",cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Reply>replyList=new ArrayList<>();

    @OneToMany(mappedBy = "pId",cascade = CascadeType.ALL)
    @ToString.Exclude
    List<PostLike>postLikeList=new ArrayList<>();

}
