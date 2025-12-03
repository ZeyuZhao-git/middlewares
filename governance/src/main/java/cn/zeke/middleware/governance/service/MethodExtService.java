package cn.zeke.middleware.governance.service;

import cn.zeke.middleware.governance.IGovernanceStrategy;
import cn.zeke.middleware.governance.annotation.DoMethodExt;
import com.alibaba.fastjson.JSON;
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
public class MethodExtService implements IGovernanceStrategy {
    @Override
    public boolean supports(Object annotation) {
        return annotation instanceof DoMethodExt;
    }

    @Override
    public Object access(ProceedingJoinPoint joinPoint, Method method, Annotation annotation, Object[] args) throws Throwable {
        DoMethodExt doMethodExt = (DoMethodExt) annotation;

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

        boolean invoke = (boolean) extMethod.invoke(joinPoint.getThis(), joinPoint.getArgs());

        return invoke ? null : JSON.parseObject(doMethodExt.returnJson(),
                                                method.getReturnType());
    }
}
