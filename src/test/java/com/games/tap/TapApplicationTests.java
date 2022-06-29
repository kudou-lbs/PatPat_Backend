package com.games.tap;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import com.games.tap.domain.User;
import com.games.tap.mapper.UserMapper;
import com.games.tap.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class TapApplicationTests {
	// @Resource
	// SearchService service;
	// @Resource
	// ElasticsearchClient client;
// 	@Test
// 	void test() {
// 		User u1=new User(4L,"塞尔达玩家","111");
// 		User u2=new User(1L,"原皮魅力时刻","999");
// 		User u3=new User(3L,"正常玩家","woc,原");
// 		try {
// //			service.addItem("user",u1);
// //			service.updateItem("user",u2,User.class);
// //			service.addUser(u2);
// //			service.addUser(u3);
// 			service.searchUser("魅力",0,10);
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 	}
// 	@Test
// 	void test1(){
// 		try {
// 			service.searchAll("user",0,10,User.class);
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 	}
// 	@Test
// 	void test2(){
// 		try {
// //			service.deleteUser("1");
// 			service.deleteIndex("user");
// 			service.createUserIndex();
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 	}
// 	@Test
// 	void test3(){
// 		try {
// 			log.info(service.getItem("user","1",User.class).toString());
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 	}
// 	@Test
// 	void test4(){
// 		try {
// 			service.createUserIndex();
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 	}

}
