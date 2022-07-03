package com.games.tap.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "forum_user",uniqueConstraints = @UniqueConstraint(name = "fufu_idx",columnNames = {"fid","uid"}),
        indexes = @Index(name = "fuu_idx",columnList = "uid"))
@Schema(description = "论坛的用户信息")
public class ForumUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "level", columnDefinition = "int(3) default 1")
    @Schema(description = "用户论坛等级")
    private Integer level;

    @Column(name = "exp", columnDefinition = "int(7) default 0")
    @Schema(description = "论坛经验值")
    private Integer exp;

    @Column(name = "identity", columnDefinition = "int(2) default 0")
    @Schema(description = "论坛权限，0为普通用户，1为论坛会员，2为管理员，管理帖子，3为版主，管理论坛")
    private Integer identity;

    @Column(name = "is_like", columnDefinition = "tinyint(1) default 0")
    @Schema(description = "是否关注论坛")
    private Boolean isLike;

    @Column(name = "uid", columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "用户Id",required = true)
    private Long uId;

    @Column(name = "fid", columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "论坛Id")
    private Long fId;

    public ForumUser(){}

    public ForumUser(Long uid,Long fid,Boolean isLike,Integer identity){
        this.uId=uid;
        this.fId=fid;
        this.isLike=isLike;
        this.identity=identity;
    }

}
