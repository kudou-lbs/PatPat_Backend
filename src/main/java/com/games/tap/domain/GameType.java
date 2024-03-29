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
@Table(name = "game_type",uniqueConstraints = @UniqueConstraint(name = "gt_idx",columnNames = {"gid","type"}))
public class GameType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "type",columnDefinition = "varchar(10)")
    @Schema(description = "游戏类型")
    private String type;

    @Column(name = "gid", columnDefinition = "bigint(20)")
    @Schema(description = "游戏Id",required = true)
    private Long gId;
}
