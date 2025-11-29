package cn.zeke.middleware.whitelist.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zeyuzhao$
 * @date 11/29/25$
 * @description Description of the Class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WhitelistFilter {
    String key() default "";
    String errorMsg() default "Default Error Message";
}
