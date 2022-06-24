package com.games.tap.mapper;

import com.games.tap.domain.Game;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GameMapper {
    List<Game> getAllGame();
    List<Game> OrderByHot();
    Game getById(Long gId);
    Integer isExisted(Long gId);
    Integer isTypeExited(Long gId,String type);
    Long selectByName(String name);

    List<Game> getGameList(@Param("offset")Long offset, @Param("pageSize")Long pageSize);
    List<Game> getOrderList(@Param("offset")Long offset, @Param("pageSize")Long pageSize);
    List<Long> getTypeList(String type,@Param("offset")Long offset, @Param("pageSize")Long pageSize);

    List<Long> getGidByType(String type);

    int insertGame(Game game);
    int insertType(Long gId, String[] types);
    int deleteType(Long gId, String[] types);
    int deleteTypeById(Long gId);

    int deleteGameById(Long gId);

    int updateGame(Game game);

}
