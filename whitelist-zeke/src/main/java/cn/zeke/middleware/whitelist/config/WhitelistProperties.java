package cn.zeke.middleware.whitelist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zeyuzhao
 * @date PM4:30
 * @description Description of the Class
 */
@Component
@ConfigurationProperties(prefix = "zeke")
public class WhitelistProperties {

    private String whitelist;

    public String getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(String whitelist) {
        System.out.println("Whitelist loaded: " + whitelist);
        this.whitelist = whitelist;
    }
}
