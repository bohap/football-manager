package com.android.finki.mpip.footballdreamteam.shadow;

import android.security.NetworkSecurityPolicy;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Created by Borce on 06.08.2016.
 */
@Implements(NetworkSecurityPolicy.class)
public class ShadowNetworkSecurityPolicy {

    @Implementation
    public static NetworkSecurityPolicy getInstance() {
        try {
            Class<?> shadow = Class.forName("android.security.NetworkSecurityPolicy");
            return (NetworkSecurityPolicy) shadow.newInstance();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Implementation
    public boolean isCleartextTrafficPermitted() {
        return true;
    }
}
