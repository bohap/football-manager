package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.exception.NotAuthenticatedException;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.AuthenticateUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.AuthenticateUserResponse;
import com.android.finki.mpip.footballdreamteam.rest.response.AuthenticationFailedResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.LoginActivity;
import com.android.finki.mpip.footballdreamteam.ui.component.LoginView;
import com.android.finki.mpip.footballdreamteam.utility.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 06.08.2016.
 */
public class LoginViewPresenter extends BasePresenter implements Callback<AuthenticateUserResponse> {

    private Logger logger = LoggerFactory.getLogger(LoginViewPresenter.class);
    private LoginView view;
    private SharedPreferences preferences;
    private final UserDBService userDBService;
    private final AuthApi authApi;

    private String AUTH_USER_ID_KEY;
    private String JWT_TOKEN_KEY;
    private boolean isSending = false;

    public LoginViewPresenter(LoginView view, SharedPreferences preferences, Context context,
                              UserDBService userDBService, AuthApi authApi) {
        this.view = view;
        this.preferences = preferences;
        this.userDBService = userDBService;
        this.authApi = authApi;

        this.AUTH_USER_ID_KEY = context.getString(R.string.preference_auth_user_id_key);
        this.JWT_TOKEN_KEY = context.getString(R.string.preference_jwt_token_key);
    }

    /**
     * Checks if the given email is valid.
     *
     * @param email string email to be checked
     * @return whatever the email is valid
     */
    private boolean isEmailValid(String email) {
        if (StringUtils.isEmpty(email)) {
            view.showEmailError(LoginActivity.EMAIL_ERROR.REQUIRED);
            return false;
        }
        if (!StringUtils.isValidEmail(email)) {
            view.showEmailError(LoginActivity.EMAIL_ERROR.INVALID);
            return false;
        }
        view.showEmailOk();
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
            view.showPasswordError();
            return false;
        }
        view.showPasswordOk();
        return true;
    }

    /**
     * Login the user.
     *
     * @param email    user email
     * @param password user password
     */
    public void login(String email, String password) {
        boolean valid = this.isEmailValid(email);
        valid = this.isPasswordValid(password) && valid;
        if (valid && !isSending) {
            isSending = true;
            logger.info("sending login request");
            view.showLogging();
            AuthenticateUserRequest request = new AuthenticateUserRequest(email, password);
            authApi.login(request).enqueue(this);
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
        logger.info("Login request succeeded");
        this.loginSuccess(response);
    }

    /**
     * Called when the login request failed.
     *
     * @param call retrofit call
     * @param t    exception that has throw
     */
    @Override
    public void onFailure(Call<AuthenticateUserResponse> call, Throwable t) {
        logger.error("login request error");
        t.printStackTrace();
        isSending = false;
        if (t instanceof NotAuthenticatedException) {
            this.authenticationFailed(((NotAuthenticatedException) t).getResponse());
        } else {
            view.showLoginFailed();
            super.onRequestFailed(view, t);
        }
    }

    /**
     * Called when the login is successful.
     *
     * @param response server response
     */
    private void loginSuccess(Response<AuthenticateUserResponse> response) {
        AuthenticateUserResponse body = response.body();
        User user = new User(body.getId(), body.getName(),
                body.getEmail(), new Date(), new Date());
        userDBService.open();
        try {
            if (!userDBService.exists(user.getId())) {
                userDBService.store(user);
            } else {
                userDBService.update(user);
            }
        } catch (RuntimeException exp) {
            exp.printStackTrace();
            view.showLoginFailed();
            view.showInternalServerError();
            userDBService.close();
            return;
        }
        userDBService.close();
        preferences.edit().putInt(AUTH_USER_ID_KEY, user.getId()).apply();
        preferences.edit().putString(JWT_TOKEN_KEY, body.getJwtToken()).apply();
        view.createUserComponent(user);
        view.showLoginSuccessful();
    }

    /**
     * Called when the user authentication failed.
     *
     * @param response server response
     */
    private void authenticationFailed(okhttp3.Response response) {
        Gson gson = new Gson();
        try {
            AuthenticationFailedResponse error = gson.fromJson(response.body().string(),
                    AuthenticationFailedResponse.class);
            view.showLoginFailed(error.getErrors());
        } catch (IOException | JsonSyntaxException exp) {
            exp.printStackTrace();
            view.showLoginFailed();
            view.showInternalServerError();
        }
    }
}
