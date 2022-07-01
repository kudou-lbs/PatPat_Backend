package com.games.tap.mapper;

import com.games.tap.domain.Forum;
import com.games.tap.vo.ForumInfo;
import com.games.tap.vo.SearchedPost;
import com.games.tap.vo.UserForumInfo;
import com.games.tap.vo.PostBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumMapper {

    List<Forum> getAllForum();

    List<ForumInfo> getForumList(@Param("offset") Long offset, @Param("pageSize") Long pageSize);

    List<ForumInfo> getForumListWithUserId(@Param("uid") Long id, @Param("offset") Long offset, @Param("pageSize") Long pageSize);

    List<PostBasicInfo> getForumPostList(@Param("fid") Long fid, @Param("uid") Long uid, @Param("offset") Long offset,
                                         @Param("pageSize") Long pageSize, @Param("rank") int rank);

    List<String> getPicsByFId(Long fid);

    Forum getForumByName(String name);

    UserForumInfo getForumInfo(@Param("fid") Long fid, @Param("uid") Long uid);

    Forum getForumById(Long id);

    String getForumNameById(Long id);

    List<Long> getPIdListByFId(Long fid);

    List<SearchedPost>getSearchPostListById(Long fid);

    int insertForum(Forum forum);

    int addPostNum(Long fid);

    int subPostNum(Long fid);

    int updateForum(Forum forum);

    int deleteForumById(Long id);

    String selectIconById(Long fid);
}
