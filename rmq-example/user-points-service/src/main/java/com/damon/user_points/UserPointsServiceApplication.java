package com.damon.user_points;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.damon.user_points.domain.user_points.mapper")
public class UserPointsServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserPointsServiceApplication.class, args);

    }
}
