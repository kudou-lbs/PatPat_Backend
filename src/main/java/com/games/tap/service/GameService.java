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

    public List<Game> OrderByHot(){
        return gameMapper.OrderByHot();
    }

    public List<Game> getById(Long gId){
        return gameMapper.getById(gId);
    }

    public int insertGame(Game game){
        return gameMapper.insertGame(game);
    }

    public int insertType(Long gId,@Param("types") String[] types){
        return gameMapper.insertType(gId,types);
    }

    public int deleteType(Long gId,@Param("types") String[] types){
        return gameMapper.deleteType(gId,types);
    }

    public Long getGidByGame(Game game){
        return gameMapper.getGidByGame(game);
    }

    public int deleteGameById(Long gId){
        return gameMapper.deleteGameById(gId);
    }

    public int updateGame(Game game){
        return gameMapper.updateGame(game);
    }
}
