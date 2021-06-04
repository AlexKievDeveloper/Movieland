package com.hlushkov.movieland.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class TestConfiguration {

    @Bean
    public DataSource dataSource(@Value("${jdbc.maximum.pool.size}") int maximumPoolSize) {

        PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:13.1");
        log.info("Postgres container database name: {}", postgresContainer.getDatabaseName());
        log.info("Postgres container username: {}", postgresContainer.getUsername());
        log.info("Postgres container password: {}", postgresContainer.getPassword());
        postgresContainer.start();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(postgresContainer.getJdbcUrl());
        hikariConfig.setUsername(postgresContainer.getUsername());
        hikariConfig.setPassword(postgresContainer.getPassword());
        hikariConfig.setDriverClassName(postgresContainer.getDriverClassName());
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Flyway flyway = Flyway.configure().dataSource(hikariDataSource)
                .locations("classpath:db/migration/initial").baselineOnMigrate(true).load();

        flyway.migrate();

        return hikariDataSource;
    }

}


