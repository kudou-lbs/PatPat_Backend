package com.games.tap.controller;

import com.games.tap.domain.Game;
import com.games.tap.service.GameService;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "game",description = "游戏数据接口，提供数据操作、排行榜、分类、搜索等功能")
public class GameController {
    @Resource
    GameService gameService;

    @PassToken
    @Operation(summary = "返回所有游戏信息")
    @RequestMapping(value = "/Game/getAllGame",method = RequestMethod.GET)
    public List<Game> getAllGame(){
        return gameService.getAllGame();
    }

    @PassToken
    @Operation(summary = "热度排行榜")
    @RequestMapping(value = "/Game/OrderByHot",method = RequestMethod.GET)
    public List<Game> OrderByHot(){
        return gameService.OrderByHot();
    }

    @PassToken
    @Operation(summary = "根据id返回游戏信息")
    @RequestMapping(value = "/Game/getGameById",method = RequestMethod.GET)
    public List<Game> getById(Long gId){
        return gameService.getById(gId);
    }

    @PassToken
    @Operation(summary = "插入单条游戏信息")
    @RequestMapping(value = "/Game/insert",method = RequestMethod.POST)
    public Echo insertGame(Game game){
        gameService.insertGame(game);
        Long gId=game.getGId();
        String[] types=game.getTypes().split(",");
//        int types[] = new int[str.length];
//        for(int i=0;i<str.length;i++) {
//            types[i] = Integer.parseInt(str[i]);
//        }
        gameService.insertType(gId,types);
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "批量插入游戏信息")
    @RequestMapping(value = "/Game/insertM",method = RequestMethod.POST)
    public Echo insertMGame(@RequestBody List<Game> games){
        for (Game game : games) {
            gameService.insertGame(game);
            Long gId=game.getGId();
            String[] types=game.getTypes().split(",");
//            int types[] = new int[str.length];
//            for(int i=0;i<str.length;i++) {
//                types[i] = Integer.parseInt(str[i]);
//            }
            gameService.insertType(gId,types);
        }
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "根据id删除游戏信息")
    @RequestMapping(value = "/Game/deleteById",method = RequestMethod.DELETE)
    public Echo deleteById(Long gId){
        gameService.deleteGameById(gId);
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "根据id修改游戏信息(除标签)")
    @RequestMapping(value = "/Game/updateGame",method = RequestMethod.PUT)
    public Echo updateGame(Game game){
        gameService.updateGame(game);
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "添加某个游戏（id）的部分标签（int[] types）")
    @RequestMapping(value = "/Game/addType",method = RequestMethod.POST)
    public Echo insertType(Long gId,String[] types){
        gameService.insertType(gId,types);
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "删除某个游戏（id）的部分标签（int[] types）")
    @RequestMapping(value = "/Game/deleteType",method = RequestMethod.DELETE)
    public Echo deleteType(Long gId, String[] types){
        gameService.deleteType(gId,types);
        return Echo.success();
    }
}
