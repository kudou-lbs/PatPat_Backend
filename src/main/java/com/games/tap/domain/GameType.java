package com.games.tap.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "game_type")
public class GameType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    private Long id;

    @Column(name = "type",columnDefinition = "int(3)")
    private TypeEnum type;

    @ManyToOne
    @JoinColumn(name = "gid",referencedColumnName = "gid")
    private Game gId;
}
