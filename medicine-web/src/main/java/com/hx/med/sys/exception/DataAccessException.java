package com.hx.med.sys.exception;

/**
 * Created by tengxianfei on 2016/11/4.
 *
 * @author tengxianfei
 * @since 1.6
 */
public class DataAccessException extends RuntimeException{

  private static final long serialVersionUID = -4256459731170546117L;

  /**
   * DataAccess异常.
   *
   * @param message 错误消息
   */
  public DataAccessException(final String message) {
    super(message);
  }

  /**
   * DataAccess异常.
   *
   * @param message 错误消息
   * @param throwable 错误类型
   */
  public DataAccessException(final String message, final Throwable throwable) {
    super(message, throwable);
  }

  /**
   * DataAccess异常.
   *
   * @param throwable 错误类型
   */
  public DataAccessException(final Throwable throwable) {
    super(throwable);
  }
}
