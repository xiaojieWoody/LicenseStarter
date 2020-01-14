package com.mylicense.service;

import com.mylicense.common.ResMsg;
import com.mylicense.common.SpringContextUtils;
import com.mylicense.config.LicenseInfoProperties;
import com.mylicense.license.manager.LicenseManagerHolder;
import com.mylicense.license.param.CustomKeyStoreParam;
import com.mylicense.license.param.LicenseVerifyParam;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

@Slf4j
public class LicenseVerifyService {

    /**
     * 传入License路径
     * 校验License证书
     */
    public ResMsg verify(String licensePath){
        LicenseVerifyParam param = new LicenseVerifyParam();
        LicenseInfoProperties licenseConfig = SpringContextUtils.getBeanByClass(LicenseInfoProperties.class);
        if(!StringUtils.isEmpty(licensePath)) {
            licenseConfig.setLicensePath(licensePath);
        }
        BeanUtils.copyProperties(licenseConfig, param);
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
            log.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",format.format(licenseContent.getNotBefore()),format.format(licenseContent.getNotAfter())));

            // 证书结束日期
            licenseConfig.setLicenseValidDate(format.format(licenseContent.getNotAfter()));
            // 证书剩余时间
            licenseConfig.setRemainTime(getRemainTime(new Date(), licenseContent.getNotAfter()));
            log.warn("证书有效期剩余时间{}",licenseConfig.getRemainTime());

            return new ResMsg(200, "success", "", null);
        }catch (Exception e){
            log.error("证书校验失败！",e);
            // 关闭应用
            shutdownApp();
            return new ResMsg(200, "fail", "证书校验失败！", null);
        }
    }

    /**
     * 关闭应用
     */
    private void shutdownApp() {
        // 关闭应用
        log.error("关闭应用......................");
        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) applicationContext;
        ctx.close();
    }

    /**
     * 初始化证书生成参数
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param){
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerifyService.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerifyService.class
                ,param.getPublicKeysStorePath()
                ,param.getPublicAlias()
                ,param.getStorePass()
                ,null);

        return new DefaultLicenseParam(param.getSubject()
                ,preferences
                ,publicStoreParam
                ,cipherParam);
    }

    /**
     * 日期相减
     * @param beginTime
     * @param endTime
     * @return
     */
    public String getRemainTime(Date beginTime, Date endTime) {
        if(beginTime == null || endTime == null) {
            return null;
        }
        if(endTime.before(beginTime)) {
            return null;
        }

        long diff = endTime.getTime() - beginTime.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffDays +"天" + diffHours + "小时" + diffMinutes + "分" + diffSeconds + "秒";
    }
}
