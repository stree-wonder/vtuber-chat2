package com.xhq.promptservice;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.xhq.api.client")
@SpringBootApplication
@MapperScan("com.xhq.promptservice.mapper")
public class PromptServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromptServiceApplication.class, args);
    }

}
