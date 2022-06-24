package com.games.tap.service;

import com.games.tap.domain.Game;
import com.games.tap.mapper.GameMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GameService {
    @Resource
    GameMapper gameMapper;
    public List<Game> getAllGame(){
        return gameMapper.getAllGame();
    }

    public List<Game> getGameList(Long offset,Long pageSize){
        return gameMapper.getGameList(offset,pageSize);
    }

    public List<Game> OrderByHot(){
        return gameMapper.OrderByHot();
    }

    public List<Game> getOrderList(Long offset,Long pageSize){
        return gameMapper.getOrderList(offset,pageSize);
    }

    public Game getById(Long gId){
        return gameMapper.getById(gId);
    }

    public List<Long> getGidByType(String type){
        return gameMapper.getGidByType(type);
    }

    public List<Long> getTypeList(String type,Long offset, Long pageSize){
        return gameMapper.getTypeList(type,offset,pageSize);
    }

    public Integer isExisted(Long gId){
        return gameMapper.isExisted(gId);
    }

    public Integer isTypeExited(Long gId,String type){
        return gameMapper.isTypeExited(gId,type);
    }

    public int insertGame(Game game){
        return gameMapper.insertGame(game);
    }

    public int insertType(Long gId,String[] types){
        return gameMapper.insertType(gId,types);
    }

    public int deleteType(Long gId,String[] types){
        return gameMapper.deleteType(gId,types);
    }

    public int deleteTypeById(Long gId){
        return gameMapper.deleteTypeById(gId);
    }

    public int deleteGameById(Long gId){
        return gameMapper.deleteGameById(gId);
    }

    public int updateGame(Game game){
        return gameMapper.updateGame(game);
    }

    public Long selectByName(String name){
        return gameMapper.selectByName(name);
    }
}
