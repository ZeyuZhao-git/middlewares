package cn.zeke.middleware.governance;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date 12/2/25 AM10:53
 * @description Description of the Class
 */
public interface IGovernanceStrategy {
    /**
     * Determine whether the specified annotation type is supported.
     * @param annotation
     * @return boolean
     */
    boolean supports(Object annotation);

    /**
     * Use the service corresponding to the annotation.
     * @param joinPoint
     * @param method
     * @param annotation
     * @param args
     * @return
     * @throws Throwable
     */
    Object access(ProceedingJoinPoint joinPoint, Method method, Annotation annotation, Object[] args) throws Throwable;
}
