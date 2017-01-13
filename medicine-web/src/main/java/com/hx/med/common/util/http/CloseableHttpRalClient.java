package com.hx.med.common.util.http;

import org.apache.http.HttpResponse;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public interface CloseableHttpRalClient {

  /**
   * Http Get请求.
   *
   * @param httpMessage HttpMessage
   * @return HttpResponse
   * @throws IOException IO异常
   */
  HttpResponse get(@NotNull final HttpMessage httpMessage) throws IOException;

  /**
   * Http Post 请求.
   *
   * @param httpMessage HttpMessage
   * @return HttpResponse
   * @throws IOException IO异常
   */
  HttpResponse post(@NotNull final HttpMessage httpMessage) throws IOException;

  /**
   * Http Put请求.
   *
   * @param httpMessage HttpMessage.
   * @return HttpResponse
   * @throws IOException IO异常
   */
  HttpResponse put(@NotNull final HttpMessage httpMessage) throws IOException;

  /**
   * Http delete请求.
   *
   * @param httpMessage HttpMessage
   * @return HttpResponse
   * @throws IOException IO异常
   */
  HttpResponse delete(@NotNull final HttpMessage httpMessage) throws IOException;
}
