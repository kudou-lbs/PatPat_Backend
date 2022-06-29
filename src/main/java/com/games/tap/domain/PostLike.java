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
@Table(name = "post_like",uniqueConstraints = @UniqueConstraint(name = "pl_idx",columnNames = {"uid","pid"}),indexes = {
        @Index(name = "plp_idx",columnList = "pid")
})
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "uid", columnDefinition = "bigint(20)")
    @Schema(description = "用户Id")
    private Long uId;

    @Column(name = "pid",columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "帖子Id",required = true)
    private Long pId;

}
