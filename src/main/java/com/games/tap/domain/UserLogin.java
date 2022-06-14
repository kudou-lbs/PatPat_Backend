package com.games.tap.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_login")
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    private Long id;

    @OneToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private UserInfo userInfo;

    @Column(name = "username", columnDefinition = "varchar(40)")
    private String userName;

    @Column(name = "password", columnDefinition = "varchar(30)")
    private String password;
}
