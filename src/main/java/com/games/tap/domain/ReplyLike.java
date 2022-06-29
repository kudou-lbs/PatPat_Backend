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
@Table(name = "reply_like",uniqueConstraints = @UniqueConstraint(name = "rl_idx",columnNames = {"uid","rid"}),indexes = {
        @Index(name = "rlr_idx",columnList = "rid")
})
public class ReplyLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "uid",columnDefinition = "bigint(20)")
    @Schema(description = "用户Id")
    private Long uId;

    @Column(name = "rid",columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "回复Id",required = true)
    private Long rId;

}
