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
@Table(name = "reply",indexes = {
        @Index(name = "rpf_idx",columnList = "pid"),
        @Index(name="rpf_idx",columnList = "floor_num"),
        @Index(name = "rpf_idx",columnList = "post_time"),
        @Index(name = "ru_idx",columnList = "uId"),
        @Index(name = "rr2u_idx",columnList = "reply_to_uid")
})
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long rId;

    @Column(name = "uid", columnDefinition = "bigint(20)")
    @Schema(description = "用户Id")
    private Long uId;

    @Column(name = "fid", columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "论坛Id",required = true)
    private Long fId;

    @Column(name = "pid",columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "帖子Id",required = true)
    private Long pId;

    @Column(name = "content",columnDefinition = "varchar(500)")
    @Schema(description = "发布内容")
    private String content;

    @Column(name = "post_time",columnDefinition = "varchar(50)")
    @Schema(description = "发布时间")
    private String postTime;

    @Column(name = "like_num",columnDefinition = "int(11) default 0")
    @Schema(description = "点赞数量")
    private Integer likeNum;

    @Column(name = "reply_num",columnDefinition = "int(11) default 0")
    @Schema(description = "回复数量")
    private Integer replyNum;

    @Column(name = "floor_num",columnDefinition = "int(8) default 1")
    @Schema(description = "所属楼层")
    private Integer floorNum;

    @Column(name = "reply_to_uid",columnDefinition = "bigint(20)")
    @Schema(description = "被回复者的用户Id")
    private Long replyToUid;

    @Column(name = "reply_to_rid",columnDefinition = "bigint(20)")
    @Schema(description = "被回复者的帖子Id")
    private Long replyToRid;

    @Column(name = "is_floor",columnDefinition = "tinyint(1)")
    @Schema(description = "是否是子贴（否则为楼中楼回复）")
    private Boolean isFloor;

    @Transient
    private String replyToUserName;

    public Reply(){}

    public Reply(Long uid,Long fid,Long pid,String content,String time){
        this.uId=uid;
        this.fId=fid;
        this.pId=pid;
        this.content=content;
        this.postTime=time;
    }

}
