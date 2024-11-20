package com.ingsis.permission.permissions

import com.ingsis.permission.logging.CorrelationIdInterceptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.support.HttpRequestWrapper
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.eq

@ExtendWith(MockitoExtension::class)
class CorrelationIdInterceptorTest {

  @Mock
  private lateinit var request: HttpRequest

  @Mock
  private lateinit var execution: ClientHttpRequestExecution

  @Mock
  private lateinit var response: ClientHttpResponse

  @InjectMocks
  private lateinit var interceptor: CorrelationIdInterceptor

  private lateinit var headers: HttpHeaders

  @BeforeEach
  fun setUp() {
    headers = HttpHeaders()
    `when`(request.headers).thenReturn(headers)
  }

  @Test
  fun `intercept should add correlation ID to request header`() {
    val body = ByteArray(0)
    val correlationId = "test-correlation-id"
    MDC.put(CorrelationIdInterceptor.CORRELATION_ID_HEADER, correlationId)

    val requestWrapper = HttpRequestWrapper(request)
    `when`(execution.execute(requestWrapper, body)).thenReturn(response)

    interceptor.intercept(requestWrapper, body, execution)

    val captor = ArgumentCaptor.forClass(HttpRequest::class.java)
    verify(execution).execute(captor.capture(), eq(body))
    val capturedRequest = captor.value

    assertNotNull(capturedRequest.headers[CorrelationIdInterceptor.CORRELATION_ID_HEADER])
    assertTrue(capturedRequest.headers[CorrelationIdInterceptor.CORRELATION_ID_HEADER]!!.contains(correlationId))
  }

  @Test
  fun `intercept should use default correlation ID if not present in MDC`() {
    val body = ByteArray(0)
    MDC.clear()

    val requestWrapper = HttpRequestWrapper(request)
    `when`(execution.execute(requestWrapper, body)).thenReturn(response)

    interceptor.intercept(requestWrapper, body, execution)

    val captor = ArgumentCaptor.forClass(HttpRequest::class.java)
    verify(execution).execute(captor.capture(), eq(body))
    val capturedRequest = captor.value

    assertNotNull(capturedRequest.headers[CorrelationIdInterceptor.CORRELATION_ID_HEADER])
    assertTrue(capturedRequest.headers[CorrelationIdInterceptor.CORRELATION_ID_HEADER]!!.contains(CorrelationIdInterceptor.NO_CORRELATION_ID))
  }
}
