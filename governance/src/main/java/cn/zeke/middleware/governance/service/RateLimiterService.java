package cn.zeke.middleware.governance.service;

import cn.zeke.middleware.governance.Constants;
import cn.zeke.middleware.governance.IGovernanceStrategy;
import cn.zeke.middleware.governance.annotation.DoRateLimiter;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date 12/2/25 PM5:56
 * @description Description of the Class
 */
@Service
public class RateLimiterService implements IGovernanceStrategy {
    @Override
    public boolean supports(Object annotation) {
        return annotation instanceof DoRateLimiter;
    }

    @Override
    public Object access(ProceedingJoinPoint pjp, Method method, Annotation annotation, Object[] args) throws Throwable {
        DoRateLimiter doRateLimiter = (DoRateLimiter) annotation;
        // no rate limiter
        if(0 == doRateLimiter.permitsPerSecond()){
            return null;
        }

        // construct key
        String clazzName = pjp.getTarget().getClass().getName();
        String methodName = method.getName();
        String key = clazzName + "." + methodName;

        if(null == Constants.rateLimiterMap.get(key)) {
            Constants.rateLimiterMap.put(key, RateLimiter.create(doRateLimiter.permitsPerSecond()));
        }

        RateLimiter rateLimiter = Constants.rateLimiterMap.get(key);
        if (rateLimiter.tryAcquire()) {
            return null;
        }

        return JSON.parseObject(doRateLimiter.returnJson(), method.getReturnType());
    }
}
