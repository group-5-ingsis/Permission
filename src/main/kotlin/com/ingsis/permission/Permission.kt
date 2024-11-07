package com.ingsis.permission

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Permission

fun main(args: Array<String>) {
  val dotenv = Dotenv.load()

  val profile = dotenv["SPRING_PROFILES_ACTIVE"] ?: "local"
  System.setProperty("spring.profiles.active", profile)

  if (profile == "local") {
    System.setProperty("AUTH_SERVER_URI", dotenv["AUTH_SERVER_URI"])
    System.setProperty("AUTH_CLIENT_ID", dotenv["AUTH_CLIENT_ID"])
    System.setProperty("AUTH_CLIENT_SECRET", dotenv["AUTH_CLIENT_SECRET"])
    System.setProperty("AUTH0_AUDIENCE", dotenv["AUTH0_AUDIENCE"])
    System.setProperty("SERVER_PORT", dotenv["SERVER_PORT"])
    System.setProperty("SPRING_LOCAL_DATASOURCE_URL", dotenv["SPRING_LOCAL_DATASOURCE_URL"])
    System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv["SPRING_DATASOURCE_USERNAME"])
    System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv["SPRING_DATASOURCE_PASSWORD"])
    System.setProperty("CLAIMS_KEY", dotenv["CLAIMS_KEY"])
  }

  runApplication<Permission>(*args)
}
