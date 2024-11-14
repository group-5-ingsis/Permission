package com.ingsis.permission.logging

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CorrelationIdFilter : Filter {

  companion object {
    const val CORRELATION_ID_HEADER = "X-Correlation-ID"
  }

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val httpServletRequest = request as HttpServletRequest
    val httpServletResponse = response as HttpServletResponse

    // Check if the correlation ID is present in the headers, otherwise generate a new one
    val correlationId = httpServletRequest.getHeader(CORRELATION_ID_HEADER) ?: UUID.randomUUID().toString()

    // Set the correlation ID in MDC for logging
    MDC.put(CORRELATION_ID_HEADER, correlationId)

    // Add correlation ID to response headers
    httpServletResponse.setHeader(CORRELATION_ID_HEADER, correlationId)

    try {
      chain.doFilter(request, response)
    } finally {
      MDC.remove(CORRELATION_ID_HEADER)
    }
  }

  override fun init(filterConfig: FilterConfig?) {}
  override fun destroy() {}
}
