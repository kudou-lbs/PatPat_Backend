package com.games.tap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("com.games.tap.mapper")
@SpringBootApplication
public class TapApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TapApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder app){
		return app.sources(TapApplication.class);
	}

}
