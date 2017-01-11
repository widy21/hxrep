package com.hx.med.sys.validator;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.DefaultValidateCallback;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidateCallback;
import com.baidu.unbiz.fluentvalidator.ValidatorChain;
import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.baidu.unbiz.fluentvalidator.registry.impl.SpringApplicationContextRegistry;
import com.baidu.unbiz.fluentvalidator.support.FluentValidatorPostProcessor;
import com.baidu.unbiz.fluentvalidator.util.ArrayUtil;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.LocaleUtil;
import com.baidu.unbiz.fluentvalidator.util.Preconditions;
import com.baidu.unbiz.fluentvalidator.util.ReflectionUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;

/**
 * Created by tengxianfei on 2016/11/3.
 *
 * @author tengxianfei
 * @since 1.6
 */
public class DefaultFluentValidateInterceptor implements MethodInterceptor, InitializingBean, ApplicationContextAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFluentValidateInterceptor.class);

  /**
   * Spring容器上下文.
   */
  private ApplicationContext applicationContext;

  /**
   * 验证回调.
   */
  private ValidateCallback callback = new DefaultValidateCallback();

  /**
   * 验证器查找registry.
   */
  private SpringApplicationContextRegistry registry;

  /**
   * FluentValidator后置处理.
   * <p/>
   * 一般情况用不到，除非需要自己在FluentValidator中加入一些操作，例如
   * {@link FluentValidator#putAttribute2Context(String, Object)}
   * <p/>
   * 这里是做扩展使用的
   */
  private FluentValidatorPostProcessor fluentValidatorPostProcessor;

  /**
   * hibernate validator.
   */
  private Validator validator;

  /**
   * 语言地区，主要为Hibernate Validator使用.
   */
  private String locale;

  /**
   * 如果是hibernate validator验证注解的错误，统一存在一个error code.
   * <p/>
   * 如果是{@link com.baidu.unbiz.fluentvalidator.Validator}的则可以自定义error code
   */
  private int hibernateDefaultErrorCode;

  @Override
  public void afterPropertiesSet() throws Exception {
    registry = new SpringApplicationContextRegistry();
    registry.setApplicationContext(applicationContext);

    // 如果hibernate validator通过spring配置了，则不进行初始化
    if (validator == null) {
      // init hibernate validator
      if (locale != null) {
        Locale.setDefault(LocaleUtil.parseLocale(locale));
      }
      final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      validator = factory.getValidator();
    }

    LOGGER.info("Place {} as an interceptor", this.getClass().getSimpleName());
  }

  /**
   * 拦截方法进行验证，如果参数前面存在{@link FluentValid}注解，则进行校验，分一下三中情况：
   * <ul>
   * <li>1. 普通对象，直接验证</li>
   * <li>2. 列表对象，onEach验证</li>
   * <li>3. 数组对象，onEach验证</li>
   * </ul>
   * <p/>
   * 注：<br/>
   * 1) 如果参数上的注解{@link FluentValid}设置了value，则value上的验证器先验证。
   * 2) hibernate validator次先校验，然后才是级联验证
   *
   * @param invocation 调用
   * @return 返回对象
   * @throws Throwable
   */
  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {
    // Work out the target class: may be <code>null</code>.
    // The TransactionAttributeSource should be passed the target class
    // as well as the method, which may be from an interface.
    final Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
    Preconditions.checkState(targetClass != null, "Target class should NOT be NULL!");

    try {
      final Object[] arguments = invocation.getArguments();
      final Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
      final Method implMethod = ReflectionUtil.getMethod(targetClass, invocation.getMethod().getName(), parameterTypes);
      final Annotation[][] paramAnnotations = implMethod.getParameterAnnotations();
      if (paramAnnotations != null) {
        start:
        for (int i = 0; i < paramAnnotations.length; i++) {
          final Annotation[] paramAnnotation = paramAnnotations[i];
          if (ArrayUtil.isEmpty(paramAnnotation)) {
            continue start;
          }
          for (int j = 0; j < paramAnnotation.length; j++) {
            if (paramAnnotation[j].annotationType() == FluentValid.class) {
              LOGGER.debug("Find @FluentValid annotation on index[{}] parameter and ready to validate for method {}",
                i, implMethod);
              final ValidatorChain addOnValidatorChain =
                getAddOnValidatorChain((FluentValid) paramAnnotation[j]);
              final Class<?>[] groups = ((FluentValid) paramAnnotation[j]).groups();
              final Class<?>[] excludeGroups = ((FluentValid) paramAnnotation[j]).excludeGroups();
              final boolean isFailFast = ((FluentValid) paramAnnotation[j]).isFailFast();

              final FluentValidator fluentValidator = FluentValidator.checkAll(groups)
                .setExcludeGroups(excludeGroups)
                .configure(registry)
                .setIsFailFast(isFailFast);
              if (fluentValidatorPostProcessor != null) {
                fluentValidatorPostProcessor.postProcessBeforeDoValidate(fluentValidator, invocation);
              }
              final Annotation classAnnotation = arguments[i].getClass().getAnnotation(CustomValidate.class);
              com.baidu.unbiz.fluentvalidator.Validator classValidator = null;
              if (classAnnotation != null) {
                classValidator = this.getAddOnValidator((CustomValidate) classAnnotation);
              }
              ComplexResult result;
              if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                fluentValidator
                  .on(arguments[i], addOnValidatorChain)
                  .onEach((Collection) arguments[i],
                    new HibernateSupportedValidator()
                      .setHibernateDefaultErrorCode(hibernateDefaultErrorCode)
                      .setHiberanteValidator(validator))
                  .when(arguments[i] != null)
                  .onEach((Collection) arguments[i]);

                if (classValidator != null) {
                  fluentValidator.onEach((Collection) arguments[i], classValidator);
                }
                result = fluentValidator.doValidate(callback)
                  .result(toComplex());
              } else if (parameterTypes[i].isArray()) {
                fluentValidator
                  .on(arguments[i], addOnValidatorChain)
                  .onEach(ArrayUtil.toWrapperIfPrimitive(arguments[i]),
                    new HibernateSupportedValidator()
                      .setHibernateDefaultErrorCode(hibernateDefaultErrorCode)
                      .setHiberanteValidator(validator))
                  .when(arguments[i] != null)
                  .onEach(ArrayUtil.toWrapperIfPrimitive(arguments[i]));

                if (classValidator != null) {
                  fluentValidator.onEach(ArrayUtil.toWrapperIfPrimitive(arguments[i]), classValidator);
                }
                result = fluentValidator.doValidate(callback)
                  .result(toComplex());
              } else {
                fluentValidator.on(arguments[i], addOnValidatorChain)
                  .on(arguments[i],
                    new HibernateSupportedValidator()
                      .setHibernateDefaultErrorCode(hibernateDefaultErrorCode)
                      .setHiberanteValidator(validator))
                  .when(arguments[i] != null)
                  .on(arguments[i]);
                if (classValidator != null) {
                  fluentValidator.on(arguments[i], classValidator);
                }
                result = fluentValidator.doValidate(callback).result(toComplex());
              }
              if (result != null) {
                LOGGER.debug(result.toString());
              }
              continue start;
            }
          }
        }
      }

      return invocation.proceed();
    } catch (final RuntimeValidateException e) {
      throw e.getCause();
    } catch (final Throwable throwable) {
      throw throwable;
    }
  }

  /**
   * 将需要额外验证的验证器封装为{@link ValidatorChain}.
   *
   * @param fluentValid 参数上的注解装饰
   * @return 验证器链
   */
  private ValidatorChain getAddOnValidatorChain(final FluentValid fluentValid) {
    final ValidatorChain chain = new ValidatorChain();
    chain.setValidators(getAddOnValidators(fluentValid));
    return chain;
  }

  /**
   * 将参数注解上的{@link FluentValid}内的<code>value</code>设置的{@link com.baidu.unbiz.fluentvalidator.Validator}
   * 在上下文中查询，返回列表.
   *
   * @param fluentValid 参数上的注解装饰
   * @return 验证器列表
   */
  private List<com.baidu.unbiz.fluentvalidator.Validator> getAddOnValidators(final FluentValid fluentValid) {
    final Class<? extends com.baidu.unbiz.fluentvalidator.Validator>[] addOnValidatorClasses;
    List<com.baidu.unbiz.fluentvalidator.Validator> addOnValidators = null;
    if (!ArrayUtil.isEmpty(fluentValid.value())) {
      LOGGER.debug("{} additional validators found", fluentValid.value().length);
      addOnValidatorClasses = fluentValid.value();
      addOnValidators = CollectionUtil.createArrayList(fluentValid.value().length);
      for (final Class<? extends com.baidu.unbiz.fluentvalidator.Validator> addOnValidatorClass : addOnValidatorClasses) {
        final List<?> beans = registry.findByType(addOnValidatorClass);
        if (!CollectionUtil.isEmpty(beans)) {
          addOnValidators.add((com.baidu.unbiz.fluentvalidator.Validator) beans.get(0));
        }
      }
    }
    return addOnValidators;
  }

  /**
   * 获取validator.
   *
   * @param customValidate CustomerValidator
   * @return Validator
   */
  private com.baidu.unbiz.fluentvalidator.Validator getAddOnValidator(final CustomValidate customValidate) {
    final Class<? extends com.baidu.unbiz.fluentvalidator.Validator> addOnValidatorClass = customValidate.value();
    final List<?> beans = registry.findByType(addOnValidatorClass);
    if (!CollectionUtil.isEmpty(beans)) {
      return (com.baidu.unbiz.fluentvalidator.Validator) beans.get(0);
    }
    return null;
  }

  public void setCallback(final ValidateCallback callback) {
    this.callback = callback;
  }

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public void setValidator(final Validator validator) {
    this.validator = validator;
  }

  public void setLocale(final String locale) {
    this.locale = locale;
  }

  public void setHibernateDefaultErrorCode(final int hibernateDefaultErrorCode) {
    this.hibernateDefaultErrorCode = hibernateDefaultErrorCode;
  }

  public void setFluentValidatorPostProcessor(final FluentValidatorPostProcessor fluentValidatorPostProcessor) {
    this.fluentValidatorPostProcessor = fluentValidatorPostProcessor;
  }
}
