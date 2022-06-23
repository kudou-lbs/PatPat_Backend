package com.games.tap.mapper;

import com.games.tap.vo.UserPostInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectMapper {

    Integer postCollection(Long uid,Long pid);

    Integer postCancelCollection(Long uid,Long pid);

    Integer isCollectionExited(Long uid,Long pid);

    Integer addCollectionNum(Long pid);

    Integer subCollectionNum(Long pid);

    List<UserPostInfo> getUserCollectList(@Param("uid")Long uid, @Param("offset")Long offset, @Param("pageSize")Long pageSize);

}
