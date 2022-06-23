package com.games.tap.service;

import com.games.tap.domain.PostLike;
import com.games.tap.mapper.PostLikeMapper;
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

    public Integer postCancelLike(Long uid,Long pid){
        return postLikeMapper.postCancelLike(uid,pid);
    }
}
