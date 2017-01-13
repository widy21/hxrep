package com.hx.med.common.util.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public class CloseableHttpRalClientImpl implements CloseableHttpRalClient {

  /**
   * Logger Object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(CloseableHttpRalClientImpl.class.getName());

  /**
   * HTTP Client.
   */
  private final CloseableHttpClient httpClient;

  /**
   * constructor.
   */
  public CloseableHttpRalClientImpl() {
    this(HttpClients.createDefault());
  }

  /**
   * constructor.
   *
   * @param httpClient CloseableHttpClient.
   */
  public CloseableHttpRalClientImpl(final CloseableHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  /**
   * Http Get请求.
   *
   * @param httpMessage HttpMessage
   * @return HttpResponse
   * @throws IOException IO异常
   */
  @Override
  public HttpResponse get(@NotNull final HttpMessage httpMessage) throws IOException {
    try {
      String url = httpMessage.getUrl().toString();
      if (url.endsWith("?") || httpMessage.getMessage().startsWith("?")) {
        url = url + httpMessage.getMessage();
      } else {
        url = new StringBuilder(url).append('?').append(httpMessage.getMessage()).toString();
      }
      final HttpGet request = new HttpGet(url);
      request.addHeader("Content-Type", httpMessage.getContentType());
      return this.httpClient.execute(request);
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new IOException(e.getMessage(), e);
    }
  }

  /**
   * Http Post 请求.
   *
   * @param httpMessage HttpMessage
   * @return HttpResponse
   * @throws IOException IO异常
   */
  @Override
  public HttpResponse post(@NotNull final HttpMessage httpMessage) throws IOException {
   try {
     final HttpPost request = new HttpPost(httpMessage.getUrl().toURI());
     request.addHeader("Content-Type", httpMessage.getContentType());
     final StringEntity entity = new StringEntity(httpMessage.getMessage(),
       ContentType.create(httpMessage.getContentType()));
     request.setEntity(entity);
     return this.httpClient.execute(request);
   } catch (final Exception e) {
     LOGGER.error(e.getMessage(), e);
     throw new IOException(e.getMessage(), e);
   }
  }

  /**
   * Http Put请求.
   *
   * @param httpMessage HttpMessage.
   * @return HttpResponse
   * @throws IOException IO异常
   */
  @Override
  public HttpResponse put(@NotNull final HttpMessage httpMessage) throws IOException {
    try {
      final HttpPut request = new HttpPut(httpMessage.getUrl().toURI());
      request.addHeader("Content-Type", httpMessage.getContentType());
      final StringEntity entity = new StringEntity(httpMessage.getMessage(),
        ContentType.create(httpMessage.getContentType()));
      request.setEntity(entity);
      return this.httpClient.execute(request);
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new IOException(e.getMessage(), e);
    }
  }

  /**
   * Http delete请求.
   *
   * @param httpMessage HttpMessage
   * @return HttpResponse
   * @throws IOException IO异常
   */
  @Override
  public HttpResponse delete(@NotNull final HttpMessage httpMessage) throws IOException {
    try {
      final HttpDelete request = new HttpDelete(httpMessage.getUrl().toURI());
      request.addHeader("Content-Type", httpMessage.getContentType());
      return this.httpClient.execute(request);
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new IOException(e.getMessage(), e);
    }
  }
}
