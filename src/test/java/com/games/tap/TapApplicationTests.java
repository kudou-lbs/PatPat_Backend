package com.games.tap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.games.tap.domain.TypeEnum;
import com.games.tap.domain.User;
import com.games.tap.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class TapApplicationTests {
	@Resource
	UserMapper userMapper;

	@Test
	void contextLoads() {
		Assert.isTrue(AsyncLoggerContextSelector.isSelected(), "log4j2 的异步 disruptor启动失败");
	}

}
