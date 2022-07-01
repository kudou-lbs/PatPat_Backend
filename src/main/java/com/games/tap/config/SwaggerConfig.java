package com.games.tap.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addParameters("JWT", new HeaderParameter()
                        .name("token").description("用户登录认证").schema(new StringSchema()).required(false)))
                .info(new Info()
                        .title("Patpat接口测试")
                        .version("beta v1.0")
                        .description("测试接口文档    —— 致高贵的客户端和前端爷")
                        .contact(new Contact().name("admin").url("https://gitee.com/lin_po_sheng/patpat-backend")));
    }

    /**
     * 添加全局的请求头参数
     */
    @Bean
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
                .forEach(operation -> operation.addParametersItem(new HeaderParameter().$ref("#/components/parameters/JWT")));
    }
}
