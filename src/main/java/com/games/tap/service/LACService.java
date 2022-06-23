package com.games.tap.service;

import com.games.tap.domain.PostLike;
import com.games.tap.mapper.PostLikeMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LACService {
    @Resource
    PostLikeMapper postLikeMapper;
    /**
     * 帖子点赞相关
     */
    public Integer postLike(Long uid, Long pid){
        return postLikeMapper.postLike(uid, pid);
    }

    public Integer postIsLiked(Long uid,Long pid){
        return postLikeMapper.postIsLiked(uid,pid);
    }

    public Integer postCancelLike(Long uid,Long pid){
        return postLikeMapper.postCancelLike(uid,pid);
    }

    public Integer deletePostLikeByPid(Long pid){
        return postLikeMapper.deletePostLikeByPid(pid);
    }

    public List<Long> getPidByUid(Long uid){
        return postLikeMapper.getPidByUid(uid);
    }

    public List<Long> getPidList(Long uid, Long offset, Long pageSize){
        return postLikeMapper.getPidList(uid, offset, pageSize);
    }

    public Integer getPostLikes(Long pid){
        return postLikeMapper.getPostLikes(pid);
    }
}
