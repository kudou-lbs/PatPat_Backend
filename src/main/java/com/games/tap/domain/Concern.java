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
public class Concern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_uid",referencedColumnName = "uid")
    @Schema(description = "跟随者Id",required = true)
    private UserInfo followingUId;

    @ManyToOne
    @JoinColumn(name = "followed_uid",referencedColumnName = "uid")
    @Schema(description = "被关注者Id",required = true)
    private UserInfo followedUId;
}
