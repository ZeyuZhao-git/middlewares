package cn.zeke.middleware.ratelimiter.valve.Impl;

import cn.zeke.middleware.ratelimiter.Constants;
import cn.zeke.middleware.ratelimiter.annotation.DoRateLimiter;
import cn.zeke.middleware.ratelimiter.valve.IValveService;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date 12/2/25 AM10:54
 * @description Description of the Class
 */
public class ValveServiceImpl implements IValveService {

    @Override
    public Object access(ProceedingJoinPoint pjp, Method method, DoRateLimiter doRateLimiter, Object[] args) throws Throwable {
        // no rate limiter
        if(0 == doRateLimiter.permitsPerSecond()){
            return pjp.proceed();
        }

        // construct key
        String clazzName = pjp.getTarget().getClass().getName();
        String methodName = method.getName();
        String key = clazzName + "." + methodName;

        if(null == Constants.rateLimiterMap.get(key)) {
            System.out.println("RateLimiter created, key: " + key);
            Constants.rateLimiterMap.put(key, RateLimiter.create(doRateLimiter.permitsPerSecond()));
        }

        RateLimiter rateLimiter = Constants.rateLimiterMap.get(key);
        if (rateLimiter.tryAcquire()) {
            System.out.println("RateLimiter permits successful, key: " + key);
            return pjp.proceed();
        }

        System.out.println("RateLimiter permits denied, key: " + key);
        return JSON.parseObject(doRateLimiter.returnJson(), method.getReturnType());
    }
}
