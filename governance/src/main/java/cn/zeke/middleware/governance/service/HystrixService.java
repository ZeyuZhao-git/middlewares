package cn.zeke.middleware.governance.service;

import cn.zeke.middleware.governance.annotation.DoHystrix;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.*;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date 12/2/25 PM5:56
 * @description Description of the Class
 */
public class HystrixService extends HystrixCommand<Object>{

    private ProceedingJoinPoint proceedingJoinPoint;
    private Method method;
    private DoHystrix doHystrix;

    public HystrixService(String groupName,
                            String commandKey,
                            String threadPoolName,
                            int coreThreadNum,
                            int timeout) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupName))
                      .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                      .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPoolName))
                      .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                                                            .withExecutionTimeoutInMilliseconds(timeout))
                      .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                                                               .withCoreSize(coreThreadNum))
        );
    }

    @Override
    protected Object run() throws Exception {
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            if (throwable instanceof Exception) {
                throw new RuntimeException("Hystrix Command Exception: " + throwable.getMessage(), throwable);
            } else {
                System.err.println("Severe Error in Hystrix Command: " + throwable.getMessage());
                throw new RuntimeException("Serious Error Occurred in Command", throwable);
            }
        }
    }

    @Override
    protected Object getFallback() {
        return JSON.parseObject(doHystrix.returnJson(), method.getReturnType());
    }

    public Object access(ProceedingJoinPoint pjp, Method method, Annotation annotation, Object[] args) throws Throwable {
        this.proceedingJoinPoint = pjp;
        this.method = method;
        this.doHystrix = (DoHystrix) annotation;

        return this.execute();
    }
}
