package com.hlushkov.movieland.service.config;


import com.hlushkov.movieland.dao.config.JdbcConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JdbcConfig.class)
@ComponentScan(value = "com.hlushkov.movieland.service")
public class RootApplicationContext {

}
