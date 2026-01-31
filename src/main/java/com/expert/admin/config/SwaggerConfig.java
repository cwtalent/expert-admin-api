package com.expert.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("投票系统后台管理API")
                        .version("1.0.0")
                        .description("投票系统后台管理接口文档")
                        .contact(new Contact()
                                .name("Expert Admin")
                                .email("admin@expert.com")));
    }
}
