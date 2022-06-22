package com.games.tap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.games.tap.mapper")
@SpringBootApplication
public class TapApplication {

	public static void main(String[] args) {
		SpringApplication.run(TapApplication.class, args);
	}

}
