package com.openschool.starter.aspect;

import com.openschool.starter.config.AspectAutoConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Aspect
@Component
public class MainAspect {

    private static final Logger logger = LoggerFactory.getLogger(MainAspect.class.getName());

    @Autowired
    private AspectAutoConfig aspectAutoConfig;

    public MainAspect() {
    }

    @Pointcut("within(*..*Controller)")
    public void controllerPoint() {}

    @Before("controllerPoint() || @annotation(LogArgs)")
    public void logInputParameters(JoinPoint joinPoint) {
        logger.debug("The args of the method {}.{} - {}", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

   @AfterThrowing(pointcut = "controllerPoint() || @annotation(LogException)", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        if (ex.getClass() != ResponseStatusException.class) {
            logger.error("Method {}.{} threw {}", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName(), ex.getCause().toString());
        }
    }

    @AfterReturning(pointcut = "controllerPoint()", returning = "returnValue")
    public void logReturnValue(JoinPoint joinPoint, Object returnValue) {
        if (returnValue != null) {
            logger.debug("Method {}.{} returned {}", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName(), returnValue);
        }
    }

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        logger.debug("Method {}.{} took {} ms", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName(), endTime - startTime);

        return result;
    }

}
