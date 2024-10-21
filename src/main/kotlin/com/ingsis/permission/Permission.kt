package com.ingsis.permission

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Permission {

}

fun main(args: Array<String>) {
  val dotenv = Dotenv.load() // Asegúrate de que el archivo .env esté en la raíz del proyecto

  System.setProperty("AUTH_SERVER_ISSUER", dotenv["AUTH_SERVER_ISSUER"])
  System.setProperty("AUTH_CLIENT_ID", dotenv["AUTH_CLIENT_ID"])
  System.setProperty("AUTH_CLIENT_SECRET", dotenv["AUTH_CLIENT_SECRET"])
  System.setProperty("AUTH0_AUDIENCE", dotenv["AUTH0_AUDIENCE"])
  System.setProperty("SERVER_PORT", dotenv["SERVER_PORT"])
  System.setProperty("SPRING_DATASOURCE_URL", dotenv["SPRING_DATASOURCE_URL"])
  System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv["SPRING_DATASOURCE_USERNAME"])
  System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv["SPRING_DATASOURCE_PASSWORD"])

  runApplication<Permission>(*args)
}
