package com.hlushkov.movieland;

import org.junit.ClassRule;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestConfiguration {

    @ClassRule
    public GenericContainer postgresContainer = new PostgreSQLContainer("postgres:13.1")
            .withFileSystemBind("db/migration", "/docker-entrypoint-initdb.d",
                    BindMode.READ_ONLY);

}
