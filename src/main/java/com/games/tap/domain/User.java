package com.games.tap.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user",uniqueConstraints = {@UniqueConstraint(name = "uun_idx",columnNames = {"username"})},
        indexes = @Index(name = "ufn_idx",columnList = "fans_num"))
@Schema(description = "用户信息")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long uId;

    @Column(name = "nickname",columnDefinition = "varchar(30)")
    @Schema(description = "用户昵称")
    private String nickname;

    @Column(name = "intro",columnDefinition = "varchar(200)")
    @Schema(description = "自我介绍")
    private String intro;

    @Column(name = "gender",columnDefinition = "int(1) default 0")
    @Schema(description = "性别:保密 0、男 1、女 2")
    private Integer gender;

    @Column(name = "register_time",columnDefinition = "varchar(25)")
    @Schema(description = "注册时间")
    private String registerTime;

    @Column(name = "fans_num",columnDefinition = "int(11) default 0")
    @Schema(description = "粉丝数")
    private Integer fansNum;

    @Column(name = "follow_num",columnDefinition = "int(11) default 0")
    @Schema(description = "关注数")
    private Integer followNum;

    @Column(name = "avatar",columnDefinition = "varchar(200)")
    @Schema(description = "头像路径")
    private String avatar;

    @Column(name = "background",columnDefinition = "varchar(200)")
    @Schema(description = "背景图路径")
    private String background;

    @Column(name = "username", columnDefinition = "varchar(40) ",nullable = false)
    @Schema(description = "账户名",required = true)
    private String username;

    @Column(name = "password", columnDefinition = "varchar(100)",nullable = false)
    @Schema(description = "密码",required = true)
    private String password;

    public User(){}
    public User(String userName, String password, String nickname, Integer gender) {
        this.username=userName;
        this.password=password;
        this.nickname=nickname;
        this.gender=gender;
    }
    public User(String userName, String password, String nickname) {
        this.username=userName;
        this.password=password;
        this.nickname=nickname;
    }
    public User(String userName, String password){
        this.username=userName;
        this.password=password;
    }

    public User(Long uid,String nickname,String intro){
        this.uId=uid;
        this.nickname=nickname;
        this.intro=intro;
    }
}
