package com.games.tap.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "community")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid", columnDefinition = "bigint(20)")
    private Long cId;

    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "intro", columnDefinition = "varchar(200)")
    private String intro;

    @Column(name = "follower", columnDefinition = "int(11)")
    private Integer follower;

    @Column(name = "post_num", columnDefinition = "int(11)")
    private Integer postNum;
}
