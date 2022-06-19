package com.games.tap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.games.tap.domain.User;
import com.games.tap.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class TapApplicationTests {
	@Resource
	UserMapper userMapper;

	private final ObjectMapper objectMapper=new ObjectMapper();

	@Test
	void contextLoads() {
		User user=userMapper.getUserById(1L);
		log.info(user.toString());
		try {
			String us= objectMapper.writeValueAsString(user);
			log.info(us);
			user=objectMapper.readValue(us,User.class);
			log.info(user.toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
