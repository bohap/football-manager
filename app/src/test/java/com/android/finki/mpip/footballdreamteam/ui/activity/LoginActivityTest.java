package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowTextView;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

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
    private TextView txtEmailRequiredError;
    private TextView txtEmailInvalidError;
    private EditText txtPassword;
    private TextView txtPasswordRequiredError;
    private ProgressBar spinner;
    private LinearLayout btnLogin;
    private LinearLayout errorsContainer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        application = (MockApplication) RuntimeEnvironment.application;
        application.setLoginViewComponent(component);
        this.mockDependencies();
        controller = Robolectric.buildActivity(LoginActivity.class);
        activity = controller.create().start().resume().visible().get();
        this.getViews();
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
                activity.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(LoginActivity.class));
    }

    /**
     * Test that the activity title is correctly set.
     */
    @Test
    public void testActivityTitleIsSet() {
        String title = application.getString(R.string.loginLayout_title);
        assertNotNull(activity.getSupportActionBar());
        assertEquals(title, activity.getSupportActionBar().getTitle());
        verify(presenter).onViewLayoutCreated();
    }

    /**
     * Get the views in the activity layout.
     */
    private void getViews() {
        txtEmail = (EditText) activity.findViewById(R.id.loginLayout_txtEmail);
        assertNotNull(txtEmail);
        txtEmailRequiredError = (TextView)
                activity.findViewById(R.id.loginLayout_email_requiredError);
        assertNotNull(txtEmailRequiredError);
        txtEmailInvalidError = (TextView)
                activity.findViewById(R.id.loginLayout_email_invalidError);
        txtPassword = (EditText) activity.findViewById(R.id.loginLayout_txtPassword);
        assertNotNull(txtPassword);
        txtPasswordRequiredError = (TextView)
                activity.findViewById(R.id.loginLayout_password_requiredError);
        assertNotNull(txtPasswordRequiredError);
        spinner = (ProgressBar) activity.findViewById(R.id.loginLayout_spinner);
        assertNotNull(spinner);
        btnLogin = (LinearLayout) activity.findViewById(R.id.loginLayout_btnLogin);
        assertNotNull(btnLogin);
        errorsContainer = (LinearLayout) activity.findViewById(R.id.loginLayout_errorsContainer);
        assertNotNull(errorsContainer);
    }

    /**
     * Test that errorEmail called with EMAIL_ERROR.REQUIRED paramwill show the required error
     * for the email.
     */
    @Test
    public void testEmailRequiredError() {
        activity.showEmailError(LoginActivity.EMAIL_ERROR.REQUIRED);
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtEmailRequiredError.getVisibility());
    }

    /**
     * Test the errorEmail called with EMAIL.REQUIRED error will hide the email invalid message,
     * and show the email required message.
     */
    @Test
    public void testEmailRequiredErrorWhenEmailWasInvalid() {
        txtEmailInvalidError.setVisibility(View.VISIBLE);
        activity.showEmailError(LoginActivity.EMAIL_ERROR.REQUIRED);
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtEmailRequiredError.getVisibility());
        assertEquals(View.GONE, txtEmailInvalidError.getVisibility());
    }

    /**
     * Test that errorEmail called with EMAIL_ERROR.INVALID param will show the invalid error
     * for the email.
     */
    @Test
    public void testEmailInvalidError() {
        activity.showEmailError(LoginActivity.EMAIL_ERROR.INVALID);
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtEmailInvalidError.getVisibility());
    }

    /**
     * Test the errorEmail called with EMAIL_ERROR.INVALID will hide the email required message
     * and show the email invalid message.
     */
    @Test
    public void testEmailInvalidErrorWhenEmailWasRequired() {
        txtEmailRequiredError.setVisibility(View.VISIBLE);
        activity.showEmailError(LoginActivity.EMAIL_ERROR.INVALID);
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtEmailInvalidError.getVisibility());
        assertEquals(View.GONE, txtEmailRequiredError.getVisibility());
    }

    /**
     * Test that errorPassword will show the required error for the password.
     */
    @Test
    public void testPasswordRequiredError() {
        activity.showPasswordError();
        ShadowDrawable sDrawable = shadowOf(txtPassword.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtPasswordRequiredError.getVisibility());
    }

    /**
     * Test that okEmail method will hide the errors for the email.
     */
    @Test
    public void testOkEmailWhenEmailWasRequired() {
        txtEmailRequiredError.setVisibility(View.VISIBLE);
        txtEmailInvalidError.setVisibility(View.VISIBLE);
        activity.showEmailOk();
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.GONE, txtEmailRequiredError.getVisibility());
        assertEquals(View.GONE, txtEmailInvalidError.getVisibility());
    }

    /**
     * Test that okPassword method will hide the errors for the password.
     */
    @Test
    public void testOkPassword() {
        txtPasswordRequiredError.setVisibility(View.VISIBLE);
        activity.showPasswordOk();
        ShadowDrawable sDrawable = shadowOf(txtPassword.getBackground());
        assertEquals(R.drawable.edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.GONE, txtPasswordRequiredError.getVisibility());
    }

    /**
     * The the behavior when button 'Login' is clicked.
     */
    @Test
    public void testBtnLoginClick() {
        String email = "email";
        String password = "password";
        txtEmail.setText(email);
        txtPassword.setText(password);
        assertTrue(btnLogin.performClick());
        verify(presenter).login(email, password);
    }

    /**
     * Test the behavior when showLogging is called.
     */
    @Test
    public void testShowLogging() {
        activity.showLogging();
        assertEquals(View.VISIBLE, spinner.getVisibility());
    }

    /**
     * Test the behavior when showLogging is called and the previous login had errors.
     */
    @Test
    public void testShowLoggingWhenPreviousLoginHadErrors() {
        errorsContainer.setVisibility(View.VISIBLE);
        activity.showLogging();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(View.GONE, errorsContainer.getVisibility());
    }

    /**
     * Test the behavior when showLoggingSuccess is called.
     */
    @Test
    public void testShowLoggingSuccess() {
        activity.showLoggingSuccess();
        Intent expectedIntent = new Intent(activity, HomeActivity.class);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertTrue(activity.isFinishing());
        assertNotNull(application.getAuthComponent());
    }

    /**
     * Test the behavior when showLoggingFailed is called with errors.
     */
    @Test
    public void testShowLoggingFailedWithError() {
        spinner.setVisibility(View.VISIBLE);
        List<String> errors = Arrays.asList("error 1", "error 2", "error 3");
        activity.showLoggingFailed(errors);
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.VISIBLE, errorsContainer.getVisibility());
        assertEquals(errors.size(), errorsContainer.getChildCount());
        for (int i = 0; i < errors.size(); i++) {
            View view = errorsContainer.getChildAt(i);
            assertTrue(view instanceof TextView);
            TextView error = (TextView) view;
            assertEquals(errors.get(i), error.getText());
            ShadowTextView shadow = shadowOf(error);
            assertEquals(R.style.TextView_Block_Error, shadow.getTextAppearanceId());
        }
    }

    /**
     * Test that showLoggingFailed will remove the previous errors.
     */
    @Test
    public void testShowLoggingFailedWillDeleteThePreviousErrors() {
        activity.showLoggingFailed(Arrays.asList("Previous Error 1", "Previous Error 2"));
        final int number_of_errors = 3;
        List<String> errors = Arrays.asList("error 1", "error 2", "error 3");
        activity.showLoggingFailed(errors);
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
     * Test the behavior when showLoggingFailed is called without errors.
     */
    @Test
    public void testLoginError() {
        spinner.setVisibility(View.VISIBLE);
        activity.showLoggingFailed();
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, errorsContainer.getVisibility());
    }

    /**
     * Test the behavior when button 'Register' is clicked.
     */
    @Test
    public void testBtnRegisterClick() {
        Button button = (Button) activity.findViewById(R.id.loginLayout_btnRegister);
        assertNotNull(button);
        assertTrue(button.performClick());
        Intent expectedIntent = new Intent(activity, RegisterActivity.class);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertFalse(activity.isFinishing());
    }
}
