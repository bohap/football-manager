package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.exception.UserException;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.AuthenticateUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.AuthenticateUserResponse;
import com.android.finki.mpip.footballdreamteam.rest.response.AuthenticationFailedResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.LoginActivity;
import com.android.finki.mpip.footballdreamteam.utility.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.IllegalStateException;
import java.net.SocketTimeoutException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 06.08.2016.
 */
public class LoginActivityPresenter implements Callback<AuthenticateUserResponse> {

    private Logger logger = LoggerFactory.getLogger(LoginActivityPresenter.class);
    private LoginActivity activity;
    private SharedPreferences preferences;
    private final UserDBService userDBService;
    private final AuthApi authApi;

    private String AUTH_USER_ID_KEY;
    private boolean isSending = false;

    public LoginActivityPresenter(LoginActivity activity, SharedPreferences preferences,
                                  UserDBService userDBService, AuthApi authApi) {
        this.activity = activity;
        this.preferences = preferences;
        this.userDBService = userDBService;
        this.authApi = authApi;

        this.AUTH_USER_ID_KEY = activity.getString(R.string.preference_auth_user_id_key);
    }

    /**
     * Checks if the given email is valid.
     *
     * @param email string email to be checked
     * @return whatever the email is valid
     */
    private boolean isEmailValid(String email) {
        if (StringUtils.isEmpty(email)) {
            activity.errorEmail(LoginActivity.EMAIL_ERROR.REQUIRED);
            return false;
        }
        if (!StringUtils.isValidEmail(email)) {
            activity.errorEmail(LoginActivity.EMAIL_ERROR.INVALID);
            return false;
        }
        activity.okEmail();
        return true;
    }

    /**
     * Checks if the given password is valid.
     *
     * @param password password to be checked
     * @return whatever the password is valid
     */
    private boolean isPasswordValid(String password) {
        if (StringUtils.isEmpty(password)) {
            activity.errorPassword();
            return false;
        }
        activity.okPassword();
        return true;
    }

    /**
     * Login the user.
     *
     * @param email    user email
     * @param password user password
     */
    public void login(String email, String password) {
        boolean valid = true;
        valid = this.isEmailValid(email);
        valid = this.isPasswordValid(password) && valid;
        if (valid && ! isSending) {
            isSending = true;
            logger.info("sending login request");
            activity.showLoading();
            AuthenticateUserRequest request = new AuthenticateUserRequest(email, password);
            Call<AuthenticateUserResponse> call = authApi.login(request);
            call.enqueue(this);
        }
    }

    /**
     * Called when the login request is successful.
     *
     * @param call     retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<AuthenticateUserResponse> call,
                           Response<AuthenticateUserResponse> response) {
        isSending = false;
        if (response.isSuccessful()) {
            logger.info("Login request succeeded");
            this.loginSuccess(response);
        } else if (response.code() == 401) {
            logger.info("login response return 401");
            this.authenticationFailed(response);
        } else {
            logger.info(String.format("login response with code %d", response.code()));
            activity.showServerErrorMessage();
        }
    }

    /**
     * Called when the login is successful.
     *
     * @param response server response
     */
    private void loginSuccess(Response<AuthenticateUserResponse> response) {
        AuthenticateUserResponse body = response.body();
        User user = new User(body.getId(), body.getName(), body.getEmail(), new Date(),
                new Date(), body.getJwtToken());
        userDBService.open();
        try {
            if (!userDBService.exists(user.getId())) {
                userDBService.store(user);
            } else {
                userDBService.update(user);
            }
        } catch (RuntimeException exp) {
            exp.printStackTrace();
            activity.showAppErrorMessage();
            userDBService.close();
            return;
        }
        userDBService.close();
        preferences.edit().putInt(AUTH_USER_ID_KEY, user.getId()).apply();
        ((MainApplication) activity.getApplication()).createUserComponent(user);
        activity.successfulLogin();
    }

    /**
     * Called when the user authentication failed.
     *
     * @param response server response
     */
    private void authenticationFailed(Response<AuthenticateUserResponse> response) {
        Gson gson = new Gson();
        try {
            AuthenticationFailedResponse error = gson.fromJson(response.errorBody().string(),
                    AuthenticationFailedResponse.class);
            activity.failedLogin(error.getErrors());
        } catch (IOException | JsonSyntaxException exp) {
            exp.printStackTrace();
            activity.loginError();
            activity.showServerErrorMessage();
        }
    }

    /**
     * Called when the login request failed.
     *
     * @param call retrofit call
     * @param t exception that has throw
     */
    @Override
    public void onFailure(Call<AuthenticateUserResponse> call, Throwable t) {
        logger.error("login request error");
        isSending = false;
        activity.loginError();
        t.printStackTrace();
        if (t instanceof SocketTimeoutException) {
            activity.showConnectionTimeoutMessage();
        } else {
            activity.showServerErrorMessage();
        }
    }
}
