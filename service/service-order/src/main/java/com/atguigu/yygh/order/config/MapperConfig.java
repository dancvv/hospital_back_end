package com.atguigu.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.atguigu.yygh.order.mapper")
public class MapperConfig {
}
