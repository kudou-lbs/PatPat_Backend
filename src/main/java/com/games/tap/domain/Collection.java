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
@Table(name = "collection",uniqueConstraints = @UniqueConstraint(name = "cup_idx",columnNames = {"uid","pid"}))
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "uid",columnDefinition = "bigint(20)",nullable = false)
    @Schema(description = "用户Id",required = true)
    private Long uId;

    @Column(name = "pid", columnDefinition = "bigint(20)")
    @Schema(description = "帖子Id")
    private Long pId;

}
