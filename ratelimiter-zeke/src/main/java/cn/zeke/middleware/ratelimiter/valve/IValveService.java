package cn.zeke.middleware.ratelimiter.valve;

import cn.zeke.middleware.ratelimiter.annotation.DoRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date 12/2/25 AM10:53
 * @description Description of the Class
 */
public interface IValveService {
    Object access(ProceedingJoinPoint pjp, Method method, DoRateLimiter rateLimiter, Object[] args) throws Throwable;
}
