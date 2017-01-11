package com.hx.med.sys.validator;

import com.baidu.unbiz.fluentvalidator.DefaultValidateCallback;
import com.baidu.unbiz.fluentvalidator.ValidateCallback;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.validator.element.ValidatorElementList;
import com.hx.med.sys.exception.ParameterNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by tengxianfei on 2016/11/3.
 *
 * @author tengxianfei
 * @since 1.6
 */
public class ValidateEntityCallback extends DefaultValidateCallback implements ValidateCallback {

  /**
   * Logger Object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ValidateEntityCallback.class.getName());

  @Override
  public void onSuccess(final ValidatorElementList validatorElementList) {
    super.onSuccess(validatorElementList);
  }

  @Override
  public void onFail(final ValidatorElementList validatorElementList, final List<ValidationError> errors) {
    //TODO 统一使用ConstraintViolationException
    throw new ParameterNotValidException(errors.get(0).getErrorMsg());
  }

  @Override
  public void onUncaughtException(final Validator validator, final Exception e, final Object target) throws Exception {
    LOGGER.error(e.getMessage(), e);
    super.onUncaughtException(validator, e, target);
  }
}
