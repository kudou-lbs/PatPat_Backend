package com.games.tap.mapper;

import com.games.tap.domain.ForumUser;
import com.games.tap.vo.LikeForum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumUserMapper {

    ForumUser getForumUser(@Param("uid")Long uid, @Param("fid")Long fid);

    List<ForumUser> getLikeForumUser(Long id);

    List<LikeForum>getLikeForum(Long id);

    Boolean isLike(@Param("uid")Long uid,@Param("fid")Long fid);

    int insertForumUser(ForumUser forumUser);

    int likeForum(@Param("uid")Long uid,@Param("fid")Long fid);

    int unlikeForum(@Param("uid")Long uid,@Param("fid")Long fid);

    int updateIdentity(@Param("uid")Long uid,@Param("fid")Long fid,@Param("identity")Integer identity);

    int deleteForumUser(@Param("uid")Long uid,@Param("fid")Long fid);
}
