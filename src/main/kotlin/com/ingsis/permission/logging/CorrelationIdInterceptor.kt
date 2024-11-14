package com.ingsis.permission.logging

import org.slf4j.MDC
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CorrelationIdInterceptor : ClientHttpRequestInterceptor {

  companion object {
    const val CORRELATION_ID_HEADER = "X-Correlation-ID"
  }

  override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): org.springframework.http.client.ClientHttpResponse {
    // Retrieve the correlation ID from MDC or generate a new one if missing
    val correlationId = MDC.get(CORRELATION_ID_HEADER) ?: UUID.randomUUID().toString()
    // Set the correlation ID in the request header
    request.headers.set(CORRELATION_ID_HEADER, correlationId)
    // Proceed with the request
    return execution.execute(request, body)
  }
}
