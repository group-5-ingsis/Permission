package com.ingsis.permission

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Permission

fun main(args: Array<String>) {
  runApplication<Permission>(*args)
}
