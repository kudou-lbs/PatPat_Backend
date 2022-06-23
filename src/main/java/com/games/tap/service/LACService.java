package com.games.tap.service;

import com.games.tap.domain.PostLike;
import com.games.tap.mapper.PostLikeMapper;
import com.games.tap.vo.UserPostInfo;
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

    public Integer postCancelLike(Long uid,Long pid){
        return postLikeMapper.postCancelLike(uid,pid);
    }

    public List<UserPostInfo> getUserPostList(Long uid, Long offset, Long pageSize){
        return postLikeMapper.getUserPostList(uid,offset,pageSize);
    }

    public Integer addLikeNum(Long pid){
        return postLikeMapper.addLikeNum(pid);
    }

    public Integer subLikeNum(Long pid){
        return postLikeMapper.subLikeNum(pid);
    }

    public Integer isExited(Long uid,Long pid){
        return postLikeMapper.isExited(uid,pid);
    }
}
