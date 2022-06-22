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
@Table(name = "concern")
@Schema(description = "用户间相互关注的信息")
public class Concern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "following_uid",columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "跟随者Id",required = true)
    private Long followingUId;

    @Column(name = "followed_uid",columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "被关注者Id",required = true)
    private Long followedUId;
}
