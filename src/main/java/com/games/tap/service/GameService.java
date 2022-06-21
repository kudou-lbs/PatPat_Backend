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

    public int insertGame(Game game){
        return gameMapper.insertGame(game);
    }

    public int insertType(Long gid,@Param("types") int[] types){
        return gameMapper.insertType(gid,types);
    }

    public Long getGidByGame(Game game){
        return gameMapper.getGidByGame(game);
    }
}
