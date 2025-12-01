package cn.zeke.middleware.hystrix.valve;

import cn.zeke.middleware.hystrix.annotation.DoHystrix;
import cn.zeke.middleware.hystrix.valve.Impl.HystrixValveImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date 11/30/25 PM9:12
 * @description Description of the Class
 */
@Aspect
@Component
public class DoHystrixPoint {

    @Pointcut("@annotation(cn.zeke.middleware.hystrix.annotation.DoHystrix)")
    public void hystrixPoint() {

    }

    @Around("hystrixPoint() && @annotation(doGovern)")
    public Object doRouter(ProceedingJoinPoint jp, DoHystrix doGovern) throws Throwable {
        IValveService valveService = new HystrixValveImpl(doGovern.groupName(),
                                                          doGovern.commandKey(),
                                                          doGovern.threadPoolKey(),
                                                          doGovern.coreThreadNum(),
                                                          doGovern.timeout());
        return valveService.access(jp, getMethod(jp), doGovern, jp.getArgs());
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget()
                .getClass()
                .getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
