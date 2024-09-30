package com.ingsis.permission.config

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import javax.sql.DataSource

@TestConfiguration
open class TestDatabaseConfig {

    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("testdb")
            withUsername("user")
            withPassword("password")
        }
    }

    @Bean
    open fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .url(postgresContainer.jdbcUrl)
            .username(postgresContainer.username)
            .password(postgresContainer.password)
            .build()
    }
}
