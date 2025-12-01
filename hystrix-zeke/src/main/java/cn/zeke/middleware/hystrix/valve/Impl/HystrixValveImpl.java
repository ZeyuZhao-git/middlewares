package cn.zeke.middleware.hystrix.valve.Impl;

import cn.zeke.middleware.hystrix.annotation.DoHystrix;
import cn.zeke.middleware.hystrix.valve.IValveService;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.*;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date PM8:51
 * @description Description of the Class
 */
public class HystrixValveImpl extends HystrixCommand<Object> implements IValveService {

    private ProceedingJoinPoint proceedingJoinPoint;
    private Method method;
    private DoHystrix doHystrix;

    public HystrixValveImpl(String groupName,
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
    public Object access(ProceedingJoinPoint pjp, Method method, DoHystrix doHystrix, Object[] args) throws Throwable {
        this.proceedingJoinPoint = pjp;
        this.method = method;
        this.doHystrix = doHystrix;

        return this.execute();
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
        return JSON.parseObject(doHystrix.errorMsg(), method.getReturnType());
    }
}
