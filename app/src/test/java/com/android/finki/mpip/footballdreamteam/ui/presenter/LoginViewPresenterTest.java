package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.exception.NotAuthenticatedException;
import com.android.finki.mpip.footballdreamteam.exception.UserException;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.AuthenticateUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.AuthenticateUserResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.component.LoginView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 06.08.2016.
 */
@Ignore
public class LoginViewPresenterTest {

    @Mock
    private LoginView view;

    @Mock
    private Context context;

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
    private ArgumentCaptor<AuthenticateUserRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<Callback<AuthenticateUserResponse>> callbackCaptor;

    @Captor
    private ArgumentCaptor<List<String>> listCaptor;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private LoginViewPresenter presenter;

    private String AUTH_USER_ID_KEY = "auth_user_id_key";
    private String JWT_TOKEN_KEY = "jwt_tok";
    private User user = new User(1, "User", "user@user.com", "password", null, null);
    private String email = user.getEmail();
    private String password = user.getPassword();
    private AuthenticateUserResponse response =
            new AuthenticateUserResponse(user.getId(), user.getName(), user.getEmail(), "Token");

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        this.initMocks();
        presenter = new LoginViewPresenter(view, preferences, context, userDBService, authApi);
    }

    @SuppressLint("CommitPrefEdits")
    private void initMocks() {
        when(context.getString(R.string.preference_auth_user_id_key)).thenReturn(AUTH_USER_ID_KEY);
        when(context.getString(R.string.preference_jwt_token_key)).thenReturn(JWT_TOKEN_KEY);
        when(preferences.edit()).thenReturn(editor);
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);
        when(authApi.login(any(AuthenticateUserRequest.class))).thenReturn(call);
    }

    /**
     * Test the behavior when login is called and the view layout is not creted.
     */
    @Test
    public void testLoginWhenViewLayoutIsNotCreated() {
        presenter.login(email, password);
        verify(view, never()).showEmailError(any(LoginView.EMAIL_ERROR.class));
        verify(view, never()).showEmailOk();
        verify(view, never()).showPasswordError();
        verify(view, never()).showPasswordOk();
        verify(view, never()).showLogging();
    }

    /**
     * Test the behavior on the login method called with empty email and password.
     */
    @Test
    public void testLoginWithEmptyEmailAndPassword() {
        presenter.onViewLayoutCreated();
        presenter.login("", "");
        verify(view).showEmailError(LoginView.EMAIL_ERROR.REQUIRED);
        verify(view, never()).showEmailError(LoginView.EMAIL_ERROR.INVALID);
        verify(view, never()).showEmailOk();
        verify(view).showPasswordError();
        verify(authApi, never()).login(any(AuthenticateUserRequest.class));
    }

    /**
     * Test the behavior on the login method called with invalid email.
     */
    @Test
    public void testLoginWithInvalidEmail() {
        presenter.onViewLayoutCreated();
        presenter.login("test", password);
        verify(view).showEmailError(LoginView.EMAIL_ERROR.INVALID);
        verify(view, never()).showEmailError(LoginView.EMAIL_ERROR.REQUIRED);
        verify(view, never()).showEmailOk();
        verify(view).showPasswordOk();
        verify(authApi, never()).login(any(AuthenticateUserRequest.class));
    }

    /**
     * Test the behavior when the the login request is successful and updating the user data
     * in the local database failed.
     */
    @Test
    public void testLoginSuccessAndUserDataUpdatingFailed() {
        when(userDBService.exists(anyInt())).thenReturn(true);
        doThrow(new UserException("")).when(userDBService).update(any(User.class));
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showEmailOk();
        verify(view).showPasswordOk();
        verify(view, never()).showEmailError(any(LoginView.EMAIL_ERROR.class));
        verify(view, never()).showPasswordError();
        verify(authApi).login(requestCaptor.capture());
        verify(view).showLogging();
        assertEquals(email, requestCaptor.getValue().getEmail());
        assertEquals(password, requestCaptor.getValue().getPassword());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(userDBService);
        inOrder.verify(userDBService).open();
        inOrder.verify(userDBService).exists(user.getId());
        inOrder.verify(userDBService).update(userCaptor.capture());
        inOrder.verify(userDBService).close();
        assertEquals(user.getId(), userCaptor.getValue().getId());
        assertEquals(user.getName(), userCaptor.getValue().getName());
        assertEquals(user.getEmail(), userCaptor.getValue().getEmail());
        assertEquals(user.getPassword(), userCaptor.getValue().getPassword());
        verify(userDBService, never()).store(any(User.class));
        verify(view).showLoggingFailed();
        verify(view).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when the login request is successful, updating the user data
     * failed and the layout is destroyed when the request is received.
     */
    @Test
    public void testUserDataUpdatingFailedAndViewLayoutDestroyed() {
        when(userDBService.exists(anyInt())).thenReturn(true);
        doThrow(new UserException("")).when(userDBService).update(any(User.class));
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(userDBService);
        inOrder.verify(userDBService).open();
        inOrder.verify(userDBService).exists(user.getId());
        inOrder.verify(userDBService).update(userCaptor.capture());
        inOrder.verify(userDBService).close();
        verify(view, never()).showLoggingFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when the login request is successful and updating the user data
     * is successful.
     */
    @Test
    public void testUserDataUpdatingSuccess() {
        when(userDBService.exists(anyInt())).thenReturn(true);
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(userDBService);
        inOrder.verify(userDBService).open();
        inOrder.verify(userDBService).exists(user.getId());
        inOrder.verify(userDBService).update(userCaptor.capture());
        inOrder.verify(userDBService).close();
        verify(editor).putInt(AUTH_USER_ID_KEY, response.getId());
        verify(editor).putString(JWT_TOKEN_KEY, response.getJwtToken());
        verify(editor).apply();
        verify(view, never()).showLoggingFailed();
        verify(view, never()).showInternalServerError();
        verify(view).showLoggingSuccess();
    }

    /**
     * Test the behavior when the login request is successful, updating the user data
     * is successful and the view is destroyed when the request is received.
     */
    @Test
    public void testUserDataUpdatingSuccessAndViewLayoutDestroyed() {
        when(userDBService.exists(anyInt())).thenReturn(true);
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(userDBService);
        inOrder.verify(userDBService).open();
        inOrder.verify(userDBService).exists(user.getId());
        inOrder.verify(userDBService).update(userCaptor.capture());
        inOrder.verify(userDBService).close();
        verify(editor).putInt(AUTH_USER_ID_KEY, response.getId());
        verify(editor).putString(JWT_TOKEN_KEY, response.getJwtToken());
        verify(editor).apply();
        verify(view, never()).showLoggingFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when the login request is successful and storing the user data in the
     * local database failed.
     */
    @Test
    public void testLoginSuccessAndUserDataStoringFailed() {
        when(userDBService.exists(anyInt())).thenReturn(false);
        doThrow(new UserException("")).when(userDBService).store(any(User.class));
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(userDBService);
        inOrder.verify(userDBService).open();
        inOrder.verify(userDBService).exists(user.getId());
        inOrder.verify(userDBService).store(userCaptor.capture());
        assertEquals(user.getId(), userCaptor.getValue().getId());
        assertEquals(user.getName(), userCaptor.getValue().getName());
        assertEquals(user.getEmail(), userCaptor.getValue().getEmail());
        assertEquals(user.getPassword(), userCaptor.getValue().getPassword());
        inOrder.verify(userDBService).close();
        verify(userDBService, never()).update(any(User.class));
        verify(view).showLoggingFailed();
        verify(view).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when the login request is successful, storing the user data in the
     * local database failed and the view layout is destroyed.
     */
    @Test
    public void testUserDataStoringFailedAndViewLayoutDestroyed() {
        when(userDBService.exists(anyInt())).thenReturn(false);
        doThrow(new UserException("")).when(userDBService).store(any(User.class));
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        presenter.onViewLayoutDestroyed();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(userDBService);
        inOrder.verify(userDBService).open();
        inOrder.verify(userDBService).exists(user.getId());
        inOrder.verify(userDBService).store(userCaptor.capture());
        inOrder.verify(userDBService).close();
        verify(view, never()).showLoggingFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when login request is successful and saving the user data is successful.
     */
    @Test
    public void testUserDataStoringSuccess() {
        when(userDBService.exists(anyInt())).thenReturn(false);
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(userDBService);
        inOrder.verify(userDBService).open();
        inOrder.verify(userDBService).exists(user.getId());
        inOrder.verify(userDBService).store(userCaptor.capture());
        inOrder.verify(userDBService).close();
        verify(editor).putInt(AUTH_USER_ID_KEY, response.getId());
        verify(editor).putString(JWT_TOKEN_KEY, response.getJwtToken());
        verify(editor).apply();
        verify(view, never()).showLoggingFailed();
        verify(view, never()).showInternalServerError();
        verify(view).showLoggingSuccess();
    }

    /**
     * Test the behavior when login request is successful, saving the user data is successful
     * and view layout is destroyed.
     */
    @Test
    public void testUserDataStoringSuccessAndViewLayoutDestroyed() {
        when(userDBService.exists(anyInt())).thenReturn(false);
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(userDBService);
        inOrder.verify(userDBService).open();
        inOrder.verify(userDBService).exists(user.getId());
        inOrder.verify(userDBService).store(userCaptor.capture());
        inOrder.verify(userDBService).close();
        verify(editor).putInt(AUTH_USER_ID_KEY, response.getId());
        verify(editor).putString(JWT_TOKEN_KEY, response.getJwtToken());
        verify(editor).apply();
        verify(view, never()).showLoggingFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when logging failed and the view layout is destroyed.
     */
    @Test
    public void testLoginFailedAndViewLayoutDestroyed() {
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onFailure(call, new Throwable());
        verify(view, never()).showLoggingFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when login failed and the exceptions that has been thrown is not
     * NotAuthenticatedException.
     */
    @Test
    public void testLoginFailedWithExceptionThatIsNotNotAuthenticated() {
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new SocketTimeoutException());
        verify(view).showLoggingFailed();
        verify(view).showSocketTimeout();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when the login request failed and the server response is null.
     */
    @Test
    public void testLoginFailedWithAuthenticatedExceptionAndNullResponse() {
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new NotAuthenticatedException());
        verify(view).showLoggingFailed();
        verify(view).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Create a test response.
     *
     * @param body response body
     * @return test response
     */
    private okhttp3.Response createResponse(String body) {
        HttpUrl url = new HttpUrl.Builder().scheme("https").host("example.com").build();
        Request request = new Request.Builder().url(url).build();
        return new okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_0)
                .code(500)
                .body(ResponseBody.create(MediaType.parse("application/json"), body))
                .build();
    }

    /**
     * Test the behavior when the login request failed and the server response is not valid.
     */
    @Test
    public void testLoginFailedWithAuthenticatedExceptionAndInvalidServerResponse() {
        String body = "Test";
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call,
                new NotAuthenticatedException(createResponse(body)));
        verify(view).showLoggingFailed();
        verify(view).showInternalServerError();
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when the login request failed and the server response is not valid.
     */
    @Test
    public void testLoginFailedWithAuthenticatedExceptionAndValidServerResponse() {
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3", "Error 4");
        String body = "{\"errors\": [";
        for (int i = 0; i < errors.size(); i++) {
            body += "\"" + errors.get(i) + "\"";
            if (i < errors.size() - 1) {
                body += ",";
            }
        }
        body += "]}";
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(view).showLogging();
        verify(authApi).login(requestCaptor.capture());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call,
                new NotAuthenticatedException(createResponse(body)));
        verify(view).showLoggingFailed(listCaptor.capture());
        assertEquals(errors.size(), listCaptor.getValue().size());
        for (int i = 0; i < errors.size(); i++) {
            assertEquals(errors.get(i), listCaptor.getValue().get(i));
        }
        verify(view, never()).showLoggingSuccess();
    }

    /**
     * Test the behavior when login is called before the last login request response is received.
     */
    @Test
    public void testLoginCalledBeforeTheLastReqeustResponseReceived() {
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
    }

    /**
     * Test the behavior when login is called and the previous request responded with
     * success response.
     */
    @Test
    public void testLoginWhenThePreviousRequestIsSuccessful() {
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        presenter.login(email, password);
        verify(call, times(2)).enqueue(callbackCaptor.capture());
    }

    /**
     * Test the behavior when login is called and the previous request responded with
     * success response.
     */
    @Test
    public void testLoginWhenThePreviousRequestFailed() {
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        presenter.login(email, password);
        verify(call, times(2)).enqueue(callbackCaptor.capture());
    }

    /**
     * Test the behavior when the view is destroyed and the request response is still
     * not received.
     */
    @Test
    public void testViewDestroyedWhenRequestResponseNotYetReceived() {
        presenter.onViewLayoutCreated();
        presenter.login(email, password);
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewDestroyed();
        verify(call).cancel();
    }
}
