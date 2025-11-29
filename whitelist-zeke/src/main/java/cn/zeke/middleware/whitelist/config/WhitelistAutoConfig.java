package cn.zeke.middleware.whitelist.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zeyuzhao$
 * @date 11/29/25$
 * @description Description of the Class
 */
@Configuration
@ConditionalOnClass(WhitelistProperties.class)
@EnableConfigurationProperties(WhitelistProperties.class)
public class WhitelistAutoConfig {
    @Bean("whitelist")
    @ConditionalOnMissingBean
    public List<String> whitelist(WhitelistProperties whitelistProperties) {
        if(StringUtils.isEmpty(whitelistProperties.getWhitelist())) {
            return Collections.EMPTY_LIST;
        }

        String[] userArray = whitelistProperties.getWhitelist().split(",");
        return Arrays.asList(userArray);
    }
}
