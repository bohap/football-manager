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
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LoginViewModule;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.component.LoginView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LoginViewPresenter;

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
public class LoginActivity extends BaseActivity implements LoginView {

    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);
    private LoginViewPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindString(R.string.loginLayout_title)
    String title;

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

    @BindView(R.id.loginLayout_spinner)
    ProgressBar spinner;

    @BindView(R.id.loginLayout_errorsContainer)
    LinearLayout errorsContainer;

    /**
     * Set the presenter for the activity.
     *
     * @param presenter activity presenter
     */
    @Inject
    public void setPresenter(LoginViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved state from when the activity is recreated
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getAppComponent()
                .plus(new LoginViewModule(this)).inject(this);
        /* Set the toolbar */
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
        }
        presenter.onViewLayoutCreated();
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        logger.info("onDestroy");
        super.onDestroy();
        presenter.onViewLayoutDestroyed();
        presenter.onViewDestroyed();
    }

    /**
     * Call the login method on the presenter when the login btn is clicked.
     */
    @OnClick(R.id.loginLayout_btnLogin)
    void login() {
        logger.info("btn 'Login' clicked");
        String username = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        presenter.login(username, password);
    }

    /**
     * Show the register activity when the register button is clicked.
     */
    @OnClick(R.id.loginLayout_btnRegister)
    void register() {
        logger.info("btn 'Register' clicked");
        this.startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * Change the edit text background depending on whatever he has a error.
     *
     * @param txtEdit EditText
     * @param error   whatever the background should be error or not
     */
    private void toggleErrorBackground(EditText txtEdit, boolean error) {
        if (error) {
            txtEdit.setBackgroundResource(R.drawable.error_edit_text);
        } else {
            txtEdit.setBackgroundResource(R.drawable.edit_text);
        }
    }

    /**
     * Add a error for the email edit text field.
     */
    @Override
    public void showEmailError(EMAIL_ERROR error) {
        logger.info(String.format("showEmailError, error %s", error));
        this.toggleErrorBackground(txtEmail, true);
        if (error == EMAIL_ERROR.REQUIRED) {
            this.toggleVisibility(emailErrorRequired, true);
            this.toggleVisibility(emailErrorInvalid, false);
        } else if (error == EMAIL_ERROR.INVALID) {
            this.toggleVisibility(emailErrorInvalid, true);
            this.toggleVisibility(emailErrorRequired, false);
        }
    }

    /**
     * Remove the errors (if he has) from the email edit text field.
     */
    @Override
    public void showEmailOk() {
        logger.info("showEmailOk");
        this.toggleErrorBackground(txtEmail, false);
        this.toggleVisibility(emailErrorRequired, false);
        this.toggleVisibility(emailErrorInvalid, false);
    }

    /**
     * Add a error for the password edit text field.
     */
    @Override
    public void showPasswordError() {
        logger.info("showPasswordError");
        this.toggleErrorBackground(txtPassword, true);
        this.toggleVisibility(passwordErrorRequired, true);
    }

    /**
     * Remove the error (if he has) from the password text field.
     */
    @Override
    public void showPasswordOk() {
        logger.info("showPasswordOk");
        this.toggleErrorBackground(txtPassword, false);
        this.toggleVisibility(passwordErrorRequired, false);
    }

    /**
     * Show a spinner indicating that the app is waiting for a response from the server.
     */
    @Override
    public void showLogging() {
        logger.info("showLogging");
        this.toggleVisibility(spinner, true);
    }

    /**
     * Called when the login is successful.
     */
    @Override
    public void showLoginSuccessful() {
        logger.info("showLoginSuccessful");
        this.startActivity(new Intent(this, HomeActivity.class));
        super.finish();
    }

    /**
     * Called when the login failed and the server responded with body containing error messages.
     *
     * @param errors login errors
     */
    @SuppressWarnings("deprecation")
    @Override
    public void showLoginFailed(List<String> errors) {
        logger.info("showLoginFailed with messages");
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorsContainer, true);
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
     * Called when the login failed and the server returned body containing no messages.
     */
    @Override
    public void showLoginFailed() {
        logger.info("showLoginFailed with no messages");
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorsContainer, false);
    }

    /**
     * Create the user component for the application.
     *
     * @param user authenticated user
     */
    @Override
    public void createUserComponent(User user) {
        logger.info("createUserComponent");
        ((MainApplication) this.getApplication()).createUserComponent(user);
    }
}
