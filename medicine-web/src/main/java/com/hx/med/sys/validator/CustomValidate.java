package com.hx.med.sys.validator;

import com.baidu.unbiz.fluentvalidator.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tengxianfei on 2016/11/3.
 *
 * @author tengxianfei
 * @since 1.6
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomValidate {

  /**
   * 验证器列表，接受{@link Validator}实现类的数组，除了级联外需要处理的额外验证.
   */
  Class<? extends Validator> value();

  /**
   * 作用于Spring AOP时候，用于标示分组验证，作于与属性时不起任何作用.
   */
  Class<?>[] groups() default {};

  /**
   * 作用于Spring AOP时候，用于标示分组验证，作于与属性时不起任何作用.
   * <p/>
   * 和{@link #groups()}意义相反，设置了这个的就不会调用使用了{@link FluentValidate}注解的验证，对于hibernate无效
   */
  Class<?>[] excludeGroups() default {};
}
