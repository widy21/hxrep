package com.hx.med.sys.exception;

import com.alibaba.fastjson.JSONException;
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;
import com.hx.med.common.dto.BaseResult;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * 400.
   *
   * @param ex MethodArgumentNotValidException
   * @param headers HttpHeaders
   * @param status HttpStatus
   * @param request WebRequest
   * @return ResponseEntity
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                final HttpHeaders headers, final HttpStatus status,
                                                                final WebRequest request) {
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    final BaseResult<List<String>> apiError = new BaseResult<>(String.valueOf(HttpStatus.BAD_REQUEST.value()),
      HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
  }

  /**
   * BindException Handle.
   *
   * @param ex BindException
   * @param headers HttpHeaders
   * @param status HttpStatus
   * @param request WebRequest
   * @return ResponseEntity
   */
  @Override
  protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers,
                                                       final HttpStatus status, final WebRequest request) {
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    final BaseResult<List<String>> apiError = new BaseResult<>(String.valueOf(HttpStatus.BAD_REQUEST.value()),
      HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
                                                      final HttpStatus status, final WebRequest request) {
    final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
      + ex.getRequiredType();
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.BAD_REQUEST.value()),
      HttpStatus.BAD_REQUEST.getReasonPhrase(), error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
                                                                   final HttpHeaders headers, final HttpStatus status,
                                                                   final WebRequest request) {
    final String error = ex.getRequestPartName() + " part is missing";
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.BAD_REQUEST.value()),
      HttpStatus.BAD_REQUEST.getReasonPhrase(), error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex,
                                                                        final HttpHeaders headers,
                                                                        final HttpStatus status,
                                                                        final WebRequest request) {
    final String error = ex.getParameterName() + " parameter is missing";
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.BAD_REQUEST.value()),
      HttpStatus.BAD_REQUEST.getReasonPhrase(), error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle MethodArgumentTypeMismatchException.
   *
   * @param ex MethodArgumentTypeMismatchException
   * @param request WebRequest
   * @return ReseponseEntity
   */
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
                                                                 final WebRequest request) {
    final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.BAD_REQUEST.value()),
      HttpStatus.BAD_REQUEST.getReasonPhrase(), error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle ConstrainViolationException.
   *
   * @param ex ConstrainViolationException
   * @param request WebRequest
   * @return ReseponseEntity
   */
  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {

    final List<String> errors = new ArrayList<>();
    for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getRootBeanClass().getName() + ' ' + violation.getPropertyPath() + ": " + violation.getMessage());
    }

    final BaseResult<List<String>> apiError = new BaseResult<>(String.valueOf(HttpStatus.BAD_REQUEST.value()),
      HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle MethodArgumentTypeMismatchException.
   *
   * @param ex MethodArgumentTypeMismatchException
   * @param request WebRequest
   * @return ReseponseEntity
   */
  @ExceptionHandler({ParameterNotValidException.class, UnexpectedTypeException.class, JSONException.class})
  public ResponseEntity<Object> handleParameterNotValidException(final Exception ex,
                                                                 final WebRequest request) {
    final String error = ex.getMessage();
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.BAD_REQUEST.value()),
      HttpStatus.BAD_REQUEST.getReasonPhrase(), error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle AccessDeniedException. (401).
   *
   * @param ex Exception
   * @param request WebRequest
   * @return ResponseEntity
   */
  @ExceptionHandler({ BadCredentialsException.class })
  public ResponseEntity<Object> handleBadCredentialsException(final Exception ex, final WebRequest request) {
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.FORBIDDEN.value()),
      HttpStatus.UNAUTHORIZED.getReasonPhrase(), MessageSupport.getText("global.401.error"));
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  /**
   * Handle AccessDeniedException. (403).
   *
   * @param ex Exception
   * @param request WebRequest
   * @return ResponseEntity
   */
  @ExceptionHandler({ AccessDeniedException.class })
  public ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.FORBIDDEN.value()),
      HttpStatus.FORBIDDEN.getReasonPhrase(), MessageSupport.getText("global.403.error"));
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  // 404
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
                                                                 final HttpHeaders headers, final HttpStatus status,
                                                                 final WebRequest request) {
    final String error = new StringBuilder("No handler found for ").append(ex.getHttpMethod()).append(' ')
      .append(ex.getRequestURL()).toString();
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.NOT_FOUND.value()),
      HttpStatus.NOT_FOUND.getReasonPhrase(), error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  // 405
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex,
                                                                       final HttpHeaders headers,
                                                                       final HttpStatus status,
                                                                       final WebRequest request) {
    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are ");
    for(final HttpMethod method: ex.getSupportedHttpMethods()) {
      builder.append(method).append(' ');
    }

    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()),
      HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), builder.toString());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
  }


  // 415
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
                                                                   final HttpHeaders headers, final HttpStatus status,
                                                                   final WebRequest request) {
    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    for (final MediaType type: ex.getSupportedMediaTypes()) {
      builder.append(type).append(' ');
    }
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()),
      HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), builder.toString());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  //504
  @Override
  protected ResponseEntity<Object> handleAsyncRequestTimeoutException(final AsyncRequestTimeoutException ex,
                                                                      final HttpHeaders headers,
                                                                      final HttpStatus status,
                                                                      final WebRequest request) {
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()),
      HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase(), "Gateway Timeout");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.GATEWAY_TIMEOUT);
  }

  /**
   * Handle Exception(500).
   *
   * @param ex Exception
   * @param request WebRequest
   * @return ResponseEntity
   */
  @ExceptionHandler({Exception.class, BusinessException.class, DataAccessException.class})
  public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
    final BaseResult<String> apiError = new BaseResult<>(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
      HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), MessageSupport.getText("global.500.error"));
      HttpHeaders headers = new HttpHeaders();
      MediaType mediaType=new MediaType("text","html", Charset.forName("UTF-8"));
      headers.setContentType(mediaType);
    return new ResponseEntity<Object>(apiError, headers, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
