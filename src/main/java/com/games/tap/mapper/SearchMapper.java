package com.games.tap.mapper;

import com.games.tap.domain.Forum;
import com.games.tap.domain.Game;
import com.games.tap.vo.SearchedPost;
import com.games.tap.vo.SearchedUser;

import java.util.List;

public interface SearchMapper {
    List<SearchedUser>getSearchUser();

    List<Forum>getForums();

    List<SearchedPost>getSearchPost();

    List<Game>getGames();
}
