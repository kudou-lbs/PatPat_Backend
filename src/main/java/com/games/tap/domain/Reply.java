package com.games.tap.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid", columnDefinition = "bigint(20)")
    private Long rId;

    @OneToOne
    @JoinColumn(name = "uid",referencedColumnName = "uid")
    private UserInfo uId;

    @OneToOne
    @JoinColumn(name = "pid",referencedColumnName = "pid")
    private Post pId;

    @Column(name = "content",columnDefinition = "varchar(500)")
    private String content;

    @Column(name = "post_time",columnDefinition = "varchar(50)")
    private String postTime;

    @Column(name = "like_num",columnDefinition = "int(11)")
    private Integer likeNum;

    @Column(name = "reply_num",columnDefinition = "int(11)")
    private Integer replyNum;

    @Column(name = "floor_num",columnDefinition = "int(8)")
    private Integer floorNum;

    @Column(name = "reply_to_uid",columnDefinition = "bigint(20)")
    private Long replyToUid;

    @Column(name = "is_floor",columnDefinition = "int(1)")
    private Integer isFloor;
}
