package com.games.tap.domain;

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
    private Long gId;

    @Column(name = "name",columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "score",columnDefinition = "int(4)")
    private Integer score;

    @Column(name = "hot",columnDefinition = "int(11)")
    private Integer hot;

    @Column(name = "url",columnDefinition = "varchar(300)")
    private String url;

    //TODO implement picture

}
