package com.games.tap.controller;

import com.games.tap.domain.ForumUser;
import com.games.tap.domain.Reply;
import com.games.tap.domain.TypeEnum;
import com.games.tap.mapper.ForumUserMapper;
import com.games.tap.mapper.PostMapper;
import com.games.tap.mapper.ReplyMapper;
import com.games.tap.mapper.UserMapper;
import com.games.tap.service.LACService;
import com.games.tap.util.ToolUtil;
import com.games.tap.util.DateUtil;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import com.games.tap.util.RetCode;
import com.games.tap.vo.SubReply;
import com.games.tap.vo.UserReply;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@Tag(name = "reply", description = "回复接口，包含主题贴回复、楼中楼回复的基本操作和点赞功能")
public class ReplyController {

    @Resource
    ReplyMapper replyMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ForumUserMapper forumUserMapper;
    @Resource
    LACService lacService;

    @Operation(summary = "新增回复", description = "前四个参数必带，后三个全为空或全不空，全空为新增主回复，不空为新增楼中楼")
    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public Echo addReply(String uid, String fid, String pid, String content, String replyToUid, String floorNum,String replyToRid) {
        if (uid == null || uid.equals("") || fid == null || fid.equals("") || pid == null || pid.equals("")
                || content == null || content.equals("")) return Echo.define(RetCode.PARAM_IS_EMPTY);
        if ((replyToUid == null || replyToUid.equals("")) ^ (floorNum == null || floorNum.equals("")))
            return Echo.fail("floor和r2id不能一个为空一个不空");
        if (!StringUtils.isNumeric(uid) || !StringUtils.isNumeric(fid) || !StringUtils.isNumeric(pid))
            return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long uId = Long.parseLong(uid), fId = Long.parseLong(fid), pId = Long.parseLong(pid);
        if (userMapper.getUsernameById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (postMapper.getFIdByPId(pId) != fId) return Echo.fail("fid和pid不匹配");
        String ctime = DateUtil.getCurrentTime();
        Reply reply = new Reply(uId, fId, pId, content, ctime);
        if (replyToUid != null && !replyToUid.equals("")) {//新增楼中楼
            if(replyToRid==null||replyToRid.equals(""))return Echo.fail("r2rid不能为空");
            if (!StringUtils.isNumeric(replyToUid) || !StringUtils.isNumeric(floorNum)
            || !StringUtils.isNumeric(replyToRid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            long r2uid = Long.parseLong(replyToUid),r2rid=Long.parseLong(replyToRid);
            if (userMapper.getUsernameById(r2uid) == null) return Echo.fail("回复的用户不存在");
            int floor = Integer.parseInt(floorNum);
            Long floorId = replyMapper.getRIdByFloor(floor, pId);
            if (floorId == null) return Echo.fail("回复楼层不存在");
            reply.setReplyToUid(r2uid);
            reply.setReplyToRid(r2rid);
            reply.setIsFloor(false);
            reply.setFloorNum(floor);
            if (replyMapper.addFloorReplyNum(floorId) == 0) return Echo.fail("更新楼层回复数失败");
        } else {//新增主回复
            reply.setIsFloor(true);
            Integer floor = replyMapper.getMaxFloor(pId);
            if (floor == null) {
                log.info(pid + "回复为空");
                floor = 1;
            }
            reply.setFloorNum(floor + 1);
        }
        if (postMapper.addReplyNum(pId) == 0) return Echo.fail("更新帖子回复数失败");
        if (replyMapper.insertReply(reply) == 0) return Echo.fail("新增回复失败");
        if (postMapper.updateLastDate(pId, ctime) == 0) return Echo.fail("更新帖子最新回复时间失败");
        ForumUser forumUser = forumUserMapper.getForumUser(uId, fId);
        if (forumUser != null) {
            int exp = forumUser.getExp() + 3 * ToolUtil.expRatio(forumUser.getIdentity());
            forumUser.setExp(exp);
            ToolUtil.checkExp(forumUser);
            if (forumUserMapper.updateLevelAndExp(uId, fId, forumUser.getExp(), forumUser.getLevel()) == 0)
                return Echo.fail("更新用户经验值失败");
        }
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "获取子贴回复数据", description = "获取主回复对应的子回复，uid可选，order定义排序，0为最新，1为最早")
    @RequestMapping(value = "/reply", method = RequestMethod.GET)
    public Echo getReply(String rid, String uid, String offset, String pageSize, String order) {
        if (rid == null || rid.equals("")) return Echo.define(RetCode.PARAM_IS_EMPTY);
        Echo echo = ToolUtil.checkList(rid, offset, pageSize);
        if (echo != null) return echo;
        Reply reply = replyMapper.getReplyById(Long.parseLong(rid));
        if (reply == null) return Echo.fail("主回复不存在");
        if (!reply.getIsFloor())
            reply= replyMapper.getReplyByFloor(reply.getFloorNum(),reply.getPId());
        int rank = 0;
        if (order != null) {
            if (!StringUtils.isNumeric(order)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            rank = Integer.parseInt(order);
            if (rank < 0 || rank > 1) return Echo.define(RetCode.PARAM_IS_INVALID);
        }
        Long size = null, start = null, uId = null;
        if (uid != null && !uid.equals("")) {
            if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            uId = Long.parseLong(uid);
            if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        }
        if (offset != null) start = Long.parseLong(offset);
        if (pageSize != null) size = Long.parseLong(pageSize);
        List<SubReply> subReplyList = replyMapper.getSubReplyList(reply.getPId(), uId, reply.getFloorNum(), start, size, rank);
        if (subReplyList == null || subReplyList.isEmpty()) return Echo.fail("子贴为空");
        if(start==null||start==0){
            Map<String, Object>map=new HashMap<>();
            map.put("reply_list",subReplyList);
            map.put("reply_num",reply.getReplyNum());
            map.put("floor_num",reply.getFloorNum());
            map.put("fid",reply.getFId());
            map.put("pid",reply.getPId());
            return Echo.success(map);
        }
        return Echo.success(subReplyList);
    }

    @Operation(summary = "删除回复", description = "通过rid删除回复")
    @RequestMapping(value = "/reply", method = RequestMethod.DELETE)
    public Echo deleteReply(String rid) {
        if (rid == null || rid.equals("")) return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (!StringUtils.isNumeric(rid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Reply reply = replyMapper.getReplyById(Long.parseLong(rid));
        if (reply == null) return Echo.fail("回复Id不存在");
        if (reply.getIsFloor()) {
            if (postMapper.subReplyNum(reply.getPId(), (reply.getReplyNum() + 1)) == 0)
                return Echo.fail("更新帖子回复数失败");
            if (replyMapper.deleteByFloorNum(reply.getFloorNum(), reply.getPId()) == 0) return Echo.fail("删除失败");
        } else {
            if (replyMapper.subFloorReplyNum(reply.getFloorNum(), reply.getPId()) == 0)
                return Echo.fail("更新楼层回复数失败");
            if (postMapper.subReplyNum(reply.getPId(), null) == 0)
                return Echo.fail("更新帖子回复数失败");
            if (replyMapper.deleteById(reply.getRId()) == 0) return Echo.fail("删除失败");
        }
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "回复点赞")
    @RequestMapping(value = "/reply/like", method = RequestMethod.POST)
    public Echo postLike(String uid, String rid) {
        if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if (!StringUtils.isNumeric(rid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long uId = Long.parseLong(uid), rId = Long.parseLong(rid);
        if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (replyMapper.getReplyById(rId) == null) return Echo.fail("回复不存在");
        if (lacService.isExited(uId, rId, TypeEnum.REPLY) != null) return Echo.fail("已经点赞");

        Integer like = lacService.like(uId, rId, TypeEnum.REPLY);
        lacService.add(rId, TypeEnum.REPLY);
        if (like == 0) return Echo.fail("点赞失败");
        return Echo.success("点赞成功");
    }

    @PassToken
    @Operation(summary = "取消回复点赞")
    @RequestMapping(value = "/reply/like", method = RequestMethod.DELETE)
    public Echo postCancelLike(String uid, String rid) {
        if (!StringUtils.isNumeric(uid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if (!StringUtils.isNumeric(rid)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        long uId = Long.parseLong(uid), rId = Long.parseLong(rid);
        if (userMapper.getUserById(uId) == null) return Echo.define(RetCode.USER_NOT_EXIST);
        if (replyMapper.getReplyById(rId) == null) return Echo.fail("回复不存在");
        if (lacService.isExited(uId, rId, TypeEnum.REPLY) == null) return Echo.fail("还没有点赞");

        Integer like = lacService.unlike(uId, rId, TypeEnum.REPLY);
        lacService.sub(rId, TypeEnum.REPLY);
        if (like == 0) return Echo.fail("取消点赞失败");
        return Echo.success("取消点赞成功");
    }
}
