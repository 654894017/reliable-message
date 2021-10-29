package com.damon.rmq.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.damon.rmq.service.utils.ClassPathFileUtil;


@ComponentScan(basePackages = {"com.damon.rmq.service"})
@SpringBootApplication
@MapperScan("com.damon.rmq.dal.mapper")
public class ServiceApplication {
    
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean("shardingDataSource")
    public DataSource shardjdbcDataSource() throws SQLException, IOException {
        return YamlShardingDataSourceFactory
            .createDataSource(ClassPathFileUtil.getFile("application-database-" + activeProfile + ".yaml"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
