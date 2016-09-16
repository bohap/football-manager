package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.RegisterViewModule;
import com.android.finki.mpip.footballdreamteam.ui.component.RegisterView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.RegisterViewPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 06.08.2016.
 */
public class RegisterActivity extends BaseActivity implements RegisterView {

    private static final Logger logger = LoggerFactory.getLogger(RegisterActivity.class);
    private RegisterViewPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindString(R.string.registerLayout_title)
    String title;

    @BindView(R.id.registerLayout_txtName)
    EditText txtName;

    @BindView(R.id.registerLayout_name_requiredError)
    TextView txtNameRequiredError;

    @BindView(R.id.registerLayout_txtEmail)
    EditText txtEmail;

    @BindView(R.id.registerLayout_email_requiredError)
    TextView txtEmailRequiredError;

    @BindView(R.id.registerLayout_email_invalidError)
    TextView txtEmailInvalidError;

    @BindView(R.id.registerLayout_txtPassword)
    EditText txtPassword;

    @BindView(R.id.registerLayout_password_requiredError)
    TextView txtPasswordRequiredError;

    @BindView(R.id.registerLayout_txtRepeatPassword)
    EditText txtRepeatPassword;

    @BindView(R.id.registerLayout_repeatPassword_requiredError)
    TextView txtRepeatPasswordRequiredError;

    @BindView(R.id.registerLayout_repeatPassword_notMatchError)
    TextView txtRepeatPasswordNotMatchError;

    @BindView(R.id.registerLayout_spinner)
    ProgressBar spinner;

    @BindView(R.id.registerLayout_errorsContainer)
    LinearLayout errorsContainer;

    /**
     * Set the presenter for the activity.
     *
     * @param presenter activity presenter
     */
    @Inject
    public void setPresenter(RegisterViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the acitivty is ready to be created.
     *
     * @param savedInstanceState saved instance state when the activity is recreated
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.register_layout);
        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getAppComponent()
                .plus(new RegisterViewModule(this)).inject(this);
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        presenter.onViewLayoutCreated();
    }

    /**
     * Called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.hold, R.anim.exit_from_right);
    }

    /**
     * Called when a options item is selected.
     *
     * @param item selected item
     * @return whatever the action should be canceled or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logger.info("onOptionsItemSelected");
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle click on the button 'Register'.
     */
    @OnClick(R.id.registerLayout_btnRegister)
    void onBtnRegisterClick() {
        logger.info("btn 'Register' clicked");
        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String repeatPassword = txtRepeatPassword.getText().toString();
        presenter.register(name, email, password, repeatPassword);
    }

    /**
     * Change the EditText background depending on whatever he has a error.
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
     * Add a error to the name EditText field.
     */
    @Override
    public void showNameError() {
        logger.info("showNameError");
        this.toggleErrorBackground(txtName, true);
        super.toggleVisibility(txtNameRequiredError, true);
    }

    /**
     * Remove the errors from the name EditText field.
     */
    @Override
    public void showNameOk() {
        logger.info("showNameOk");
        this.toggleErrorBackground(txtName, false);
        super.toggleVisibility(txtNameRequiredError, false);
    }

    /**
     * Add a error to the email EditText field.
     *
     * @param error the error type (invalid|required)
     */
    @Override
    public void showEmailError(EMAIL_ERROR error) {
        logger.info(String.format("showEmailError, error %s", error));
        this.toggleErrorBackground(txtEmail, true);
        if (error == EMAIL_ERROR.REQUIRED) {
            super.toggleVisibility(txtEmailRequiredError, true);
            super.toggleVisibility(txtEmailInvalidError, false);
        } else {
            super.toggleVisibility(txtEmailInvalidError, true);
            super.toggleVisibility(txtEmailRequiredError, false);
        }
    }

    /**
     * Remove the error from the email EditText.
     */
    @Override
    public void showEmailOk() {
        logger.info("showEmilOk");
        this.toggleErrorBackground(txtEmail, false);
        super.toggleVisibility(txtEmailRequiredError, false);
        super.toggleVisibility(txtEmailInvalidError, false);
    }

    /**
     * Add a error to the password EditText field.
     */
    @Override
    public void showPasswordError() {
        logger.info("showPasswordError");
        this.toggleErrorBackground(txtPassword, true);
        super.toggleVisibility(txtPasswordRequiredError, true);
    }

    /**
     * Remove the error from the password EditText field.
     */
    @Override
    public void showPasswordOk() {
        logger.info("showPasswordError");
        this.toggleErrorBackground(txtPassword, false);
        super.toggleVisibility(txtPasswordRequiredError, false);
    }

    /**
     * Add error to the repeat password EditText field.
     *
     * @param error the email type (required|not_match)
     */
    @Override
    public void showRepeatPasswordError(REPEAT_PASSWORD_ERROR error) {
        logger.info(String.format("showRepeatPasswordError, error %s", error));
        this.toggleErrorBackground(txtRepeatPassword, true);
        if (error == REPEAT_PASSWORD_ERROR.REQUIRED) {
            super.toggleVisibility(txtRepeatPasswordRequiredError, true);
            super.toggleVisibility(txtRepeatPasswordNotMatchError, false);
        } else {
            super.toggleVisibility(txtRepeatPasswordNotMatchError, true);
            super.toggleVisibility(txtRepeatPasswordRequiredError, false);
        }
    }

    /**
     * Remove the error from the password EditText field.
     */
    @Override
    public void showRepeatPasswordOk() {
        logger.info("showRepeatPasswordOk");
        this.toggleErrorBackground(txtRepeatPassword, false);
        super.toggleVisibility(txtRepeatPasswordRequiredError, false);
        super.toggleVisibility(txtRepeatPasswordNotMatchError, false);
    }

    /**
     * Called when the register request has been send.
     */
    @Override
    public void showRegistering() {
        logger.info("showRegistering");
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(errorsContainer, false);
    }

    /**
     * Called when the registering the user is successful.
     */
    @Override
    public void showRegisteringSuccess() {
        logger.info("showRegisteringSuccess");
        ((MainApplication) this.getApplication()).createAuthComponent();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    /**
     * Called when registering the user failed.
     *
     * @param errors server errors
     */
    @SuppressWarnings("deprecation")
    @Override
    public void showRegisteringFailed(List<String> errors) {
        logger.info("showRegisteringFailed with messages");
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
     * Called when registering the user failed.
     */
    @Override
    public void showRegisteringFailed() {
        logger.info("showRegisteringFailed");
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorsContainer, false);
    }
}
