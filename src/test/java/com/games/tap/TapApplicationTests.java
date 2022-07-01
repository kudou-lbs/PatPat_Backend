package com.games.tap;

import com.games.tap.controller.SearchController;
import com.games.tap.domain.Forum;
import com.games.tap.domain.User;
import com.games.tap.mapper.SearchMapper;
import com.games.tap.service.SearchService;
import com.games.tap.vo.SearchedPost;
import com.games.tap.vo.SearchedUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class TapApplicationTests {

	@Resource
	SearchService service;
	@Resource
	SearchMapper searchMapper;
	@Resource
	SearchController searchController;

	private final SearchedUser user=new SearchedUser(1L,"admin","null","hello",0);
	private final SearchedPost post= new SearchedPost(1L,0,0,"test","echo","gn","null");
	private final Forum forum=new Forum(1L,"gn","gck","gnk48",0,0);

	@Order(1)
	@Test
	void createIndex() {
		try {
			service.deleteIndex("user");
			service.deleteIndex("post");
			service.deleteIndex("forum");
			service.deleteIndex("game");
			if(service.isIndexNonexistence("user")) service.createUserIndex();
			if(service.isIndexNonexistence("post"))service.createPostIndex();
			if(service.isIndexNonexistence("forum"))service.createForumIndex();
			if(service.isIndexNonexistence("game"))service.createGameIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Order(2)
	@Test
	void addItem(){
		try {
			service.addItem("user",user);
			service.addItem("post",post);
			service.addItem("forum",forum);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Order(3)
	@Test
	void deleteItem(){
		try {
			service.deleteItem("user", user.getId());
			service.deleteItem("post", post.getId());
			service.deleteItem("forum", forum.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Order(4)
	@Test
	void initItem(){
		try {
			service.addItemList("user",searchMapper.getSearchUser());
			service.addItemList("post",searchMapper.getSearchPost());
			service.addItemList("forum",searchMapper.getForums());
			service.addItemList("game",searchMapper.getGames());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void test(){
		try {
			log.info(searchController.search("user","王者",0,100,null).toString());
//			service.searchUser("V",0,10,SearchedUser.class);
//			service.searchAll("user",0,10,SearchedUser.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
