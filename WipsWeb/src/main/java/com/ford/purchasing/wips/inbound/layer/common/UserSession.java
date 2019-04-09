package com.ford.purchasing.wips.inbound.layer.common;

import com.ford.purchasing.wips.common.layer.UserProfile;

@SuppressWarnings("javadoc")
public class UserSession {

    private String lterm;
    private long lastUpdateTime;
    private UserProfile userProfile;

    public UserSession(final String lterm) {
        this.lterm = lterm;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public UserSession() {
        super();
    }

    public String getLterm() {
        return this.lterm;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(final UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void updateLastUpdateTime() {
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public boolean isValid(final long sessionTimeOutValue) {
        return System.currentTimeMillis() - this.lastUpdateTime <= sessionTimeOutValue;
    }

}
