package cn.zeke.middleware.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zeyuzhao
 * @date 12/2/25 AM10:55
 * @description Description of the Class
 */
public class Constants {
    public static Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
}
