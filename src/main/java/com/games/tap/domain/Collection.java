package com.games.tap.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "collection")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uid",referencedColumnName = "uid")
    private UserInfo uId;

    @Column(name = "pid", columnDefinition = "bigint(20)")
    private Long pId;

}
