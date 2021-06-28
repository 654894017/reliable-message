package com.cn.rmq.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.cn.rmq.service.utils.ClassPathFileUtil;


@ComponentScan(basePackages = {"com.cn.rmq.service.mq" })
@SpringBootApplication
@MapperScan("com.cn.rmq.dal.mapper")
public class ServiceApplication {
    
    @Bean("shardingDataSource")
    public static DataSource shardjdbcDataSource() throws SQLException, IOException {
        return YamlShardingDataSourceFactory.createDataSource(ClassPathFileUtil.getFile("application-database.yaml"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
