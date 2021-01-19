package com.hlushkov.movieland.service.config;


import com.hlushkov.movieland.dao.config.JdbcConfig;
import com.hlushkov.movieland.service.impl.DefaultGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@Import(JdbcConfig.class)
@ComponentScan(value = "com.hlushkov.movieland.service")
public class RootApplicationContext {
    @Autowired
    private DefaultGenreService defaultGenreService;

    @Bean
    protected ScheduledExecutorService getExecutorService() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(defaultGenreService, 0, 4, TimeUnit.HOURS);
        return executorService;
    }
}
