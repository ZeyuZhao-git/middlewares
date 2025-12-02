package cn.zeke.middleware.methodExt;

import cn.zeke.middleware.methodExt.annotation.DoMethodExt;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zeyuzhao
 * @date 12/2/25 PM2:48
 * @description Description of the Class
 */
@Aspect
@Component
public class DoMethodExtPoint {

    @Pointcut("@annotation(cn.zeke.middleware.methodExt.annotation.DoMethodExt)")
    public void extMethodPointCut() {
    }

    @Around("extMethodPointCut()")
    public Object doExtMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        DoMethodExt doMethodExt = method.getAnnotation(DoMethodExt.class);

        // Extension method should have same parameters as original method
        Method extMethod = joinPoint.getTarget()
                .getClass()
                .getMethod(doMethodExt.methodName(), method.getParameterTypes());
        Class<?> returnType = extMethod.getReturnType();

        // Extension method should return boolean type
        if (!returnType.getName()
                .equals("boolean")) {
            throw new RuntimeException("annotation @DoMethodExt set method：" + doMethodExt.methodName() + " returnType is not boolean");
        }

        return (boolean) extMethod.invoke(joinPoint.getThis(),
                                          joinPoint.getArgs()) ? joinPoint.proceed() : JSON.parseObject(doMethodExt.returnJson(),
                                                                                                        method.getReturnType());
    }

    Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return joinPoint.getTarget()
                .getClass()
                .getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
