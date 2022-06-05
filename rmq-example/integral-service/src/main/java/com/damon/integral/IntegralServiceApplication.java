package com.damon.integral;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.damon.integral.mapper")
public class IntegralServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(IntegralServiceApplication.class, args);

    }
}
