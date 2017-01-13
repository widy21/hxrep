package com.hx.med.sys.exception;

public class BusinessException extends Exception {

  /**
   * serialVersionUID.
   */
  private static final long serialVersionUID = 1410653404592087380L;

  /**
   * Business异常.
   *
   * @param message 错误消息
   */
  public BusinessException(final String message) {
    super(message);
  }

  /**
   * Business异常.
   *
   * @param message 错误消息
   * @param throwable 错误类型
   */
  public BusinessException(final String message, final Throwable throwable) {
    super(message, throwable);
  }

  /**
   * Business异常.
   *
   * @param throwable 错误类型
   */
  public BusinessException(final Throwable throwable) {
    super(throwable);
  }
}
