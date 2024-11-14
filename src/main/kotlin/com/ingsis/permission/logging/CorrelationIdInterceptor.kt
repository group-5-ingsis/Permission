package com.ingsis.permission.logging

import org.slf4j.MDC
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Component

@Component
class CorrelationIdInterceptor : ClientHttpRequestInterceptor {

  companion object {
    const val CORRELATION_ID_HEADER = "X-Correlation-ID"
    const val NO_CORRELATION_ID = "No-Correlation-ID" // Tag to use when no correlation ID is available
  }

  override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): org.springframework.http.client.ClientHttpResponse {
    // Retrieve the correlation ID from MDC or set it to a default tag if missing
    val correlationId = MDC.get(CORRELATION_ID_HEADER) ?: NO_CORRELATION_ID

    // Set the correlation ID in the request header
    request.headers.set(CORRELATION_ID_HEADER, correlationId)

    // Proceed with the request
    return execution.execute(request, body)
  }
}
