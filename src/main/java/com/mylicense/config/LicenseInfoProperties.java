package com.mylicense.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "license")
@PropertySource(value= {"classpath:config/application.properties"})
public class LicenseInfoProperties {

    private String subject;
    private String publicAlias;
    private String storePass;
    private String licensePath;
    private String publicKeysStorePath;
    private String httpType;

    /**
     * 证书有效结束日期
     */
    private String licenseValidDate;

    /**
     * 证书剩余时间
     */
    private String remainTime;
}
