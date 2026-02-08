package com.bank.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {
    }
    
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {
    }
    
    @Before("restControllerPointcut()")
    public void logBeforeController(JoinPoint joinPoint) {
        logger.info("Entering Controller: {}.{}() with arguments = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }
    
    @AfterReturning(pointcut = "restControllerPointcut()", returning = "result")
    public void logAfterReturningController(JoinPoint joinPoint, Object result) {
        logger.info("Exiting Controller: {}.{}() with result = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }
    
    @AfterThrowing(pointcut = "restControllerPointcut()", throwing = "exception")
    public void logAfterThrowingController(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in Controller: {}.{}() with message = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }
    
    @Around("servicePointcut()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        logger.info("Entering Service: {}.{}() with arguments = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            logger.info("Exiting Service: {}.{}() with result = {} | Execution time: {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    result,
                    executionTime);
            
            return result;
        } catch (Throwable ex) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            logger.error("Exception in Service: {}.{}() with message = {} | Execution time: {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    ex.getMessage(),
                    executionTime);
            
            throw ex;
        }
    }
}
