package com.ingsis.permission

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class HomeController {

  @GetMapping("/")
  fun index(): String {
    return "I'm Alive!"
  }

  @GetMapping("/jwt")
  fun jwt(@AuthenticationPrincipal jwt: Jwt): String {
    return jwt.tokenValue
  }

  @GetMapping("/snippets")
  fun getAllMessages(): String {
    return "secret message"
  }

  @GetMapping("/snippets/{id}")
  fun getSingleMessage(@PathVariable id: String): String {
    return "secret message $id"
  }

  @PostMapping("/snippets")
  fun createMessage(@RequestBody message: String?): String {
    return String.format("Message was created. Content: %s", message)
  }


  fun home(model: Model, @AuthenticationPrincipal principal: OidcUser): String { return "index" }
}
