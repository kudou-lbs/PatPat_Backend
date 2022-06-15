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
@Table(name = "user_login")
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @Schema(description = "用户Id",required = true)
    private UserInfo uId;

    @Column(name = "username", columnDefinition = "varchar(40)")
    @Schema(description = "账户名",required = true)
    private String userName;

    @Column(name = "password", columnDefinition = "varchar(30)")
    @Schema(description = "密码",required = true)
    private String password;
}
