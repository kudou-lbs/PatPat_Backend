package com.games.tap.controller;

import com.games.tap.domain.Game;
import com.games.tap.service.GameService;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "game",description = "游戏数据接口，提供数据操作、排行榜、分类、搜索等功能")
public class GameController {
    @Resource
    GameService gameService;

    @PassToken
    @Operation(summary = "返回所有游戏信息")
    @RequestMapping(value = "/Game/getAllGame")
    public List<Game> getAllGame(){
        return gameService.getAllGame();
    }

    @PassToken
    @Operation(summary = "插入游戏信息")
    @RequestMapping(value = "/Game/insert")
    public Echo insertGame(Game game, int[] types){
        gameService.insertGame(game);
        Long gid= gameService.getGidByGame(game);
        gameService.insertType(gid,types);
        return Echo.success();
    }


}
