package com.hlushkov.service.config;


import com.hlushkov.dao.config.JdbcConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JdbcConfig.class)
@ComponentScan(value = "com.hlushkov.service")
public class RootApplicationContext {

}
