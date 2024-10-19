package com.ingsis.permission

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model

@Controller
class HomeController {

  fun home(model: Model, @AuthenticationPrincipal principal: OidcUser): String { return "index" }
}
