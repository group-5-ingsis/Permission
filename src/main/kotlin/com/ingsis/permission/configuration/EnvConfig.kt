package com.ingsis.permission.configuration

import io.github.cdimascio.dotenv.Dotenv
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class EnvConfig {

    @PostConstruct
    fun init() {
        val dotenv = Dotenv.load()
        System.setProperty("SPRING_DATASOURCE_URL", dotenv["SPRING_DATASOURCE_URL"])
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv["SPRING_DATASOURCE_USERNAME"])
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv["SPRING_DATASOURCE_PASSWORD"])
        System.setProperty("POSTGRES_DB", dotenv["POSTGRES_DB"])
        System.setProperty("POSTGRES_USER", dotenv["POSTGRES_USER"])
        System.setProperty("POSTGRES_PASSWORD", dotenv["POSTGRES_PASSWORD"])
    }
}
