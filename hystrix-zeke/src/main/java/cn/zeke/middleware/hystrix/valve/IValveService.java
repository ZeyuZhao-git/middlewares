package cn.zeke.middleware.hystrix.valve;

import cn.zeke.middleware.hystrix.annotation.DoHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date PM8:51
 * @description Interface of valve service
 */
public interface IValveService {
    Object access(ProceedingJoinPoint pjp, Method method, DoHystrix doHystrix, Object[] args) throws Throwable;
}
