package com.xhq.subject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.xhq.subject.mapper")
@EnableFeignClients(basePackages = "com.xhq.api.client")
@SpringBootApplication
public class SubjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubjectApplication.class, args);
    }

}
