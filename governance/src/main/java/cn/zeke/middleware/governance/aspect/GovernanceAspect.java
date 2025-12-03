package cn.zeke.middleware.governance.aspect;

import cn.zeke.middleware.governance.IGovernanceStrategy;
import cn.zeke.middleware.governance.annotation.DoHystrix;
import cn.zeke.middleware.governance.service.HystrixService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zeyuzhao
 * @date 12/2/25 PM5:52
 * @description Description of the Class
 */
@Aspect
@Component
public class GovernanceAspect {
    @Autowired
    private List<IGovernanceStrategy> strategies;

    @Pointcut("@annotation(cn.zeke.middleware.governance.annotation.DoMethodExt)" +
            "|| @annotation(cn.zeke.middleware.governance.annotation.DoRateLimiter)" +
            "|| @annotation(cn.zeke.middleware.governance.annotation.WhitelistFilter)")
    public void governancePointcut() {
    }

    @Pointcut("@annotation(cn.zeke.middleware.governance.annotation.DoHystrix)")
    public void hystrixPointCut() {
    }

    @Around("governancePointcut()")
    public Object doGovernance(ProceedingJoinPoint point) throws Throwable {
        Method method = getMethod(point);
        Annotation[] annotations = method.getAnnotations();

        Object result = null;
        for(Annotation annotation : annotations) {
            IGovernanceStrategy matchingStrategy = findMatchingStrategy(annotation);
            if(null != matchingStrategy) {
                result = matchingStrategy.access(point, method, annotation, point.getArgs());
                if(result != null) {
                    return result;
                }
            }
        }

        return point.proceed();
    }

    @Around("hystrixPointCut()")
    public Object doHystrix(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        DoHystrix doHystrix = method.getAnnotation(DoHystrix.class);
        HystrixService valveService = new HystrixService(doHystrix.groupName(),
                                                         doHystrix.commandKey(),
                                                         doHystrix.threadPoolKey(),
                                                         doHystrix.coreThreadNum(),
                                                         doHystrix.timeout());
        return valveService.access(joinPoint, getMethod(joinPoint), doHystrix, joinPoint.getArgs());
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

    private IGovernanceStrategy findMatchingStrategy(Annotation annotation) {
        for (IGovernanceStrategy strategy : strategies) {
            if(strategy.supports(annotation)) {
                return strategy;
            }
        }
        return null;
    }
}
