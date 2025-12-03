package cn.zeke.middleware.governance.service;

import cn.zeke.middleware.governance.IGovernanceStrategy;
import cn.zeke.middleware.governance.annotation.WhitelistFilter;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zeyuzhao
 * @date 12/2/25 PM5:55
 * @description Description of the Class
 */
@Service
public class WhiteListService implements IGovernanceStrategy {
    @Resource(name = "whitelist")
    private List<String> whitelist;

    @Override
    public boolean supports(Object annotation) {
        return annotation instanceof WhitelistFilter;
    }

    @Override
    public Object access(ProceedingJoinPoint joinPoint, Method method, Annotation annotation, Object[] args) throws Throwable {
        WhitelistFilter whitelistFilter = (WhitelistFilter) annotation;

        // Get value of target field
        String fieldValue = getFiledValue(whitelistFilter.key(), joinPoint.getArgs());

        if (StringUtils.isEmpty(fieldValue) || !whitelist.contains(fieldValue)) {
            return JSON.parseObject(whitelistFilter.returnJson(),
                                    method.getReturnType());
        }

        return null;
    }

    private String getFiledValue(String filed, Object[] args) {
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isEmpty(filedValue)) {
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
}
