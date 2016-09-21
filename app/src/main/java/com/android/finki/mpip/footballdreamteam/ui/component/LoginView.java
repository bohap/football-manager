package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.User;

import java.util.List;

/**
 * Created by Borce on 08.09.2016.
 */
public interface LoginView extends BaseView {

    enum EMAIL_ERROR {
        REQUIRED,
        INVALID
    }

    void showEmailError(EMAIL_ERROR error);

    void showEmailOk();

    void showPasswordError();

    void showPasswordOk();

    void showLogging();

    void showLoggingSuccess();

    void showLoggingFailed(List<String> errors);

    void showLoggingFailed();
}
