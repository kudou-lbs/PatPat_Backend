package com.games.tap.service;

import com.games.tap.domain.Game;
import com.games.tap.mapper.GameMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GameService {
    @Resource
    GameMapper gameMapper;
    public int insertPic(Game game){
        return gameMapper.insertPic(game);
    }
}
