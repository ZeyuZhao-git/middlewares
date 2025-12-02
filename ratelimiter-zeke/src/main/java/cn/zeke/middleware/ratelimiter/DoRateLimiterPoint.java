package cn.zeke.middleware.ratelimiter;

import cn.zeke.middleware.ratelimiter.annotation.DoRateLimiter;
import cn.zeke.middleware.ratelimiter.valve.IValveService;
import cn.zeke.middleware.ratelimiter.valve.Impl.ValveServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date 12/2/25 AM10:55
 * @description Description of the Class
 */
@Aspect
@Component
public class DoRateLimiterPoint {
    @Pointcut("@annotation(cn.zeke.middleware.ratelimiter.annotation.DoRateLimiter)")
    public void pointCut() {}

    @Around("pointCut() && @annotation(doRateLimiter)")
    public Object doRouter(ProceedingJoinPoint pjp, DoRateLimiter doRateLimiter) throws Throwable {
        IValveService valveService = new ValveServiceImpl();
        return valveService.access(pjp, getMethod(pjp), doRateLimiter, pjp.getArgs());
    }

    Method getMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        return pjp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
