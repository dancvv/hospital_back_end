package com.atguigu.yygh.statistic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.atguigu"})
@ComponentScan(basePackages = {"com.atguigu"})
public class ServiceStatisticApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceStatisticApplication.class, args);
    }
}
