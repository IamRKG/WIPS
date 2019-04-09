package com.ford.purchasing.wips.inbound.layer.common;

import java.util.List;

import javax.inject.Singleton;

import com.ford.it.cache.Cache;
import com.ford.it.cache.CacheManager;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.StringUtil;

@SuppressWarnings("javadoc")
@Singleton
public class UserSessionCacheManager {

    private static final String CLASS_NAME = "UserSessionCacheManager";
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    private static final String USER_SESSION = "UserSession";

    private final char[] suffix = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                                   'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                                   'V', 'W', 'X', 'Y', '0', '1', '2', '3', '4', '5', '6', '7',
                                   '8', '9'};

    private final Cache cache = CacheManager.getInstance().getCache(USER_SESSION);

    private long sessionTimeOut = Long.parseLong(
            PropertyManager.getInstance().getString(WipsConstant.WIPS_SESSION_TIMEOUT));

    private String generateUniqueLTerm(final String racfId) {
        final String racfInputId =
                racfId.length() < 7 ? racfId
                                      + StringUtil.createBlankSpaces(7 - racfId.length())
                                    : racfId;
        String lTerm = racfInputId + this.suffix[0];
        for (int i = 1; i < this.suffix.length && contains(lTerm); i++) {
            lTerm = racfInputId + this.suffix[i];
        }
        return lTerm;
    }

    public void deleteSessionEntry(final String lterm) {
        this.cache.remove(lterm);
    }

    public UserSession createNewUserSession(final String racfId) {
        final UserSession userSession = new UserSession(generateUniqueLTerm(racfId));
        final UserProfile userProfile = new UserProfile();
        userProfile.setUserRacfId(racfId);
        userSession.setUserProfile(userProfile);
        saveSessionInCache(userSession);
        return userSession;
    }

    public boolean isValidUserSession(final String lterm) {
        boolean isUserValid = false;
        if (contains(lterm)) {
            final UserSession userSession = retrieveUserSession(lterm);
            if (userSession.isValid(this.sessionTimeOut)) {
                isUserValid = true;
                userSession.updateLastUpdateTime();
            } else {
                log.info("Session timeout for the lterm -->" + lterm);
                this.deleteSessionEntry(lterm);
            }
        } else {
            log.info("Lterm not found in the cache -->" + lterm);
        }
        return isUserValid;
    }

    protected boolean contains(final String lTerm) {
        return this.cache != null && this.cache.get(lTerm) != null;
    }

    private void saveSessionInCache(final UserSession userSession) {
        this.cache.put(userSession.getLterm(), userSession);
    }

    public UserSession retrieveUserSession(final String lTerm) {
        return this.cache.get(lTerm);
    }

    public UserProfile retrieveUserProfile(final String lTerm) {
        return retrieveUserSession(lTerm).getUserProfile();
    }

    public void deleteStaleSessions() {
        final List<String> lTerms = this.cache.getKeys();
        UserSession userSession;
        for (final String lTerm : lTerms) {
            userSession = retrieveUserSession(lTerm);
            if (!userSession.isValid(this.sessionTimeOut)) {
                deleteSessionEntry(lTerm);
            }
        }
    }

    protected int retrieveCacheCount() {
        return this.cache.getKeys().size();
    }

    protected void setSessionTimeOutValue(final int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

}
