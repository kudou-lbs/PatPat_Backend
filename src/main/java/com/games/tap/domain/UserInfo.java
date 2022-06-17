package com.games.tap.domain;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_info",uniqueConstraints = {@UniqueConstraint(columnNames = {"uid"})})
public class UserInfo {
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

    @Column(name = "gender",columnDefinition = "int(1)")
    @Schema(description = "性别")
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

}
