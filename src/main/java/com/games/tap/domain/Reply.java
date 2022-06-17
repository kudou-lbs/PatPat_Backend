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
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long rId;

    @Column(name = "uid", columnDefinition = "bigint(20)")
    @Schema(description = "用户Id")
    private Long uId;

    @Column(name = "fid", columnDefinition = "bigint(20)")
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

    @Column(name = "is_floor",columnDefinition = "int(1)")
    @Schema(description = "是否是子贴（否则为楼中楼回复）")
    private Integer isFloor;

}
