package com.hx.med.sys.exception;

import javax.validation.ValidationException;

public class ParameterNotValidException extends ValidationException {

  /**
   * constructor.
   *
   * @param message 错误消息
   */
  public ParameterNotValidException(final String message) {
    super(message);
  }

  /**
   * constructor.
   *
   * @param message 错误消息
   * @param throwable Throwable
   */
  public ParameterNotValidException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
