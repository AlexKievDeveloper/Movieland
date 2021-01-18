package com.hlushkov.dao.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySources(
        {@PropertySource("classpath:dev.properties"), @PropertySource("classpath:application.properties")})
@ComponentScan(value = "com.hlushkov.dao")
@EnableTransactionManagement(proxyTargetClass = true)
public class JdbcConfig {
    @Bean
    protected DataSource dataSource(@Value("${jdbc.url}") String url,
                                    @Value("${jdbc.user}") String userName,
                                    @Value("${jdbc.password}") String password,
                                    @Value("${jdbc.driver}") String driverClassName,
                                    @Value("${connections.amount}") int initialSize) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);
        return dataSource;
    }

    @Bean
    protected JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    protected NamedParameterJdbcTemplate namedJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }


    @Bean
    protected DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    protected TransactionTemplate transactionTemplate(DataSourceTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}