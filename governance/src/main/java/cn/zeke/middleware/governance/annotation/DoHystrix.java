package cn.zeke.middleware.governance.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zeyuzhao
 * @date 11/30/25 PM8:54
 * @description Description of the Class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoHystrix {

    /*********************************************************************************************
     * Set the properties of HystrixCommand
     *
     * GroupKey:            Specifies which group this command belongs to. It helps organize commands better.
     * CommandKey:          The name of the command.
     * ThreadPoolKey:       The name of the thread pool associated with this command. Commands with the same
     *                      configuration will share the same thread pool. If not specified, the GroupKey
     *                      will be used as the default thread pool name.
     * CommandProperties:   Settings related to the command, including configurations for the circuit breaker,
     *                      isolation strategy, fallback settings, and various monitoring metrics.
     * ThreadPoolProperties: Configurations for the thread pool, such as thread pool size, queue size, etc.
     *********************************************************************************************/

    /**
     * timeout in hystrix queue
     */
    int timeout() default 0;

    /**
     * hystrix group name, each group have its own thread pool
     */
    String groupName() default "GovernGroup";

    /**
     * command name,
     * hystrix will use settings in command to send jobs to different group and have different valve settings
     */
    String commandKey() default "GovernGroup";

    /**
     * the name of thread pool which will be used to run the job
     */
    String threadPoolKey() default "GovernThreadPool";

    /**
     * core thread number in thread pool
     */
    int coreThreadNum() default 10;

    /**
     * the information returned when the task is degraded
     */
    String returnJson() default "";
}
