package com.hlushkov.movieland.config;

import com.hlushkov.movieland.RootApplicationContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SpringJUnitWebConfig(value = {
        RootApplicationContext.class,
        com.hlushkov.movieland.web.WebApplicationContext.class,
        TestConfiguration.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextConfiguration {
}
