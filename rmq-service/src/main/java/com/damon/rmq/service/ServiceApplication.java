package com.damon.rmq.service;

import com.damon.rmq.service.utils.ClassPathFileUtil;
import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;


@ComponentScan(basePackages = {"com.damon.rmq.service"})
@SpringBootApplication
@MapperScan("com.damon.rmq.dal.mapper")
public class ServiceApplication {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Bean("shardingDataSource")
    public DataSource shardjdbcDataSource() throws SQLException, IOException {
        return YamlShardingDataSourceFactory
                .createDataSource(ClassPathFileUtil.getFile("application-database-" + activeProfile + ".yaml"));
    }
}
