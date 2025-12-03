package cn.zeke.middleware.governance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zeyuzhao
 * @date 12/2/25 AM10:57
 * @description Description of the Class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoRateLimiter {
    double permitsPerSecond() default 0D;
    String returnJson() default "";
}
