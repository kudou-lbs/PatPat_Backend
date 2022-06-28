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
@Table(name = "game")
@Schema(name = "Game",description = "游戏信息")
public class Game implements Item{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long gId;

    @Column(name = "name",columnDefinition = "varchar(50)",nullable = false,unique = true)
    @Schema(description = "游戏名",required = true)
    private String name;

    @Column(name = "score",columnDefinition = "float")
    @Schema(description = "游戏评分")
    private float score;

    @Column(name = "hot",columnDefinition = "int(11)")
    @Schema(description = "游戏热度")
    private Integer hot;

    @Column(name = "url",columnDefinition = "varchar(300)")
    @Schema(description = "游戏官方地址")
    private String url;

    @Column(name = "intro",columnDefinition = "varchar(300)")
    @Schema(description = "游戏介绍")
    private String intro;

    @Column(name = "picture",columnDefinition = "varchar(200)")
    @Schema(description = "游戏背景路径")
    private String picture;

    @Column(name = "icon",columnDefinition = "varchar(200)")
    @Schema(description = "游戏图标路径")
    private String icon;

    @Transient
    private String types;

    @Override
    public String getId() {
        return gId.toString();
    }
}
