package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.exception.UserException;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.AuthenticateUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.AuthenticateUserResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 06.08.2016.
 */
public class LoginActivityPresenterTest {

    @Mock
    private LoginActivity activity;

    @Mock
    private SharedPreferences preferences;

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private UserDBService userDBService;

    @Mock
    private AuthApi authApi;

    @Mock
    private Call<AuthenticateUserResponse> call;

    @Captor
    private ArgumentCaptor<Callback<AuthenticateUserResponse>> callbackCaptor;

    @Captor
    private ArgumentCaptor<List<String>> listCaptor;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private LoginViewPresenter presenter;

    private String AUTH_USER_ID_KEY = "auth_user_id_key";
    private User user = new User(1, "User", "user@user.com", null, null, "token");
    private String email = user.getEmail();
    private String password = "password";
    private AuthenticateUserResponse response = new AuthenticateUserResponse(user);

    private final int ERRORS_COUNT = 2;
    private String errorMessage1 = "Error 1";
    private String errorMessage2 = "Error 2";
    private String errorBody = "{\"errors\": [\"" + errorMessage1 + "\", \""
            + errorMessage2 + "\"]}";

    private String emptyEmail = null;
    private String invalidEmail = "email";
    private String emptyPassword = null;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        this.initMocks();
        presenter = new LoginViewPresenter(activity, preferences, userDBService, authApi);
    }

    @SuppressLint("CommitPrefEdits")
    private void initMocks() {
        when(activity.getString(R.string.preference_auth_user_id_key)).thenReturn(AUTH_USER_ID_KEY);
        when(activity.getApplication()).thenReturn(mock(MainApplication.class));
        /* Mock the SharedPreferences */
        when(preferences.edit()).thenReturn(editor);
        when(editor.putInt(AUTH_USER_ID_KEY, user.getId())).thenReturn(editor);
        when(authApi.login(any(AuthenticateUserRequest.class))).thenReturn(call);
    }

    /**
     * Test the behavior on the login method called with empty email.
     */
    @Test
    public void testLoginOnEmptyEmail() {
        presenter.login(emptyEmail, password);
        verify(activity).errorEmail(LoginActivity.EMAIL_ERROR.REQUIRED);
        verify(activity, never()).errorEmail(LoginActivity.EMAIL_ERROR.INVALID);
        verify(activity, never()).okEmail();
        verify(activity).okPassword();
        verify(call, never()).enqueue(presenter);
    }

    /**
     * Test the behavior on the login method called with invalid email.
     */
    @Test
    public void testLoginOnInvalidEmail() {
        presenter.login(invalidEmail, password);
        verify(activity).errorEmail(LoginActivity.EMAIL_ERROR.INVALID);
        verify(activity, never()).errorEmail(LoginActivity.EMAIL_ERROR.REQUIRED);
        verify(activity, never()).okEmail();
        verify(activity).okPassword();
        verify(call, never()).enqueue(presenter);
    }

    /**
     * Test the behavior on login method called with empty password.
     */
    @Test
    public void testLoginOnEmptyPassword() {
        presenter.login(email, emptyPassword);
        verify(activity).errorPassword();
        verify(activity, never()).errorEmail(LoginActivity.EMAIL_ERROR.REQUIRED);
        verify(activity, never()).errorEmail(LoginActivity.EMAIL_ERROR.INVALID);
        verify(activity).okEmail();
        verify(activity, never()).okPassword();
        verify(call, never()).enqueue(presenter);
    }

    /**
     * Test the behavior on the login method called with invalid email and password.
     */
    @Test
    public void testLoginOnErrorEmailAndPassword() {
        presenter.login(invalidEmail, emptyPassword);
        verify(activity).errorEmail(LoginActivity.EMAIL_ERROR.INVALID);
        verify(activity, never()).errorEmail(LoginActivity.EMAIL_ERROR.REQUIRED);
        verify(activity, never()).okEmail();
        verify(activity, never()).okPassword();
        verify(call, never()).enqueue(presenter);
    }

    /**
     * Test the behavior on the login method called with valid email and password.
     */
    @Test
    public void testLoginOnValidEmailAndPassword() {
        presenter.login(email, password);
        verify(activity).okEmail();
        verify(activity, never()).errorEmail(LoginActivity.EMAIL_ERROR.REQUIRED);
        verify(activity, never()).errorEmail(LoginActivity.EMAIL_ERROR.INVALID);
        verify(activity).okPassword();
        verify(activity, never()).errorPassword();
        verify(call).enqueue(presenter);
    }

    /**
     * Test the behavior in the presenter when the login request is successful and the user
     * don't already exists in the database..
     */
    @Test
    public void testSuccessLoginOnUnExistingUserInTheDatabase() {
        when(userDBService.exists(user.getId())).thenReturn(false);

        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));

        verify(userDBService).open();
        verify(userDBService).exists(user.getId());
        verify(userDBService).store(userCaptor.capture());
        assertEquals(user.getId(), userCaptor.getValue().getId());
        assertEquals(user.getEmail(), userCaptor.getValue().getEmail());
        verify(userDBService).close();

        verify(editor).putInt(AUTH_USER_ID_KEY, user.getId());
        verify(activity).getApplication();

        verify(activity).successfulLogin();
        verify(activity, never()).failedLogin(anyListOf(String.class));
        verify(activity, never()).showServerErrorMessage();
    }

    /**
     * Test the behavior on presenter when the login is successful and user id already
     * exists in the database.
     */
    @Test
    public void testSuccessLoginOnExistingUserIdInTheDatabase() {
        when(userDBService.exists(user.getId())).thenReturn(true);

        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));

        verify(userDBService).open();
        verify(userDBService).exists(user.getId());
        verify(userDBService, never()).store(any(User.class));
        verify(userDBService).close();

        verify(editor).putInt(AUTH_USER_ID_KEY, user.getId());
        verify(activity).getApplication();

        verify(activity).successfulLogin();
        verify(activity, never()).failedLogin(anyListOf(String.class));
        verify(activity, never()).showServerErrorMessage();
    }

    /**
     * Test the behavior on the application when the login is successful but storing the
     * user in the database failed.
     */
    @Test
    public void testSuccessLoginOnFailedUserSaving() {
        when(userDBService.exists(user.getId())).thenReturn(false);
        doThrow(UserException.class).when(userDBService).store(any(User.class));

        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));

        verify(userDBService).open();
        verify(userDBService).exists(user.getId());
        verify(userDBService).store(any(User.class));
        verify(userDBService).close();

        verify(editor, never()).putInt(anyString(), anyInt());
        verify(activity, never()).successfulLogin();
        verify(activity, never()).failedLogin(anyListOf(String.class));
        verify(activity).showAppErrorMessage();
    }

    /**
     * Test the behavior on the presenter when the user authentication failed.
     */
    @Test
    public void testAuthenticationFailed() {
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), errorBody);
        callbackCaptor.getValue().onResponse(call,
                Response.<AuthenticateUserResponse>error(401, responseBody));

        verify(activity).failedLogin(listCaptor.capture());
        verify(activity, never()).successfulLogin();
        verify(activity, never()).showServerErrorMessage();
        verify(activity, never()).showServerErrorMessage();
        assertEquals(ERRORS_COUNT, listCaptor.getValue().size());
        assertEquals(errorMessage1, listCaptor.getValue().get(0));
        assertEquals(errorMessage2, listCaptor.getValue().get(1));
    }

    /**
     * Test the behavior on the presenter when the user authentication failed and
     * server body can't be processed.
     */
    @Test
    public void testAuthenticationFailedOnUnExpectedJsonBody() {
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.<AuthenticateUserResponse>error(401,
                ResponseBody.create(MediaType.parse("application/json"),
                        "{\"errors\": \" Ivalid body \"}")));
        verify(activity, never()).successfulLogin();
        verify(activity, never()).failedLogin(anyListOf(String.class));
        verify(activity).showServerErrorMessage();
    }

    /**
     * Test the behavior on the presenter when the server responded with a error.
     */
    @Test
    public void testServerError() {
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.<AuthenticateUserResponse>error(500,
                ResponseBody.create(MediaType.parse("application/json"), "{}")));
        verify(activity).showServerErrorMessage();
        verify(activity, never()).successfulLogin();
        verify(activity, never()).failedLogin(anyListOf(String.class));
    }

    /**
     * Test the behavior on the presenter when connection timeout happened.
     */
    @Test
    public void testLoginSocketTimeout() {
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new SocketTimeoutException());
        verify(activity).showConnectionTimeoutMessage();
        verify(activity, never()).showServerErrorMessage();
        verify(activity, never()).successfulLogin();
    }

    /**
     * Test the behavior on the presenter when a unknown connection happened.
     */
    @Test
    public void testLoginUnknownException() {
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        verify(activity).showServerErrorMessage();
        verify(activity, never()).showConnectionTimeoutMessage();
        verify(activity, never()).successfulLogin();
    }

    /**
     * Test the behavior on the presenter when the login method was called before the previous
     * request ended.
     */
    @Test
    public void testLoginCalledBeforeThePreviousRequestEnded() {
        presenter.login(email, password);
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        verify(activity).showLoading();
    }
}
