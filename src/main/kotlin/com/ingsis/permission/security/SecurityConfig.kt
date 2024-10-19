package com.ingsis.permission.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException


@Configuration
@EnableWebSecurity
open class SecurityConfig() {


  @Value("\${okta.oauth2.issuer}") // El issuer es la url base del proveedor de autenticacion
  private val issuer: String? = null

  @Value("\${okta.oauth2.client-id}") // El client-id es el identificador único de tu aplicación en Auth0
  private val clientId: String? = null


  @Bean
  @Throws(Exception::class)
  open fun configure(http: HttpSecurity): SecurityFilterChain? {
    http
      .authorizeHttpRequests { authorize ->
        authorize
          .requestMatchers("/static/**").permitAll()
          .anyRequest().authenticated()
      }
      .oauth2Login(withDefaults())
      .logout { logout: LogoutConfigurer<HttpSecurity> ->
        logout.addLogoutHandler(logoutHandler())
      }
    return http.build()
  }


  private fun logoutHandler(): LogoutHandler {
   // Hagan logout no solo de la sesión local de la aplicación, sino también de Auth0, redirigiéndolos de nuevo a tu aplicación después de cerrar la sesión.
    return LogoutHandler { _: HttpServletRequest?, response: HttpServletResponse, _: Authentication? ->
      try {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + baseUrl) // Redirige al endpoint de logout de Auth0, finaliza la session de este
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }
  }
}
