package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.User;

import java.util.List;

/**
 * Created by Borce on 13.09.2016.
 */
public interface RegisterView extends BaseView {

    enum EMAIL_ERROR {
        REQUIRED,
        INVALID
    }

    enum REPEAT_PASSWORD_ERROR {
        REQUIRED,
        NOT_MATCH
    }

    void showNameError();

    void showNameOk();

    void showEmailError(EMAIL_ERROR error);

    void showEmailOk();

    void showPasswordError();

    void showPasswordOk();

    void showRepeatPasswordError(REPEAT_PASSWORD_ERROR error);

    void showRepeatPasswordOk();

    void showRegistering();

    void showRegisteringSuccess();

    void showRegisteringFailed(List<String> errors);

    void showRegisteringFailed();
}
