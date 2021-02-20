package com.hlushkov.movieland.config;

import com.hlushkov.movieland.MovielandApplicationContext;
import com.hlushkov.movieland.web.MovielandWebApplicationContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SpringJUnitWebConfig(value = {
        MovielandApplicationContext.class,
        MovielandWebApplicationContext.class,
        TestConfiguration.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestWebContextConfiguration {
}
