package com.hx.med.common.util.http;

import javax.validation.constraints.NotNull;
import java.net.URL;

/**
 * Created by tengxianfei on 2016/11/2.
 *
 * @author tengxianfei
 * @since 1.6
 */
public interface HttpClient {

  /**
   * 发送http 消息.
   *
   * @param httpMessage HttpMessage
   * @return boolean
   */
  boolean sendMessageToEndPoint(@NotNull final HttpMessage httpMessage);

  /**
   * 验证该url是否可达.
   *
   * @param url url
   * @return boolean
   */
  boolean isValidEndPoint(@NotNull final String url);

  /**
   * 验证该url是否可达.
   *
   * @param url URL Object
   * @return boolean
   */
  boolean isValidEndPoint(@NotNull final URL url);
}
