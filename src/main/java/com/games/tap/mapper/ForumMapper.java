package com.games.tap.mapper;

import com.games.tap.domain.Forum;
import com.games.tap.vo.ForumInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumMapper {

    List<Forum> getAllForum();

    List<ForumInfo> getForumList(@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    List<ForumInfo> getForumListWithUserId(@Param("uid")Long id,@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    Forum getForumByName(String name);

    Forum getForumById(Long id);

    int insertForum(Forum forum);

    int updateForum(Forum forum);

    int deleteForumById(Long id);
}
