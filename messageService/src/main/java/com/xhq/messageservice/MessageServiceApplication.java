package com.xhq.messageservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.xhq.api.client")
@MapperScan("com.xhq.messageservice.mapper")
public class MessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
    }

}
