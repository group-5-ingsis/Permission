package com.ingsis.permission.security
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

class AudienceValidator(private val audience: String) : OAuth2TokenValidator<Jwt> {
  // Valida que el token contenga la audiencia esperada.
  override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
    val error = OAuth2Error("invalid_token", "The required audience is missing", null)

    // Verifica si la audiencia del JWT contiene la audiencia especificada.
    return if (jwt.audience.contains(audience)) {
      OAuth2TokenValidatorResult.success()
    } else {
      OAuth2TokenValidatorResult.failure(error)
    }
  }
}
