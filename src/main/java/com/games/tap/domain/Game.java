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
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long gId;

    @Column(name = "name",columnDefinition = "varchar(50)")
    @Schema(description = "游戏名",required = true)
    private String name;

    @Column(name = "score",columnDefinition = "int(4)")
    @Schema(description = "游戏评分")
    private Integer score;

    @Column(name = "hot",columnDefinition = "int(11)")
    @Schema(description = "游戏热度")
    private Integer hot;

    @Column(name = "url",columnDefinition = "varchar(300)")
    @Schema(description = "游戏官方地址")
    private String url;

    @Column(name = "picture",columnDefinition = "varchar(200)")
    @Schema(description = "游戏图标路径")
    private String picture;

}
