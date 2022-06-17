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
@Table(name = "forum_user")
public class ForumUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "level", columnDefinition = "int(3)")
    @Schema(description = "用户论坛等级")
    private Integer level;

    @Column(name = "exp", columnDefinition = "int(7)")
    @Schema(description = "论坛经验值")
    private Integer exp;

    @Column(name = "identity", columnDefinition = "int(2)")
    @Schema(description = "论坛权限")
    private Integer identity;

    @Column(name = "uid", columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "用户Id",required = true)
    private Long uId;

    @Column(name = "fid", columnDefinition = "bigint(20)")
    @Schema(description = "论坛Id")
    private Long fId;

}
