package com.atguigu.yygh.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mapper 扫描注解
 * 一共有两种方式完成
 * 在启动类上加入mapperscan
 * 或者建立config类
 */
@Configuration
@MapperScan("com.atguigu.yygh.mapper")
public class HospConfig {
}
