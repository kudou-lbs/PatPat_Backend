package com.games.tap.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "post_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    private Long id;

    @Column(name = "uid", columnDefinition = "bigint(20)")
    private Long uId;

    @ManyToOne
    @JoinColumn(name = "pid",referencedColumnName = "pid")
    private Post pId;

}
