package com.games.tap.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Blob;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_info",uniqueConstraints = {@UniqueConstraint(columnNames = {"uid"})})
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", columnDefinition = "bigint(20)")
    private Long uId;

    @Column(name = "nickname",columnDefinition = "varchar(30)")
    private String nickname;

    @Column(name = "intro",columnDefinition = "varchar(200)")
    private String intro;

    @Column(name = "gender",columnDefinition = "int(1)")
    private Integer gender;

    @Column(name = "regis_time",columnDefinition = "varchar(25)")
    private String registerTime;

    @Column(name = "fans_num",columnDefinition = "int(11)")
    private Integer fansNum;

    @Column(name = "follow_num",columnDefinition = "int(11)")
    private Integer followNum;

}
