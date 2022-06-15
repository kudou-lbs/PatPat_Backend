package com.games.tap.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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

    @OneToOne
    @JoinColumn(name = "uid",referencedColumnName = "uid")
    private UserInfo uId;

    @OneToOne
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

}
