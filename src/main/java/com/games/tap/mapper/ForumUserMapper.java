package com.games.tap.mapper;

import com.games.tap.domain.ForumUser;
import com.games.tap.vo.LikeForum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumUserMapper {

    ForumUser getForumUser(@Param("uid")Long uid, @Param("fid")Long fid);

    List<ForumUser> getLikeForumUser(Long id);

    List<LikeForum>getLikeForum(@Param("id")Long id,@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    Boolean isLike(@Param("uid")Long uid,@Param("fid")Long fid);

    int insertForumUser(ForumUser forumUser);

    int likeForum(@Param("uid")Long uid,@Param("fid")Long fid);

    int unlikeForum(@Param("uid")Long uid,@Param("fid")Long fid);

    int updateLevelAndExp(@Param("uid")Long uid,@Param("fid")Long fid,@Param("exp")Integer exp,@Param("level")Integer level);

    int updateIdentity(@Param("uid")Long uid,@Param("fid")Long fid,@Param("identity")Integer identity);

    int deleteForumUser(@Param("uid")Long uid,@Param("fid")Long fid);
}
