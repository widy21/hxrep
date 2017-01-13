package com.hx.med.common.util.http;

import com.google.common.collect.ImmutableList;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpRequestFutureTask;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

public final class SimpleHttpClient implements HttpClient, Serializable, DisposableBean {

  private static final long serialVersionUID = -96443119900407839L;

  /**
   * Logger Object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpClient.class.getName());

  /**
   * The request executor Service.
   */
  private final FutureRequestExecutionService requestExecutionService;

  /**
   * HTTP Client.
   */
  private final CloseableHttpClient httpClient;

  /**
   * The acceptable codes list.
   */
  private final List<Integer> acceptaleCodes;

  /**
   * Default construtor.
   *
   * @param requestExecutionService FutureRequestExecutionService.
   * @param httpClient HttpClient
   * @param acceptaleCodes acceptable code.
   */
  public SimpleHttpClient(final FutureRequestExecutionService requestExecutionService,
                          final CloseableHttpClient httpClient, final List<Integer> acceptaleCodes) {
    this.requestExecutionService = requestExecutionService;
    this.httpClient = httpClient;
    this.acceptaleCodes = ImmutableList.copyOf(acceptaleCodes);
  }

  @Override
  public boolean sendMessageToEndPoint(@NotNull final HttpMessage httpMessage) {
    Assert.notNull(httpMessage);
    try {
      final HttpPost request = new HttpPost(httpMessage.getUrl().toURI());
      request.addHeader("Content-Type", httpMessage.getContentType());
      final StringEntity entity = new StringEntity(httpMessage.getMessage(),
        ContentType.create(httpMessage.getContentType()));
      request.setEntity(entity);

      final HttpRequestFutureTask<String> task = this.requestExecutionService.execute(request,
        HttpClientContext.create(), new BasicResponseHandler());

      if(httpMessage.isAsynchronous()) {
        return true;
      }
      final String response = task.get();
      LOGGER.debug("send message [{}] response are {}", httpMessage, response);
      return StringUtils.isNotBlank(response);
    } catch (final RejectedExecutionException var) {
      LOGGER.error(var.getMessage(), var);
      return false;
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
      return false;
    }
  }

  @Override
  public boolean isValidEndPoint(@NotNull final String url) {
    try {
      final URL encodedUrl = new URL(url);
      return this.isValidEndPoint(encodedUrl);
    } catch (final MalformedURLException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return false;
  }

  @Override
  public boolean isValidEndPoint(@NotNull final URL url) {
    Assert.notNull(url);
    HttpEntity entity = null;
    try {
      final CloseableHttpResponse response = this.httpClient.execute(new HttpGet(url.toURI()));
      final int responseCode = response.getStatusLine().getStatusCode();
      for (final int acceptableCode: this.acceptaleCodes) {
        if (responseCode == acceptableCode) {
          LOGGER.debug("Response code from server matched {}", responseCode);
          return true;
        }
      }

      LOGGER.debug("Response code did not match any the acceptable response codes, Codes returned was {}",
        responseCode);
      if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
        final String value = response.getStatusLine().getReasonPhrase();
        LOGGER.error("There was an error contacting the endpoint {}; The error message: {}", url.toExternalForm(),
          value);
      }
      entity = response.getEntity();
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
    } finally {
      EntityUtils.consumeQuietly(entity);
    }
    return false;
  }

  @Override
  public void destroy() throws Exception {
    IOUtils.closeQuietly(this.requestExecutionService);
  }

  /**
   * get requestExecutionService.
   *
   * @return org.apache.http.impl.client.FutureRequestExecutionService
   **/
  public FutureRequestExecutionService getRequestExecutionService() {
    return requestExecutionService;
  }

  /**
   * get httpClient.
   *
   * @return org.apache.http.impl.client.CloseableHttpClient
   **/
  public CloseableHttpClient getHttpClient() {
    return httpClient;
  }

  /**
   * get acceptaleCodes.
   *
   * @return List
   **/
  public List<Integer> getAcceptaleCodes() {
    return acceptaleCodes;
  }
}
