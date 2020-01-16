package com.mylicense.license.runnable;

import com.mylicense.common.SpringContextUtils;
import com.mylicense.config.LicenseInfoProperties;
import com.mylicense.service.LicenseVerifyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LicenseRunnable implements Runnable{

    /**
     * 验证License文件有效性 任务
     */
    @Override
    public void run() {
        LicenseVerifyService licenseVerifyService = SpringContextUtils.getBeanByClass(LicenseVerifyService.class);
        LicenseInfoProperties licenseConfig = SpringContextUtils.getBeanByClass(LicenseInfoProperties.class);
        // 事先约定License文件存储在服务器的目录
        try {
            log.info("+++++++++++License Verify Begin++++++++++++");
            licenseVerifyService.verify();
            log.info("+++++++++++License Verify End++++++++++++");
        } catch (Exception e) {
            log.error("License文件验证异常.....", e);
        }
    }
}
