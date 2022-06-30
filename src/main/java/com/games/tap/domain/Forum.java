package com.games.tap.domain;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "forum",uniqueConstraints = @UniqueConstraint(name = "fn_idx",columnNames = {"name"}),
        indexes = {@Index(name = "ffn_idx",columnList = "follow_num")})
@Schema(description = "论坛信息")
public class Forum implements Item{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid", columnDefinition = "bigint(20)")
    @Schema(description = "主键Id",accessMode = Schema.AccessMode.READ_ONLY)
    private Long fId;

    @Column(name = "name", columnDefinition = "varchar(50)",nullable = false)
    @Schema(description = "论坛名")
    private String name;

    @Column(name = "intro", columnDefinition = "varchar(200)")
    @Schema(description = "论坛简介")
    private String intro;

    @Column(name = "icon",columnDefinition = "varchar(200)")
    @Schema(description = "论坛图标路径")
    private String icon;

    @Column(name = "follow_num", columnDefinition = "int(11) default 0")
    @Schema(description = "论坛用户数量")
    private Integer followNum;

    @Column(name = "post_num", columnDefinition = "int(11) default 0")
    @Schema(description = "帖子数")
    private Integer postNum;

    @Override
    public String getId() {
        return fId.toString();
    }

    public Forum(){};

    public Forum(long fId,String name,String intro,String icon,int followNum,int postNum){
        this.fId=fId;
        this.name=name;
        this.intro=intro;
        this.icon=icon;
        this.followNum=followNum;
        this.postNum=postNum;
    }

}
