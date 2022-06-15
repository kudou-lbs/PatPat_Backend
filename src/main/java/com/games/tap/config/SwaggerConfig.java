package com.games.tap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                // 是否开启swagger
                .enable(true)
                .select()
                // 过滤条件，扫描指定路径下的文件
                .apis(RequestHandlerSelectors.basePackage("com.games.tap.controller"))
                // 指定路径处理，PathSelectors.any()代表不过滤任何路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Patpat接口测试")
                .description("测试接口文档    —— 致高贵的客户端和前端爷")
                .contact(new Contact("admin", "#", "327404055@qq.com"))
                .version("v0.1")
                .build();
    }
}
