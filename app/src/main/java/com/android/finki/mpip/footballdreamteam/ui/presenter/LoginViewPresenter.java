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
    private Call<AuthenticateUserResponse> call;
    private String AUTH_USER_ID_KEY;
    private String JWT_TOKEN_KEY;
    private boolean requestSending = false;
    private boolean viewLayoutCreated = false;
    private String email;
    private String password;

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
     * Called when the view is visible to the user.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        this.viewLayoutCreated = true;

    }

    /**
     * Login the user.
     *
     * @param email    user email
     * @param password user password
     */
    public void login(String email, String password) {
        if (viewLayoutCreated && !requestSending) {
            boolean valid = this.isEmailValid(email);
            valid = this.isPasswordValid(password) && valid;
            if (valid) {
                requestSending = true;
                logger.info("sending login request");
                view.showLogging();
                this.email = email;
                this.password = password;
                AuthenticateUserRequest request = new AuthenticateUserRequest(email, password);
                call = authApi.login(request);
                call.enqueue(this);
            }
        }
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
     * Called when the login request is successful.
     *
     * @param call     retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<AuthenticateUserResponse> call,
                           Response<AuthenticateUserResponse> response) {
        logger.info("login request success");
        requestSending = false;
        this.call = null;
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
        requestSending = false;
        if (call.isCanceled()) {
            logger.info("login request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                if (t instanceof NotAuthenticatedException) {
                    this.authenticationFailed(((NotAuthenticatedException) t).getResponse());
                } else {
                    view.showLoggingFailed();
                    super.onRequestFailed(view, t);
                }
            }
        }
        this.call = null;
    }

    /**
     * Save the server response int he local database.
     *
     * @param response server response
     */
    private void loginSuccess(Response<AuthenticateUserResponse> response) {
        AuthenticateUserResponse body = response.body();
        User user = new User(body.getId(), body.getName(), email, password, new Date(), new Date());
        userDBService.open();
        try {
            if (!userDBService.exists(user.getId())) {
                userDBService.store(user);
            } else {
                userDBService.update(user);
            }
        } catch (RuntimeException exp) {
            logger.info("user saving/updating failed");
            exp.printStackTrace();
            if (viewLayoutCreated) {
                view.showLoggingFailed();
                view.showInternalServerError();
            }
            userDBService.close();
            return;
        }
        userDBService.close();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(AUTH_USER_ID_KEY, user.getId());
        editor.putString(JWT_TOKEN_KEY, body.getJwtToken());
        editor.apply();
        if (viewLayoutCreated) {
            view.showLoggingSuccess();
        }
    }

    /**
     * Try to extract the messages from the server response.
     *
     * @param response server response
     */
    private void authenticationFailed(okhttp3.Response response) {
        if (response != null) {
            Gson gson = new Gson();
            try {
                AuthenticationFailedResponse error = gson.fromJson(response.body().string(),
                        AuthenticationFailedResponse.class);
                view.showLoggingFailed(error.getErrors());
            } catch (IOException | JsonSyntaxException exp) {
                exp.printStackTrace();
                view.showLoggingFailed();
                view.showInternalServerError();
            }
        } else {
            view.showLoggingFailed();
            view.showInternalServerError();
        }
    }

    /**
     * Called when the view is not anymore visible.
     */
    public void onViewLayoutDestroyed() {
        logger.info("onViewLayoutDestroyed");
        this.viewLayoutCreated = false;
    }

    /**
     * Called when the view is being destroyed.
     */
    public void onViewDestroyed() {
        logger.info("onViewDestroyed");
        if (this.call != null) {
            this.call.cancel();
        }
    }
}
