package com.games.tap.domain;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "forum")
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long fId;

    @Column(name = "name", columnDefinition = "varchar(50)")
    @Schema(description = "论坛名")
    private String name;

    @Column(name = "intro", columnDefinition = "varchar(200)")
    @Schema(description = "论坛简介")
    private String intro;

    @Column(name = "follower", columnDefinition = "int(11) default 0")
    @Schema(description = "论坛用户数量")
    private Integer follower;

    @Column(name = "post_num", columnDefinition = "int(11) default 0")
    @Schema(description = "帖子数")
    private Integer postNum;

    @OneToMany(mappedBy = "fId",cascade = CascadeType.ALL)
    @ToString.Exclude
    @Hidden
    List<Post>postList=new ArrayList<>();
}