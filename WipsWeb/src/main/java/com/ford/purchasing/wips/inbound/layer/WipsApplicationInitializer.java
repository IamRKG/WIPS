package com.ford.purchasing.wips.inbound.layer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ford.it.exception.FordExceptionAttributes;
import com.ford.it.properties.PropertyManager;
import com.ford.it.util.wsl.WSLCredentialsProviderInitializer;
import com.ford.it.wscore.context.WscConfigInitializer;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.exception.WipsInitializationException;
import com.ford.purchasing.wips.inbound.layer.common.UserSessionCacheCleaner;

/**
 * Application Life cycle Listener implementation class WipsApplicationInitializer
 *
 * @since v. 1.0
 */
public class WipsApplicationInitializer implements ServletContextListener {

    private static final String CLASS_NAME = WipsApplicationInitializer.class.getName();

    public void contextInitialized(final ServletContextEvent contextEvent) {
        initializeDynaProp();
        initializeWsConfig(contextEvent);
        initializeWSLCredentialProvider(contextEvent);
        initializeCacheCleaner();
    }

    private void initializeWSLCredentialProvider(final ServletContextEvent contextEvent) {
        new WSLCredentialsProviderInitializer().contextInitialized(contextEvent);
    }

    private void initializeWsConfig(final ServletContextEvent contextEvent) {
        new WscConfigInitializer().contextInitialized(contextEvent);
    }

    public void contextDestroyed(final ServletContextEvent contextEvent) {
    }

    private void initializeCacheCleaner() {
        UserSessionCacheCleaner.initialize();
    }

    private void initializeDynaProp() {
        final String methodName = "initializeDynaProp";
        final PropertyManager pm = PropertyManager.getInstance();
        try {
            pm.initDynaProp();
            pm.loadFromDB(WipsConstant.WIPS_MOBILE);
            pm.loadFromDB(WipsConstant.WIPS_DATABASE);
            pm.loadFromDB(WipsConstant.WSL_PERSISTENT_COOKIES_CONFIG);
            pm.loadFromDB(WipsConstant.WSC_CONFIG);
            pm.loadFromDB(WipsConstant.WIPS_IMS_CONNECTOR_CONFIG);
        } catch (final Exception e) {
            throw new WipsInitializationException(
                new FordExceptionAttributes.Builder(CLASS_NAME, methodName).build(),
                e.getMessage(), e);
        }
    }

}
