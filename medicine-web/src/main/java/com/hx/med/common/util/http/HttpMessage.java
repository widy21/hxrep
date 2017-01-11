package com.hx.med.common.util.http;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by tengxianfei on 2016/11/2.
 *
 * @author tengxianfei
 * @since 1.6
 */
public class HttpMessage {

  /**
   * Default asynchronous callback enabled.
   */
  private static final boolean DEFAULT_ASYNCHRONOUS_CALLBACK_ENABLED = true;

  /**
   * Logger.
   */
  private final Logger logger = LoggerFactory.getLogger(HttpMessage.class.getName());

  /**
   * Url.
   */
  private URL url;

  /**
   * MessageCreator(Parameter).
   */
  private String message;

  /**
   * Is asynchronous.
   */
  private final boolean asynchronous;

  /**
   * The content tyep.
   */
  private String contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;

  /**
   * constructor.
   *
   * @param url url
   * @param message 参数内容
   */
  public HttpMessage(final URL url, final String message) {
    this(url, message, DEFAULT_ASYNCHRONOUS_CALLBACK_ENABLED);
  }

  /**
   * constructor.
   *
   * @param url url
   * @param message 参数内容
   * @param asynchronous 是否异步请求
   */
  public HttpMessage(final URL url, final String message, final boolean asynchronous) {
    this.url = url;
    this.message = message;
    this.asynchronous = asynchronous;
  }

  /**
   * get url.
   *
   * @return java.lang.String
   **/
  protected final URL getUrl() {
    return url;
  }

  /**
   * get message.
   *
   * @return java.lang.String
   **/
  protected final String getMessage() {
    return this.formatOutputMessageInternal(message);
  }

  /**
   * get asynchronous.
   *
   * @return boolean
   **/
  public boolean isAsynchronous() {
    return asynchronous;
  }

  /**
   * get contentType.
   *
   * @return java.lang.String
   **/
  protected final String getContentType() {
    return contentType;
  }

  /**
   * set contentType.
   *
   * @param contentType Content Type
   */
  protected final void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  /**
   * 格式化输出消息.
   *
   * @param message 消息内容
   * @return String
   */
  protected String formatOutputMessageInternal(final String message) {
    try {
      return URLEncoder.encode(message, "UTF-8");
    } catch (final UnsupportedEncodingException e) {
      logger.warn(e.getMessage(), e);
    }
    return message;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("url", url)
      .append("message", message)
      .append("asynchronous", asynchronous)
      .append("contentType", contentType)
      .toString();
  }
}
