package com.games.tap.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "forum_user")
public class ForumUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    private Long id;

    @Column(name = "level", columnDefinition = "int(3)")
    private Integer level;

    @Column(name = "exp", columnDefinition = "int(7)")
    private Integer exp;

    @Column(name = "identity", columnDefinition = "int(2)")
    private Integer identity;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private UserInfo uId;

    @Column(name = "cid", columnDefinition = "bigint(20)")
    private Long cId;

}
