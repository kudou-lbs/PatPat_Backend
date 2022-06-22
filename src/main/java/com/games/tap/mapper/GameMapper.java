package com.games.tap.mapper;

import com.games.tap.domain.Game;
import com.games.tap.domain.GameType;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GameMapper {
    List<Game> getAllGame();
    List<Game> OrderByHot();
    List<Game> getById(Long gId);

    int insertGame(Game game);
    Long getGidByGame(Game game);
    int insertType(Long gId, String[] types);
    int deleteType(Long gId, String[] types);

    int deleteGameById(Long gId);

    int updateGame(Game game);
}
