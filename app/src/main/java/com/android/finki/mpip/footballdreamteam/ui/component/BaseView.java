package com.android.finki.mpip.footballdreamteam.ui.component;

/**
 * Created by Borce on 08.09.2016.
 */
public interface BaseView {

    void showNotAuthenticated();

    void showInternalServerError(String message);

    void showInternalServerError();

    void showSocketTimeout();

    void showNoInternetConnection();
}
