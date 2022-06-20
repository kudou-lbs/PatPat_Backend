package com.games.tap.service;

import com.games.tap.domain.Game;
import com.games.tap.mapper.GameMapper;
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
}
