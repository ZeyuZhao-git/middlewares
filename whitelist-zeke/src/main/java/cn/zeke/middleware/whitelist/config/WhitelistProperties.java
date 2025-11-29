package cn.zeke.middleware.whitelist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zeyuzhao
 * @date PM4:30
 * @description Description of the Class
 */
@ConfigurationProperties("zeke.whitelist")
public class WhitelistProperties {

    private String whitelist;

    public String getWhitelist() {
        return whitelist;
    }
}
