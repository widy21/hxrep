package com.hx.med.common.aop;

import com.hx.med.common.dto.BaseResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * @author yingjun
 *
 * 采用AOP的方式处理参数问题。
 * @since 1.6
 */
@Component
@Aspect
public class BindingResultAop {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * aop操作.
     */
    @Pointcut("execution(* com.hx.sys.api.v1.web.*.*(..))")
    public void aopMethod(){}

    /**
     * around方法.
     * @param joinPoint test
     * @return objects
     * @throws Throwable 运行异常
     */
    @Around("aopMethod()")
    public Object  around(final ProceedingJoinPoint joinPoint) throws Throwable{
        logger.info("before method invoking!");
        BindingResult bindingResult = null;
        for(final Object arg:joinPoint.getArgs()){
            if(arg instanceof BindingResult){
                bindingResult = (BindingResult) arg;
            }
        }
        if(bindingResult != null){
            if(bindingResult.hasErrors()){
                final String errorInfo = new StringBuilder("[").append(bindingResult).append(']').
                        append(bindingResult.getFieldError().getDefaultMessage()).toString();
                return new BaseResult<Object>("0000", errorInfo);
            }
        }
        return joinPoint.proceed();
    }
}
