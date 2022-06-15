package com.games.tap.domain;

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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_uid",referencedColumnName = "uid")
    private UserInfo followingUId;

    @ManyToOne
    @JoinColumn(name = "followed_uid",referencedColumnName = "uid")
    private UserInfo followedUId;
}
