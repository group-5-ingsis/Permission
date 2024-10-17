package com.ingsis.permission.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
open class SecurityConfig {
  @Bean
  @Throws(Exception::class)
  open fun configure(http: HttpSecurity): SecurityFilterChain {
    http
      .authorizeHttpRequests { authorize ->
        authorize
          .anyRequest().authenticated()
      }
      .oauth2Login(withDefaults())
    return http.build()
  }
}
