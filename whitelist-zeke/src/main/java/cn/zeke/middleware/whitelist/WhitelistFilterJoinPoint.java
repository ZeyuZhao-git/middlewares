package cn.zeke.middleware.whitelist;

import cn.zeke.middleware.whitelist.annotation.WhitelistFilter;
import cn.zeke.middleware.whitelist.config.WhitelistAutoConfig;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zeyuzhao
 * @date PM4:37
 * @description Description of the Class
 */
@Aspect
@Component
public class WhitelistFilterJoinPoint {

    @Resource(name = "whitelist")
    private List<String> whitelist;

    @PostConstruct
    public void init() {
        System.out.println("WhitelistAspect 被加载到 Spring 容器中");
    }

    @Pointcut("@annotation(cn.zeke.middleware.whitelist.annotation.WhitelistFilter)")
    public void whitelistPointcut() {
    }

    @Around("whitelistPointcut()")
    public Object doRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("进入切片逻辑");
        // 获取方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 这里不能通过直接 joinPoint.getMethod() 获得目标方法，因为可能返回接口的方法
        // 先通过 joinPoint.getTarget() 获得注解方法的类，再通过类和方法签名获得方法
        Method method = joinPoint.getTarget()
                .getClass()
                .getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        // 获取注解
        WhitelistFilter whitelistFilter = method.getAnnotation(WhitelistFilter.class);

        // 获取 target 字段 value
        String fieldValue = getFiledValue(whitelistFilter.key(), joinPoint.getArgs());

        // 判断 value 是否在 whitelist 中
        if(StringUtils.isEmpty(fieldValue) || !whitelist.contains(fieldValue)) {
            return returnObject(whitelistFilter, method);
        }

        return joinPoint.proceed();
    }

    // 获取属性值
    private String getFiledValue(String filed, Object[] args) {
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (null == filedValue || "".equals(filedValue)) {
                    filedValue = BeanUtils.getProperty(arg, filed);
                } else {
                    break;
                }
            } catch (Exception e) {
                if (args.length == 1) {
                    return args[0].toString();
                }
            }
        }
        return filedValue;
    }

    // 返回对象
    private Object returnObject(WhitelistFilter whiteList, Method method) throws IllegalAccessException, InstantiationException {
        Class<?> returnType = method.getReturnType();
        String returnJson = whiteList.errorMsg();
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson, returnType);
    }
}
