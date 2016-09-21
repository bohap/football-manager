package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.RegisterViewComponent;
import com.android.finki.mpip.footballdreamteam.ui.component.RegisterView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.RegisterViewPresenter;

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
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDrawable;
import org.robolectric.shadows.ShadowTextView;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 20.09.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class RegisterActivityTest {

    @Mock
    private RegisterViewComponent component;

    @Mock
    private RegisterViewPresenter presenter;

    private ActivityController<RegisterActivity> controller;
    private RegisterActivity activity;
    private EditText txtName;
    private TextView txtNameRequiredError;
    private EditText txtEmail;
    private TextView txtEmailRequiredError;
    private TextView txtEmailInvalidError;
    private EditText txtPassword;
    private TextView txtPasswordRequiredError;
    private EditText txtRepeatPassword;
    private TextView txtRepeatPasswordRequiredError;
    private TextView txtRepeatPasswordNotMatchError;
    private ProgressBar spinner;
    private LinearLayout errorsContainer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setRegisterViewComponent(component);
        this.mockDependencies();
        controller = Robolectric.buildActivity(RegisterActivity.class);
        activity = controller.create().start().resume().visible().get();
        this.getViews();
    }

    /**
     * Mock the dependencies for the activity.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                RegisterActivity activity = (RegisterActivity) invocation.getArguments()[0];
                activity.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(RegisterActivity.class));
    }

    /**
     * Get the main children of the activity.
     */
    private void getViews() {
        txtName = (EditText) activity.findViewById(R.id.registerLayout_txtName);
        assertNotNull(txtName);
        txtNameRequiredError = (TextView)
                activity.findViewById(R.id.registerLayout_name_requiredError);
        assertNotNull(txtNameRequiredError);
        txtEmail = (EditText) activity.findViewById(R.id.registerLayout_txtEmail);
        assertNotNull(txtEmail);
        txtEmailRequiredError = (TextView)
                activity.findViewById(R.id.registerLayout_email_requiredError);
        assertNotNull(txtEmailRequiredError);
        txtEmailInvalidError = (TextView)
                activity.findViewById(R.id.registerLayout_email_invalidError);
        assertNotNull(txtEmailInvalidError);
        txtPassword = (EditText) activity.findViewById(R.id.registerLayout_txtPassword);
        assertNotNull(txtPassword);
        txtPasswordRequiredError = (TextView)
                activity.findViewById(R.id.registerLayout_password_requiredError);
        assertNotNull(txtPasswordRequiredError);
        txtRepeatPassword = (EditText)
                activity.findViewById(R.id.registerLayout_txtRepeatPassword);
        assertNotNull(txtRepeatPassword);
        txtRepeatPasswordRequiredError = (TextView)
                activity.findViewById(R.id.registerLayout_repeatPassword_requiredError);
        assertNotNull(txtRepeatPasswordRequiredError);
        txtRepeatPasswordNotMatchError = (TextView)
                activity.findViewById(R.id.registerLayout_repeatPassword_notMatchError);
        assertNotNull(txtRepeatPasswordNotMatchError);
        spinner = (ProgressBar) activity.findViewById(R.id.registerLayout_spinner);
        assertNotNull(spinner);
        errorsContainer = (LinearLayout) activity.findViewById(R.id.registerLayout_errorsContainer);
        assertNotNull(errorsContainer);
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Test that the activity is successfully created.
     */
    @Test
    public void testActivityIsCreated() {
        String title = activity.getString(R.string.registerLayout_title);
        assertNotNull(activity.getSupportActionBar());
        assertEquals(title, activity.getSupportActionBar().getTitle());
        verify(presenter).onViewLayoutCreated();
    }

    /**
     * Test the behavior when the Home options menu is clicked.
     */
    @Test
    public void testHomeOptionsMenuClick() {
        MenuItem item = new RoboMenuItem(android.R.id.home);
        assertTrue(activity.onOptionsItemSelected(item));
        assertTrue(activity.isFinishing());
        ShadowActivity shadow = shadowOf(activity);
        assertEquals(R.anim.hold, shadow.getPendingTransitionEnterAnimationResourceId());
        assertEquals(R.anim.exit_from_right,
                shadow.getPendingTransitionExitAnimationResourceId());
    }

    /**
     * Test the behavior when showNameError is called.
     */
    @Test
    public void testShowNameError() {
        activity.showNameError();
        ShadowDrawable sDrawable = shadowOf(txtName.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtNameRequiredError.getVisibility());
    }

    /**
     * Test the behavior when showNameOk is called.
     */
    @Test
    public void testShowNameOk() {
        txtName.setBackgroundResource(R.drawable.error_edit_text);
        txtNameRequiredError.setVisibility(View.VISIBLE);
        activity.showNameOk();
        ShadowDrawable sDrawable = shadowOf(txtName.getBackground());
        assertEquals(R.drawable.edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.GONE, txtNameRequiredError.getVisibility());
    }

    /**
     * Test the behavior when showEmailError is called with required error.
     */
    @Test
    public void testShowEmailErrorWithRequiredEmail() {
        activity.showEmailError(RegisterView.EMAIL_ERROR.REQUIRED);
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtEmailRequiredError.getVisibility());
    }

    /**
     * Test the behavior when showEmailError is called with required error and the email
     * before was invalid.
     */
    @Test
    public void testShowEmailErrorWithRequiredErrorAndThePreviousErrorWasInvalid() {
        txtEmailInvalidError.setVisibility(View.VISIBLE);
        activity.showEmailError(RegisterView.EMAIL_ERROR.REQUIRED);
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtEmailRequiredError.getVisibility());
        assertEquals(View.GONE, txtEmailInvalidError.getVisibility());
    }

    /**
     * Test the behavior when showEmailError is called with invalid error.
     */
    @Test
    public void testShowEmailErrorWithInvalidError() {
        activity.showEmailError(RegisterView.EMAIL_ERROR.INVALID);
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtEmailInvalidError.getVisibility());
    }

    /**
     * Test the behavior when showEmailError is called with invalid error and the email was
     * before required.
     */
    @Test
    public void testShowEmailErrorWithInvalidErrorAndThePreviousErrorWasRequired() {
        txtEmailRequiredError.setVisibility(View.VISIBLE);
        activity.showEmailError(RegisterView.EMAIL_ERROR.INVALID);
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtEmailInvalidError.getVisibility());
        assertEquals(View.GONE, txtEmailRequiredError.getVisibility());
    }

    /**
     * Test the behavior when emailOk is called.
     */
    @Test
    public void testEmilOk() {
        txtEmail.setBackgroundResource(R.drawable.error_edit_text);
        txtEmailRequiredError.setVisibility(View.VISIBLE);
        txtEmailInvalidError.setVisibility(View.VISIBLE);
        activity.showEmailOk();
        ShadowDrawable sDrawable = shadowOf(txtEmail.getBackground());
        assertEquals(R.drawable.edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.GONE, txtEmailRequiredError.getVisibility());
        assertEquals(View.GONE, txtEmailInvalidError.getVisibility());
    }

    /**
     * Test the behavior when showPasswordError is called.
     */
    @Test
    public void testShowPasswordError() {
        activity.showPasswordError();
        ShadowDrawable sDrawable = shadowOf(txtPassword.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtPasswordRequiredError.getVisibility());
    }

    /**
     * Test the behavior when passwordOk is called.
     */
    @Test
    public void testPasswordOk() {
        txtPassword.setBackgroundResource(R.drawable.error_edit_text);
        txtPasswordRequiredError.setVisibility(View.VISIBLE);
        activity.showPasswordOk();
        ShadowDrawable sDrawable = shadowOf(txtPassword.getBackground());
        assertEquals(R.drawable.edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.GONE, txtPasswordRequiredError.getVisibility());
    }

    /**
     * Test the behavior when showPasswordError is called with required error.
     */
    @Test
    public void testShowRepeatPasswordErrorWithRequiredError() {
        activity.showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.REQUIRED);
        ShadowDrawable sDrawable = shadowOf(txtRepeatPassword.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtRepeatPasswordRequiredError.getVisibility());
    }

    /**
     * Test the behavior when showRepeatPassword error is called with required error and the
     * previous error was not match.
     */
    @Test
    public void testShowRepeatPasswordErrorWithRequiredErrorAndThePreviousErrorWasNotMatch() {
        txtRepeatPasswordNotMatchError.setVisibility(View.VISIBLE);
        activity.showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.REQUIRED);
        ShadowDrawable sDrawable = shadowOf(txtRepeatPassword.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtRepeatPasswordRequiredError.getVisibility());
        assertEquals(View.GONE, txtRepeatPasswordNotMatchError.getVisibility());
    }

    /**
     * Test the behavior when showRepeatPasswordError called with not match error.
     */
    @Test
    public void testShowRepeatPasswordErrorWithNotMatchError() {
        activity.showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.NOT_MATCH);
        ShadowDrawable sDrawable = shadowOf(txtRepeatPassword.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtRepeatPasswordNotMatchError.getVisibility());
    }

    /**
     * Test the behavior when showRepeatPasswordError is called with not match error and the
     * previous error was required.
     */
    @Test
    public void testShowRepeatPasswordErrorWithNotMatchErrorAndThePreviousErrorWasRequired() {
        txtRepeatPasswordRequiredError.setVisibility(View.VISIBLE);
        activity.showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.NOT_MATCH);
        ShadowDrawable sDrawable = shadowOf(txtRepeatPassword.getBackground());
        assertEquals(R.drawable.error_edit_text, sDrawable.getCreatedFromResId());
        assertEquals(View.VISIBLE, txtRepeatPasswordNotMatchError.getVisibility());
        assertEquals(View.GONE, txtRepeatPasswordRequiredError.getVisibility());
    }

    /**
     * Test the behavior when button 'Register' is clicked.
     */
    @Test
    public void testBtnRegisterClick() {
        String name = "Test Name";
        String email = "Test Email";
        String password = "Test Password";
        String repeatPassword = "Test Repeat Password";
        txtName.setText(name);
        txtEmail.setText(email);
        txtPassword.setText(password);
        txtRepeatPassword.setText(repeatPassword);
        LinearLayout btnRegister = (LinearLayout)
                activity.findViewById(R.id.registerLayout_btnRegister);
        assertNotNull(btnRegister);
        assertTrue(btnRegister.performClick());
        verify(presenter).register(name, email, password, repeatPassword);
    }

    /**
     * Test the behavior when showRegistering is called.
     */
    @Test
    public void testShowRegistering() {
        activity.showRegistering();
        assertEquals(View.VISIBLE, spinner.getVisibility());
    }

    /**
     * Test the behavior when shoeRegistering is called and the previous register has errors.
     */
    @Test
    public void testShowRegisteringWhenThePreviousRegisterHasErrors() {
        errorsContainer.setVisibility(View.VISIBLE);
        activity.showRegistering();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(View.GONE, errorsContainer.getVisibility());
    }

    /**
     * Test the behavior when showRegisteringSuccess is called.
     */
    @Test
    public void testShowRegisteringSuccess() {
        activity.showRegisteringSuccess();
        Intent expectedIntent = new Intent(activity, HomeActivity.class);
        expectedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertEquals(expectedIntent.getFlags(), actualIntent.getFlags());
        assertNotNull(((MockApplication) RuntimeEnvironment.application).getAuthComponent());
    }

    /**
     * Test the behavior when showRegisteringFailed is called with errors.
     */
    @Test
    public void testRegisteringFailedWithErrors() {
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3");
        spinner.setVisibility(View.VISIBLE);
        activity.showRegisteringFailed(errors);
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.VISIBLE, errorsContainer.getVisibility());
        assertEquals(errors.size(), errorsContainer.getChildCount());
        for (int i = 0; i < errors.size(); i++) {
            View view = errorsContainer.getChildAt(i);
            assertNotNull(view);
            assertTrue(view instanceof TextView);
            TextView txt = (TextView) view;
            assertEquals(errors.get(i), ((TextView) view).getText());
            ShadowTextView shadowTxt = shadowOf(txt);
            assertEquals(R.style.TextView_Block_Error, shadowTxt.getTextAppearanceId());
        }
    }

    /**
     * Test that when showRegisteringFailed is called all previous errors in the container
     * will be removed.
     */
    @Test
    public void testShowRegisteringFailedWillRemoveThePreviousErrors() {
        activity.showRegisteringFailed(Arrays.asList("Previous Error 1", "Previous Error 2"));
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3");
        spinner.setVisibility(View.VISIBLE);
        activity.showRegisteringFailed(errors);
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.VISIBLE, errorsContainer.getVisibility());
        assertEquals(errors.size(), errorsContainer.getChildCount());
        for (int i = 0; i < errors.size(); i++) {
            View view = errorsContainer.getChildAt(i);
            assertNotNull(view);
            assertTrue(view instanceof TextView);
            TextView txt = (TextView) view;
            assertEquals(errors.get(i), ((TextView) view).getText());
            ShadowTextView shadowTxt = shadowOf(txt);
            assertEquals(R.style.TextView_Block_Error, shadowTxt.getTextAppearanceId());
        }
    }

    /**
     * Test the behavior when showRegisteringFailed is called with no errors.
     */
    @Test
    public void testShowRegisteringFailedWithNoErrors() {
        spinner.setVisibility(View.VISIBLE);
        errorsContainer.setVisibility(View.VISIBLE);
        activity.showRegisteringFailed();
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, errorsContainer.getVisibility());
    }
}
