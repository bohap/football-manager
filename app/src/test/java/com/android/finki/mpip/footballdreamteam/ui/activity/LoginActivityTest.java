package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LoginViewComponent;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LoginViewPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowTextView;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.robolectric.Shadows.*;

/**
 * Created by Borce on 26.07.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class LoginActivityTest {

    @Mock
    private LoginViewComponent component;

    @Mock
    private LoginViewPresenter presenter;

    private ActivityController<LoginActivity> controller;
    private LoginActivity activity;
    private MockApplication application;

    private EditText txtEmail;
    private TextView emailErrorRequired;
    private TextView emailErrorInvalid;
    private EditText txtPassword;
    private TextView passwordErrorRequired;
    private ProgressBar spinner;
    private RelativeLayout button;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        application = (MockApplication) RuntimeEnvironment.application;
        application.setLoginActivityComponent(component);
        this.mockDependencies();
        controller = Robolectric.buildActivity(LoginActivity.class);
        activity = controller.create().start().resume().visible().get();

        txtEmail = (EditText) activity.findViewById(R.id.loginLayout_txtEmail);
        assertNotNull(txtEmail);
        emailErrorRequired = (TextView) activity.findViewById(R.id.loginLayout_email_requiredError);
        assertNotNull(emailErrorRequired);
        emailErrorInvalid = (TextView) activity.findViewById(R.id.loginLayout_email_invalidError);
        txtPassword = (EditText) activity.findViewById(R.id.loginLayout_txtPassword);
        assertNotNull(txtPassword);
        passwordErrorRequired = (TextView) activity
                .findViewById(R.id.loginLayout_password_requiredError);
        assertNotNull(passwordErrorRequired);
        spinner = (ProgressBar) activity.findViewById(R.id.login_spinner);
        assertNotNull(spinner);
        button = (RelativeLayout) activity.findViewById(R.id.login_btnLogin);
        assertNotNull(button);
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Mock the dependencies for the activity.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LoginActivity activity = (LoginActivity) invocation.getArguments()[0];
                activity.presenter = presenter;
                return null;
            }
        }).when(component).inject(any(LoginActivity.class));
    }

    /**
     * Test that the activity title is correctly set.
     */
    @Test
    public void testActivityTitleIsSet() {
        String title = application.getString(R.string.loginActivity_title);
        assertNotNull(activity.getSupportActionBar());
        assertEquals(title, activity.getSupportActionBar().getTitle());
    }

    /**
     * Test that when the login button is clicked, presenter login method will be called.
     */
    @Test
    public void testBtnLoginClick() {
        String email = "email";
        String password = "password";
        txtEmail.setText(email);
        txtPassword.setText(password);
        button.performClick();
        verify(presenter).login(email, password);
    }

    /**
     * Test that when register btn is clicked a new activity will be started
     * and the current will be finished
     */
    @Test
    public void testBtnRegisterClick() {
        Button button = (Button) activity.findViewById(R.id.loginLayout_btnRegister);
        assertNotNull(button);
        button.performClick();
        ShadowActivity shadow = shadowOf(activity);
        Intent expectedIntent = new Intent(activity, RegisterActivity.class);
        Intent actualIntent = shadow.getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertTrue(shadow.isFinishing());
    }

    /**
     * Test that errorEmail called with EMAIL_ERROR.REQUIRED param
     * will show the required error for the email.
     */
    @Test
    public void testEmailRequiredError() {
        activity.errorEmail(LoginActivity.EMAIL_ERROR.REQUIRED);
        ShadowDrawable shadow = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, shadow.getCreatedFromResId());
        assertEquals(View.VISIBLE, emailErrorRequired.getVisibility());
    }

    /**
     * Test the errorEmail called with EMAIL.REQUIRED error will hide the email invalid message,
     * and show the email required message.
     */
    @Test
    public void testEmailRequiredErrorWhenEmailWasInvalid() {
        emailErrorInvalid.setVisibility(View.VISIBLE);
        activity.errorEmail(LoginActivity.EMAIL_ERROR.REQUIRED);
        ShadowDrawable shadow = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, shadow.getCreatedFromResId());
        assertEquals(View.VISIBLE, emailErrorRequired.getVisibility());
        assertEquals(View.GONE, emailErrorInvalid.getVisibility());
    }

    /**
     * Test that errorEmail called with EMAIL_ERROR.INVALID param
     * will show the invalid error for the email.
     */
    @Test
    public void testEmailInvalidError() {
        activity.errorEmail(LoginActivity.EMAIL_ERROR.INVALID);
        ShadowDrawable shadow = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, shadow.getCreatedFromResId());
        assertEquals(View.VISIBLE, emailErrorInvalid.getVisibility());
    }

    /**
     * Test the errorEmail called with EMAIL_ERROR.INVALID will hide the email required message
     * and show the email invalid message.
     */
    @Test
    public void testEmailInvalidErrorWhenEmailWasRequired() {
        emailErrorRequired.setVisibility(View.VISIBLE);
        activity.errorEmail(LoginActivity.EMAIL_ERROR.INVALID);
        ShadowDrawable shadow = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, shadow.getCreatedFromResId());
        assertEquals(View.VISIBLE, emailErrorInvalid.getVisibility());
        assertEquals(View.GONE, emailErrorRequired.getVisibility());
    }

    /**
     * Test that errorPassword will show the required error for the password.
     */
    @Test
    public void testPasswordRequiredError() {
        activity.errorPassword();
        ShadowDrawable shadow = shadowOf(txtPassword.getBackground());
        assertEquals(R.drawable.error_edit_text, shadow.getCreatedFromResId());
        assertEquals(View.VISIBLE, passwordErrorRequired.getVisibility());
    }

    /**
     * Test that okEmail method will hide the errors for the email.
     */
    @Test
    public void testOkEmailWhenEmailWasRequired() {
        emailErrorRequired.setVisibility(View.VISIBLE);
        activity.okEmail();
        ShadowDrawable shadow = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.edit_text, shadow.getCreatedFromResId());
        assertEquals(View.GONE, emailErrorRequired.getVisibility());
        assertEquals(View.GONE, emailErrorInvalid.getVisibility());
    }

    /**
     * Test that okEmail method will hide the errors for the email.
     */
    @Test
    public void testOkEmailWhenEmailWasInvalid() {
        emailErrorInvalid.setVisibility(View.VISIBLE);
        activity.okEmail();
        ShadowDrawable shadow = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.edit_text, shadow.getCreatedFromResId());
        assertEquals(View.GONE, emailErrorRequired.getVisibility());
        assertEquals(View.GONE, emailErrorInvalid.getVisibility());
    }

    /**
     * Test that okPassword method will hide the errors for the password.
     */
    @Test
    public void testOkPassword() {
        passwordErrorRequired.setVisibility(View.VISIBLE);
        activity.okPassword();
        ShadowDrawable shadow = shadowOf(txtPassword.getBackground());
        assertEquals(R.drawable.edit_text, shadow.getCreatedFromResId());
        assertEquals(View.GONE, passwordErrorRequired.getVisibility());
    }

    /**
     * Test that showDialog method will show the spinner.
     */
    @Test
    public void testShowLoading() {
        activity.showLoading();
        assertEquals(View.VISIBLE, spinner.getVisibility());
    }

    /**
     * Test that successLogin method will start a new activity and finish the current.
     */
    @Test
    public void testSuccessLogin() {
        activity.successfulLogin();
        ShadowActivity shadow = shadowOf(activity);
        Intent expectedIntent = new Intent(activity, HomeActivity.class);
        Intent actualIntent = shadow.getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertTrue(shadow.isFinishing());
    }

    /**
     * Test that failedLogin method will addLike all errors inside the view errors container.
     */
    @Test
    public void testFailedLogin() {
        spinner.setVisibility(View.VISIBLE);
        final int number_of_errors = 3;
        List<String> errors = Arrays.asList("error 1", "error 2", "error 3");

        activity.failedLogin(errors);
        assertEquals(View.GONE, spinner.getVisibility());
        LinearLayout errorsContainer = (LinearLayout) activity.findViewById(R.id.login_errors);
        assertNotNull(errorsContainer);
        assertEquals(View.VISIBLE, errorsContainer.getVisibility());
        assertEquals(number_of_errors, errorsContainer.getChildCount());
        for (int i = 0; i < errorsContainer.getChildCount(); i++) {
            View view = errorsContainer.getChildAt(i);
            assertTrue(view instanceof TextView);
            TextView error = (TextView) view;
            assertEquals(errors.get(i), error.getText());
            ShadowTextView shadow = shadowOf(error);
            assertEquals(R.style.TextView_Block_Error, shadow.getTextAppearanceId());
        }
    }

    /**
     * Test that loginError method will hide the loading spinner.
     */
    @Test
    public void testLoginError() {
        spinner.setVisibility(View.VISIBLE);
        activity.loginError();
        assertEquals(View.GONE, spinner.getVisibility());
    }
}
