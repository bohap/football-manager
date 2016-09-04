package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LoginActivityModule;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LoginActivityPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 26.07.2016.
 */
public class LoginActivity extends BaseActivity {

    public enum EMAIL_ERROR {
        REQUIRED,
        INVALID
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loginLayout_txtEmail)
    EditText txtEmail;

    @BindView((R.id.loginLayout_email_requiredError))
    TextView emailErrorRequired;

    @BindView(R.id.loginLayout_email_invalidError)
    TextView emailErrorInvalid;

    @BindView(R.id.loginLayout_txtPassword)
    EditText txtPassword;

    @BindView(R.id.loginLayout_password_requiredError)
    TextView passwordErrorRequired;

    @BindView(R.id.login_spinner)
    ProgressBar spinner;

    @BindView(R.id.login_errors)
    LinearLayout errorsContainer;

    @BindString(R.string.loginActivity_title)
    String title;

    @BindString(R.string.server_connectionTimeoutMessage)
    String connectionTimeoutMessage;

    @BindString(R.string.server_errorMessage)
    String serverErrorMessage;

    @Inject
    LoginActivityPresenter presenter;

    private Logger logger = LoggerFactory.getLogger(LoginActivity.class);

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved state from when the activity is recreated
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getAppComponent()
                .plus(new LoginActivityModule(this)).inject(this);
        /* Set the toolbar */
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Call the login method on the presenter when the login btn is clicked.
     */
    @OnClick(R.id.login_btnLogin)
    void login() {
        String username = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        presenter.login(username, password);
    }

    /**
     * Show the register activity when the register button is clicked.
     */
    @OnClick(R.id.loginLayout_btnRegister)
    void register() {
        this.startActivity(new Intent(this, RegisterActivity.class));
        super.finish();
    }

    /**
     * Change the edit text background depending on whatever he has a error.
     *
     * @param txtEdit EditText
     * @param error   whatever the background should be error or not
     */
    private void toggleEditTextBackground(EditText txtEdit, boolean error) {
        if (error) {
            txtEdit.setBackgroundResource(R.drawable.error_edit_text);
        } else {
            txtEdit.setBackgroundResource(R.drawable.edit_text);
        }
    }

    /**
     * Add a error for the email edit text field.
     */
    public void errorEmail(EMAIL_ERROR error) {
        this.toggleEditTextBackground(txtEmail, true);
        if (error == EMAIL_ERROR.REQUIRED) {
            this.toggleVisibility(emailErrorRequired, true);
            this.toggleVisibility(emailErrorInvalid, false);
        } else if(error == EMAIL_ERROR.INVALID) {
            this.toggleVisibility(emailErrorInvalid, true);
            this.toggleVisibility(emailErrorRequired, false);
        }
    }

    /**
     * Remove the errors (if he has) from the email edit text field.
     */
    public void okEmail() {
        this.toggleEditTextBackground(txtEmail, false);
        this.toggleVisibility(emailErrorRequired, false);
        this.toggleVisibility(emailErrorInvalid, false);
    }

    /**
     * Add a error for the password edit text field.
     */
    public void errorPassword() {
        this.toggleEditTextBackground(txtPassword, true);
        this.toggleVisibility(passwordErrorRequired, true);
    }

    /**
     * Remove the error (if he has) from the password text field.
     */
    public void okPassword() {
        this.toggleEditTextBackground(txtPassword, false);
        this.toggleVisibility(passwordErrorRequired, false);
    }

    /**
     * Show a spinner indicating that the app is waiting for a response from the server.
     */
    public void showLoading() {
        this.toggleVisibility(spinner, true);
    }

    /**
     * Called when the login is successful.
     */
    public void successfulLogin() {
        this.startActivity(new Intent(this, HomeActivity.class));
        super.finish();
    }

    /**
     * Called when the login failed.
     *
     * @param errors login errors
     */
    public void failedLogin(List<String> errors) {
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorsContainer, true);
        //Remove prevous chidler
        errorsContainer.removeAllViews();
        for (String error : errors) {
            TextView txtError = new TextView(this);
            if (Build.VERSION.SDK_INT < 23) {
                txtError.setTextAppearance(this, R.style.TextView_Block_Error);
            } else {
                txtError.setTextAppearance(R.style.TextView_Block_Error);
            }
            txtError.setText(error);
            errorsContainer.addView(txtError);
        }
    }

    /**
     * Called when there is some problem with the login request.
     */
    public void loginError() {
        this.toggleVisibility(spinner, false);
    }
}
