package com.expert.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.expert.admin.mapper")
public class ExpertAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpertAdminApiApplication.class, args);
    }
}
