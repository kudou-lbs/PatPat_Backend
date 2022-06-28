package com.games.tap.controller;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.games.tap.domain.Forum;
import com.games.tap.domain.User;
import com.games.tap.service.SearchService;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import com.games.tap.vo.SearchedPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "search", description = "搜索用户、论坛、帖子、游戏")
public class SearchController {

    @Resource
    SearchService service;

    @PassToken
    @Operation(summary = "搜索服务", description = "type有‘user’、‘forum’、‘post’、‘game’四种，key为搜索词，page为第几页，size为数量")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Echo search(String type, String key, Integer page, Integer size) throws IOException {
        if (page == null ^ size == null) return Echo.fail("page和size不能一个空一个不空");
        else if (page == null) {
            page = 0;
            size = 10;
        }
        switch (type) {
            case "user": {
                List<Hit<User>> list = service.searchUser(key, page, size);
                if (list == null || list.isEmpty()) return Echo.fail("结果为空");
                List<User> resList = new ArrayList<>();
                list.forEach(i -> {
                    User u = i.source();
                    if (i.highlight().get("nickname") != null) {
                        assert u != null;
                        u.setNickname(i.highlight().get("nickname").get(0));
                    }
                    resList.add(u);
                });
                return Echo.success(resList);
            }
            case "forum": {
                List<Hit<Forum>> list = service.searchForum(key, page, size);
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
            case "post":
                List<Hit<SearchedPost>> list = service.searchPost(key, page, size, SearchedPost.class);
                if (list == null || list.isEmpty()) return Echo.fail("结果为空");
                List<SearchedPost> resList = new ArrayList<>();
                list.forEach(i -> {
                    SearchedPost p = i.source();
                    if (p != null) {
                        if (i.highlight().get("title") != null)
                            p.setTitle(i.highlight().get("title").get(0));
                        if(i.highlight().get("forumName")!=null)
                            p.setForumName(i.highlight().get("forumName").get(0));
                    }
                    resList.add(p);
                });
                return Echo.success(resList);
            case "game":
                return Echo.success();//TODO
            default:
                return Echo.fail("搜索类型错误");
        }
    }

}
