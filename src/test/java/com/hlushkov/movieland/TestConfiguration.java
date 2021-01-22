package com.hlushkov.movieland;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.junit.ClassRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestConfiguration {

    @ClassRule
    public PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:13.1");

    @Bean
    public Flyway configureDataSource() {
        postgresContainer.start();

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(postgresContainer.getJdbcUrl());
        dataSource.setUsername(postgresContainer.getUsername());
        dataSource.setPassword(postgresContainer.getPassword());

        return Flyway.configure().dataSource(dataSource)
                .locations("classpath:db/migration").baselineOnMigrate(true).load();
    }
}
