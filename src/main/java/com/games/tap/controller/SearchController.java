package com.games.tap.controller;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.games.tap.domain.Forum;
import com.games.tap.domain.Game;
import com.games.tap.mapper.ConMapper;
import com.games.tap.service.SearchService;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import com.games.tap.util.ToolUtil;
import com.games.tap.vo.SearchedPost;
import com.games.tap.vo.SearchedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@Tag(name = "search", description = "搜索用户、论坛、帖子、游戏")
public class SearchController {

    @Resource
    SearchService service;
    @Resource
    ConMapper conMapper;

    @PassToken
    @Operation(summary = "搜索服务", description = "type有‘user’、‘forum’、‘post’、‘game’四种，key为搜索词，page为第几页，size为数量")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Echo search(String type, String key, Integer offset, Integer pageSize, HttpServletRequest request) throws IOException {
        if (offset == null ^ pageSize == null) return Echo.fail("page和size不能一个空一个不空");
        else if (offset == null) {
            offset = 0;
            pageSize = 10;
        }
        switch (type) {
            case "user": {
                List<Hit<SearchedUser>> list = service.searchUser(key, offset, pageSize, SearchedUser.class);
                if (list == null || list.isEmpty()) return Echo.fail("结果为空");
                Long id = null;
                if(request!=null){
                    String token = request.getHeader("token");
                    if (token != null && !token.equals("")) id = ToolUtil.getIdByToken(token);
                }
                List<SearchedUser> resList = new ArrayList<>();
                List<Boolean> tmp=new ArrayList<>();
                if (id != null) {
                    List<Long> tmp1 = new ArrayList<>();
                    list.forEach(i -> tmp1.add(Objects.requireNonNull(i.source()).getUId()));
                    tmp = conMapper.getLikeList(id, tmp1);
                }
                int num = 0;
                for (Hit<SearchedUser> i : list) {
                    SearchedUser u = i.source();
                    if (i.highlight().get("nickname") != null) {
                        assert u != null;
                        u.setNickname(i.highlight().get("nickname").get(0));
                    }
                    if(id!=null&&tmp.size()>num) Objects.requireNonNull(u).setIsFollow(tmp.get(num++));
                    resList.add(u);
                }
                return Echo.success(resList);
            }
            case "forum": {
                List<Hit<Forum>> list = service.searchForum(key, offset, pageSize);
                if (list == null || list.isEmpty()) return Echo.fail("结果为空");
                List<Forum> resList = new ArrayList<>();
                list.forEach(i -> {
                    Forum f = i.source();
                    if (f != null && i.highlight().get("name") != null) {
                        f.setName(i.highlight().get("name").get(0));
                    }
                    resList.add(f);
                });
                return Echo.success(resList);
            }
            case "post":{
                List<Hit<SearchedPost>> list = service.searchPost(key, offset, pageSize, SearchedPost.class);
                if (list == null || list.isEmpty()) return Echo.fail("结果为空");
                List<SearchedPost> resList = new ArrayList<>();
                list.forEach(i -> {
                    SearchedPost p = i.source();
                    if (p != null) {
                        if (i.highlight().get("title") != null)
                            p.setTitle(i.highlight().get("title").get(0));
                        if (i.highlight().get("forumName") != null)
                            p.setForumName(i.highlight().get("forumName").get(0));
                    }
                    resList.add(p);
                });
                return Echo.success(resList);
            }
            case "game":{
                List<Hit<Game>> list = service.searchGame(key, offset, pageSize);
                if (list == null || list.isEmpty()) return Echo.fail("结果为空");
                List<Game> resList = new ArrayList<>();
                list.forEach(i -> {
                    Game g = i.source();
                    if (g != null && i.highlight().get("name") != null) {
                        g.setName(i.highlight().get("name").get(0));
                    }
                    resList.add(g);
                });
                return Echo.success(resList);
            }
            default:
                return Echo.fail("搜索类型错误");
        }
    }

}
