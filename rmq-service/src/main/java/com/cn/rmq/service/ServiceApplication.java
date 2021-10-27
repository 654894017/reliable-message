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
import com.cn.rmq.service.utils.SpringContextUtil;

@ComponentScan(basePackages = {"com.cn.rmq.service"})
@SpringBootApplication
@MapperScan("com.cn.rmq.dal.mapper")
public class ServiceApplication {

    @Bean("shardingDataSource")
    public DataSource shardjdbcDataSource() throws SQLException, IOException {
        String activeProfile = SpringContextUtil.getActiveProfile();
        return YamlShardingDataSourceFactory
            .createDataSource(ClassPathFileUtil.getFile("application-database-" + activeProfile + ".yaml"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
