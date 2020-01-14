package com.mylicense.config;

import com.mylicense.common.SpringContextUtils;
import com.mylicense.service.LicenseVerifyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({LicenseVerifyService.class})
@EnableConfigurationProperties(LicenseInfoProperties.class)
public class LicenseAutoConfigure {

    /**
     * License验证Service
     * @return
     */
    @Bean
    LicenseVerifyService licenseVerifyService (){
        return  new LicenseVerifyService();
    }

    @Bean
    SpringContextUtils SpringContextUtils() {
        return new SpringContextUtils();
    }
}
