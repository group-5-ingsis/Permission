package com.ingsis.permission.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
open class SecurityConfig(@Value("\${auth0.audience}")
                          val audience: String)  {


  @Value("\${okta.oauth2.issuer}") // El issuer es la url base del proveedor de autenticacion
  private val issuer: String? = null


  @Bean
  @Throws(Exception::class)
  open fun configure(http: HttpSecurity): SecurityFilterChain? {
    http
      .authorizeHttpRequests { authorize ->
        authorize

          .anyRequest().authenticated()
      }
      .csrf {
        it.disable()
      }
      .cors(withDefaults())
      .oauth2ResourceServer { oauth2 ->
        oauth2.jwt(withDefaults())
      }
    return http.build()
  }

  @Bean
  open fun jwtDecoder(): JwtDecoder {
    val jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuer).build()
    val audienceValidator: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
    val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
    val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)
    jwtDecoder.setJwtValidator(withAudience)
    return jwtDecoder
  }

}
