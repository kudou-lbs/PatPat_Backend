package com.games.tap.mapper;

import com.games.tap.domain.Game;
import com.games.tap.domain.GameType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GameMapper {
    List<Game> getAllGame();

    int insertGame(Game game);
    Long getGidByGame(Game game);
    int insertType(Long gid,@Param("types") int[] types);
}
