package com.ford.purchasing.wips.inbound.layer.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;

@SuppressWarnings("javadoc")
public class UserSessionCacheCleaner implements Runnable {
    private static final String CLASS_NAME = "UserSessionCacheCleaner";
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    private ScheduledExecutorService cacheCleanupScheduler = Executors
            .newScheduledThreadPool(1);
    private int timePeriod = 60;

    private final UserSessionCacheManager cacheManager = new UserSessionCacheManager();

    public static void initialize() {
        log.info("User Session Cache Cleaner initialized");
        final UserSessionCacheCleaner userSessionCacheCleaner = new UserSessionCacheCleaner();
        userSessionCacheCleaner.scheduleCleaner();
    }

    private UserSessionCacheCleaner() {
    }

    private void scheduleCleaner() {
        this.cacheCleanupScheduler.scheduleAtFixedRate(this, this.timePeriod,
                this.timePeriod, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        log.info("User Session currently in the system --> "
                 + this.cacheManager.retrieveCacheCount());
        this.cacheManager.deleteStaleSessions();
    }

}
