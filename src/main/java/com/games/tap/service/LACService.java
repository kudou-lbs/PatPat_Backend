package com.games.tap.service;

import com.games.tap.domain.TypeEnum;
import com.games.tap.mapper.CollectMapper;
import com.games.tap.mapper.PostLikeMapper;
import com.games.tap.mapper.ReplyLikeMapper;
import com.games.tap.vo.UserPostInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LACService {
    @Resource
    PostLikeMapper postLikeMapper;
    @Resource
    CollectMapper collectMapper;
    @Resource
    ReplyLikeMapper replyLikeMapper;

    public Integer like(Long uid, Long id, TypeEnum type) {
        switch (type) {
            case POST:
                return postLikeMapper.postLike(uid, id);
            case REPLY:
                return replyLikeMapper.replyLike(uid, id);
            case COLLECT:
                return collectMapper.postCollection(uid, id);
            default:
                return null;
        }
    }

    public Integer unlike(Long uid, Long id, TypeEnum type) {
        switch (type) {
            case POST:
                return postLikeMapper.postCancelLike(uid, id);
            case REPLY:
                return replyLikeMapper.replyCancelLike(uid, id);
            case COLLECT:
                return collectMapper.postCancelCollection(uid, id);
            default:
                return null;
        }
    }

    public Integer isExited(Long uid, Long id, TypeEnum type) {
        switch (type) {
            case POST:
                return postLikeMapper.isPostExited(uid, id);
            case REPLY:
                return replyLikeMapper.isReplyExited(uid, id);
            case COLLECT:
                return collectMapper.isCollectionExited(uid, id);
            default:
                return null;
        }
    }

    public Integer add(Long id, TypeEnum type) {
        switch (type) {
            case POST:
                return postLikeMapper.addLikeNum(id);
            case REPLY:
                return replyLikeMapper.addReplyLikeNum(id);
            case COLLECT:
                return collectMapper.addCollectionNum(id);
            default:
                return null;
        }
    }

    public Integer sub(Long id, TypeEnum type) {
        switch (type) {
            case POST:
                return postLikeMapper.subLikeNum(id);
            case REPLY:
                return replyLikeMapper.subReplyLikeNum(id);
            case COLLECT:
                return collectMapper.subCollectionNum(id);
            default:
                return null;
        }
    }

    public List<UserPostInfo> getUserPostList(Long uid, Long offset, Long pageSize) {
        return postLikeMapper.getUserLikePostList(uid, offset, pageSize);
    }

    public List<UserPostInfo> getUserCollectList(Long uid, Long offset, Long pageSize) {
        return collectMapper.getUserCollectList(uid, offset, pageSize);
    }
}

