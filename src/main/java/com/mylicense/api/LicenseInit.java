package com.mylicense.api;

import com.mylicense.common.ResMsg;
import com.mylicense.license.runnable.LicenseRunnable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * License对外初始化接口
 */
@Slf4j
public class LicenseInit {

    public static ResMsg init() {

        // 周期性验证License有效性
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("license-verify-schedule-pool-%d").daemon(true).build());
        // 单个线程周期执行License验证
        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(new LicenseRunnable(), 0, 1, TimeUnit.SECONDS);
        try {
            // 阻塞
            Object o = scheduledFuture.get();
        } catch (InterruptedException e) {
            log.error("",e);
        } catch (ExecutionException e) {
            log.error("",e);
        }
        return new ResMsg(-1, "fail", "证书验证失败!", null);
    }
}
