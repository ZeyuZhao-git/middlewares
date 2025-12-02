package cn.zeke.middleware.methodExt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zeyuzhao
 * @date 12/2/25 PM2:48
 * @description Description of the Class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoMethodExt {
    String methodName() default "";
    String returnJson() default "";
}
