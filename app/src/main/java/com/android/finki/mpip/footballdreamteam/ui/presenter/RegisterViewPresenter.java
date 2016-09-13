package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.exception.UnProcessableEntityException;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.RegisterUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.RegisterUserResponse;
import com.android.finki.mpip.footballdreamteam.rest.response.UnProcessableEntityResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.component.RegisterView;
import com.android.finki.mpip.footballdreamteam.utility.StringUtils;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 13.09.2016.
 */
public class RegisterViewPresenter extends BasePresenter implements Callback<RegisterUserResponse> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterViewPresenter.class);
    private RegisterView view;
    private SharedPreferences preferences;
    private String key;
    private UserDBService dbService;
    private AuthApi api;
    private Call<RegisterUserResponse> call;
    private boolean requestSending = false;
    private boolean viewLayoutCreated = false;
    private String name;
    private String email;
    private String password;

    public RegisterViewPresenter(RegisterView view, SharedPreferences preferences, Context context,
                                 UserDBService dbService, AuthApi api) {
        this.view = view;
        this.key = context.getString(R.string.preference_auth_user_id_key);
        this.preferences = preferences;
        this.dbService = dbService;
        this.api = api;
    }

    /**
     * Called when the view layout is created.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        this.viewLayoutCreated = true;
        if (requestSending) {
            view.showRegistering();
        }
    }

    /**
     * Called when the view layout is destroyed.
     */
    public void onViewLayoutDestroyed() {
        logger.info("onViewLayoutDestroyed");
        this.viewLayoutCreated = false;
    }

    /**
     * Called when the view is destroyed.
     */
    public void onViewDestroyed() {
        logger.info("onViewDestroyed");
        if (call != null) {
            call.cancel();
        }
    }

    /**
     * Validate that the given name is valid.
     *
     * @param name string to be validated
     * @return whatever the name is valid or not
     */
    private boolean validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            view.showNameError();
            return false;
        }
        view.showNameOk();
        return true;
    }

    /**
     * Validate that the given email is valid.
     *
     * @param email string to be validated
     * @return whatever the email is valid or not
     */
    private boolean validateEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            view.showEmailError(RegisterView.EMAIL_ERROR.REQUIRED);
            return false;
        }
        if (!StringUtils.isValidEmail(email)) {
            view.showEmailError(RegisterView.EMAIL_ERROR.INVALID);
            return false;
        }
        view.showEmailOk();
        return true;
    }

    /**
     * Validate that the given password is valid.
     *
     * @param password       original password
     * @param repeatPassword repeated password
     * @return whatever the password is valid or not
     */
    private boolean validatePassword(String password, String repeatPassword) {
        boolean passwordError = false;
        if (StringUtils.isEmpty(password)) {
            view.showPasswordError();
            passwordError = true;
        } else {
            view.showPasswordOk();
        }
        if (StringUtils.isEmpty(repeatPassword)) {
            view.showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.REQUIRED);
            return false;
        }
        if (passwordError) {
            return false;
        }
        if (!repeatPassword.equals(password)) {
            view.showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.NOT_MATCH);
            return false;
        }
        view.showRepeatPasswordOk();
        return true;
    }

    /**
     * Register the user with the given credentials.
     *
     * @param name           user name
     * @param email          user email
     * @param password       user password
     * @param repeatPassword repeat password
     */
    public void register(String name, String email, String password, String repeatPassword) {
        if (!requestSending) {
            boolean valid = this.validateName(name);
            valid = validateEmail(email) && valid;
            valid = validatePassword(password, repeatPassword) && valid;
            if (valid) {
                logger.info("sending register request");
                requestSending = true;
                this.name = name;
                this.email = email;
                this.password = password;
                if (viewLayoutCreated) {
                    view.showRegistering();
                }
                call = api.register(new RegisterUserRequest(name, email, password, repeatPassword));
                call.enqueue(this);
            }
        }
    }

    /**
     * Called when registering the user is successful.
     *
     * @param call     retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<RegisterUserResponse> call,
                           Response<RegisterUserResponse> response) {
        logger.info("register user success");
        this.requestSending = false;
        this.call = null;
        this.registrationSuccess(response.body());
    }

    /**
     * Save the registered user in the local database, and the token in the shared preferences.
     *
     * @param response server response
     */
    private void registrationSuccess(RegisterUserResponse response) {
        User user = new User(response.getUser().getId(), name, email,
                password, new Date(), new Date());
        dbService.open();
        try {
            dbService.store(user);
        } catch (RuntimeException exp) {
            logger.info("error occurred while saving the user in the daatabase");
            exp.printStackTrace();
            if (viewLayoutCreated) {
                view.showRegisteringFailed();
                view.showInternalServerError();
            }
            dbService.close();
            return;
        }
        dbService.close();
        preferences.edit().putString(key, response.getToken()).apply();
        view.creteUserComponent(user);
        view.showRegisteringSuccess();
    }

    /**
     * Called when registering the user failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    @Override
    public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
        logger.info("register user failed");
        this.requestSending = false;
        if (call.isCanceled()) {
            logger.info("register user canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                if (t instanceof UnProcessableEntityException) {
                    this.registrationFailed(((UnProcessableEntityException) t).getResponse());
                } else {
                    view.showRegisteringFailed();
                    super.onRequestFailed(view, t);
                }
            }
        }
        this.call = null;
    }

    /**
     * Get the server errors from the throwable.
     *
     * @param response server response
     */
    private void registrationFailed(okhttp3.Response response) {
        Gson gson = new Gson();
        try {
            UnProcessableEntityResponse unProcessableEntityResponse = gson
                    .fromJson(response.body().string(), UnProcessableEntityResponse.class);
            Map<String, List<String>> errors = unProcessableEntityResponse.getErrors();
            List<String> result = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
                result.addAll(entry.getValue());
            }
            view.showRegisteringFailed(result);
        } catch (IOException e) {
            e.printStackTrace();
            view.showRegisteringFailed();
            view.showInternalServerError();
        }
    }
}
