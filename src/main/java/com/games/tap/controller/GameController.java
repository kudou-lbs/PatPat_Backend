package com.games.tap.controller;

import com.games.tap.domain.Game;
import com.games.tap.service.GameService;
import com.games.tap.util.Echo;
import com.games.tap.util.PassToken;
import com.games.tap.util.RetCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "game",description = "游戏数据接口，提供数据操作、排行榜、分类、搜索等功能")
public class GameController {
    @Resource
    GameService gameService;

    @PassToken
    @Operation(summary = "返回所有游戏信息",parameters = {
            @Parameter(name = "pageSize",description = "返回数量",required = true),
            @Parameter(name = "offset",description = "起始位置")
    })
    @RequestMapping(value = "/games",method = RequestMethod.GET)
    public Echo getAllGame(String offset, String pageSize){
        if (offset == null && pageSize == null) {
            List<Game> list = gameService.getAllGame();
            if (list == null || list.isEmpty()) return Echo.fail();
            return Echo.success(list);
        } else if (pageSize == null) {
            return Echo.fail("pageSize不能为空");
        } else {
            if (!StringUtils.isNumeric(pageSize)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            if (Long.parseLong(pageSize) <= 0) return Echo.define(RetCode.PARAM_IS_INVALID);
            long size=Long.parseLong(pageSize),start;
            if (offset == null)start=0L;
            else {
                if (!StringUtils.isNumeric(offset)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
                if (Long.parseLong(offset) < 0) return Echo.define(RetCode.PARAM_IS_INVALID);
                start=Long.parseLong(offset);
            }
            List<Game> list= gameService.getGameList(start,size);
            if (list == null || list.isEmpty()) return Echo.fail();
            return Echo.success(list);
        }
    }

    @PassToken
    @Operation(summary = "热度排行榜")
    @RequestMapping(value = "/game/rank",method = RequestMethod.GET)
    public Echo OrderByHot(String offset, String pageSize){
        if (offset == null && pageSize == null) {
            List<Game> list = gameService.OrderByHot();
            if (list == null || list.isEmpty()) return Echo.fail();
            return Echo.success(list);
        } else if (pageSize == null) {
            return Echo.fail("pageSize不能为空");
        } else {
            if (!StringUtils.isNumeric(pageSize)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            if (Long.parseLong(pageSize) <= 0) return Echo.define(RetCode.PARAM_IS_INVALID);
            long size=Long.parseLong(pageSize),start;
            if (offset == null)start=0L;
            else {
                if (!StringUtils.isNumeric(offset)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
                if (Long.parseLong(offset) < 0) return Echo.define(RetCode.PARAM_IS_INVALID);
                start=Long.parseLong(offset);
            }
            List<Game> list= gameService.getOrderList(start,size);
            if (list == null || list.isEmpty()) return Echo.fail();
            return Echo.success(list);
        }
    }

    @PassToken
    @Operation(summary = "根据游戏类型返回游戏信息")
    @RequestMapping(value = "/game/type",method = RequestMethod.GET)
    public Echo getByType(String type,String offset, String pageSize){
        if(type==null||type=="") return Echo.fail("请输入游戏类型");
        if (offset == null && pageSize == null) {
            List<Long> gIds=gameService.getGidByType(type);
            if (gIds.size()==0) return Echo.fail("没有找到该类型游戏");
            List<Game> list=new ArrayList<>();
            for (Long gId : gIds) {
                Game game=gameService.getById(gId);
                list.add(game);
            }
            if (list == null || list.isEmpty()) return Echo.fail();
            return Echo.success(list);
        } else if (pageSize == null) {
            return Echo.fail("pageSize不能为空");
        }else {
            if (!StringUtils.isNumeric(pageSize)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            if (Long.parseLong(pageSize) <= 0) return Echo.define(RetCode.PARAM_IS_INVALID);
            long size=Long.parseLong(pageSize),start;
            if (offset == null)start=0L;
            else {
                if (!StringUtils.isNumeric(offset)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
                if (Long.parseLong(offset) < 0) return Echo.define(RetCode.PARAM_IS_INVALID);
                start=Long.parseLong(offset);
            }
            List<Long> gIds=gameService.getTypeList(type,start,size);
            if (gIds.size()==0) return Echo.fail("没有找到该类型游戏");
            List<Game> list=new ArrayList<>();
            for (Long gId : gIds) {
                Game game=gameService.getById(gId);
                list.add(game);
            }
            if (list == null || list.isEmpty()) return Echo.fail();
            return Echo.success(list);
        }
    }

    @PassToken
    @Operation(summary = "根据id返回游戏信息")
    @RequestMapping(value = "/game",method = RequestMethod.GET)
    public Echo getById(String gId){
        if (!StringUtils.isNumeric(gId)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        Game gameInfo = gameService.getById(Long.parseLong(gId));
        if (gameInfo == null) return Echo.fail();
        return Echo.success(gameInfo);
    }

    @PassToken
    @Operation(summary = "插入单条游戏信息")
    @RequestMapping(value = "/game",method = RequestMethod.POST)
    public Echo insertGame(Game game){
        if(gameService.isExisted(game.getGId()) != null){
            gameService.updateGame(game);
            if (game.getTypes()!=null&&game.getTypes()!=""){
                String[] type = game.getTypes().split("  ");
                gameService.deleteTypeById(game.getGId());
                gameService.insertType(game.getGId(),type);
            }
        }
        gameService.insertGame(game);
        Long gId=game.getGId();
        String[] types=game.getTypes().split("  ");
//        int types[] = new int[str.length];
//        for(int i=0;i<str.length;i++) {
//            types[i] = Integer.parseInt(str[i]);
//        }
        gameService.insertType(gId,types);
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "批量插入游戏信息")
    @RequestMapping(value = "/games",method = RequestMethod.POST)
    public Echo insertMGame(@RequestBody List<Game> games){
        for (Game game : games) {
            Long gId = game.getGId();
            Integer result=gameService.isExisted(gId);
            if(result == null) {
                gameService.insertGame(game);
                String[] types = game.getTypes().split("  ");
//            int types[] = new int[str.length];
//            for(int i=0;i<str.length;i++) {
//                types[i] = Integer.parseInt(str[i]);
//            }
                gameService.insertType(gId, types);
            }else{
                gameService.updateGame(game);
                if (game.getTypes()!=null&&game.getTypes()!=""){
                    String[] type = game.getTypes().split("  ");
                    gameService.deleteTypeById(game.getGId());
                    gameService.insertType(game.getGId(),type);
                }
            }
        }
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "根据id删除游戏信息")
    @RequestMapping(value = "/game",method = RequestMethod.DELETE)
    public Echo deleteById(String gId){
        if (!StringUtils.isNumeric(gId)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if (gameService.deleteGameById(Long.parseLong(gId)) != 0) return Echo.success();
        return Echo.fail();
    }

    @PassToken
    @Operation(summary = "根据id修改游戏信息(除标签)")
    @RequestMapping(value = "/game",method = RequestMethod.PUT)
    public Echo updateGame(Game game){
        if(gameService.isExisted(game.getGId()) == null) return Echo.fail("该游戏不存在");
        gameService.updateGame(game);
        if (game.getTypes()!=null&&game.getTypes()!=""){
            String[] type = game.getTypes().split("  ");
            gameService.deleteTypeById(game.getGId());
            gameService.insertType(game.getGId(),type);
        }
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "添加某个游戏（id）的部分标签（String types,以两个空格分开,例如“射击  益智  RPG”）")
    @RequestMapping(value = "/game/type",method = RequestMethod.POST)
    public Echo insertType(String gId,String types){
        if (!StringUtils.isNumeric(gId)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if(gameService.isExisted(Long.parseLong(gId)) == null) return Echo.fail("该游戏不存在");
        String[] type = types.split("  ");
        for (String s : type) {
            if(gameService.isTypeExited(Long.parseLong(gId),s)!=null)
                return Echo.fail("游戏已经有该标签");
        }
        gameService.insertType(Long.parseLong(gId),type);
        return Echo.success();
    }

    @PassToken
    @Operation(summary = "删除某个游戏（id）的部分标签（String types,以两个空格分开,例如“射击  益智  RPG”）")
    @RequestMapping(value = "/game/type",method = RequestMethod.DELETE)
    public Echo deleteType(String gId, String types){
        if (!StringUtils.isNumeric(gId)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if(gameService.isExisted(Long.parseLong(gId)) == null) return Echo.fail("该游戏不存在");
        String[] type = types.split("  ");
        for (String s : type) {
            if(gameService.isTypeExited(Long.parseLong(gId),s)==null)
                return Echo.fail("游戏没有该标签");
        }
        gameService.deleteType(Long.parseLong(gId),type);
        return Echo.success();
    }
}
