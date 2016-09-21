package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.exception.UnProcessableEntityException;
import com.android.finki.mpip.footballdreamteam.exception.UserException;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.model.AuthUser;
import com.android.finki.mpip.footballdreamteam.rest.request.RegisterUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.RegisterUserResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.component.RegisterView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 20.09.2016.
 */
public class RegisterViewPresenterTest {

    @Mock
    private RegisterView view;

    @Mock
    private SharedPreferences preferences;

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private Context context;

    @Mock
    private UserDBService dbService;

    @Mock
    private AuthApi api;

    @Mock
    private Call<RegisterUserResponse> call;

    @Captor
    private ArgumentCaptor<RegisterUserRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<Callback<RegisterUserResponse>> callbackCaptor;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<List<String>> listCaptor;

    private RegisterViewPresenter presenter;
    private final String AUTH_USER_ID_KEY = "auth_user_id";
    private final String JWT_TOKEN_KEY = "auth_user_id";
    private final String name = "User";
    private final String email = "user@user.com";
    private final String password = "password";
    private User user = new User(1, name, email, password, null, null);
    private RegisterUserResponse response = new RegisterUserResponse(200, null, null,
            new AuthUser(user.getId(), name, email), "Token");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(context.getString(R.string.preference_auth_user_id_key)).thenReturn(AUTH_USER_ID_KEY);
        when(context.getString(R.string.preference_jwt_token_key)).thenReturn(JWT_TOKEN_KEY);
        when(preferences.edit()).thenReturn(editor);
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        when(api.register(any(RegisterUserRequest.class))).thenReturn(call);
        presenter = new RegisterViewPresenter(view, preferences, context, dbService, api);
    }

    /**
     * Test the behavior when register is called and the view layout is not created.
     */
    @Test
    public void testRegisterWhenViewLayoutNotCreated() {
        presenter.register(name, email, password, password);
        verify(view, never()).showRegistering();
        verify(api, never()).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the behavior when register is called with empty name, email, password and
     * repeat password.
     */
    @Test
    public void testRegisterWhenAllParamsAreEmpty() {
        presenter.onViewLayoutCreated();
        presenter.register(null, null, null, null);
        verify(view).showNameError();
        verify(view, never()).showNameOk();
        verify(view).showEmailError(RegisterView.EMAIL_ERROR.REQUIRED);
        verify(view, never()).showEmailError(RegisterView.EMAIL_ERROR.INVALID);
        verify(view, never()).showEmailOk();
        verify(view).showPasswordError();
        verify(view, never()).showPasswordOk();
        verify(view).showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.REQUIRED);
        verify(view, never()).showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.NOT_MATCH);
        verify(view, never()).showRepeatPasswordOk();
        verify(view, never()).showRegistering();
        verify(api, never()).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the behavior when register is called with invalid email and empty password.
     */
    @Test
    public void testRegisterWithInvalidEmailAndEmptyPassword() {
        presenter.onViewLayoutCreated();
        presenter.register(name, "email@", null, password);
        verify(view, never()).showNameError();
        verify(view).showNameOk();
        verify(view).showEmailError(RegisterView.EMAIL_ERROR.INVALID);
        verify(view, never()).showEmailError(RegisterView.EMAIL_ERROR.REQUIRED);
        verify(view, never()).showEmailOk();
        verify(view).showPasswordError();
        verify(view, never()).showPasswordOk();
        verify(view, never()).showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.REQUIRED);
        verify(view, never()).showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.NOT_MATCH);
        verify(view).showRepeatPasswordOk();
        verify(view, never()).showRegistering();
        verify(api, never()).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the behavior when register is called with not matching passwords.
     */
    @Test
    public void testRegisterNotMatchingPasswords() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password + "test");
        verify(view, never()).showNameError();
        verify(view).showNameOk();
        verify(view, never()).showEmailError(any(RegisterView.EMAIL_ERROR.class));
        verify(view, never()).showPasswordError();
        verify(view).showPasswordOk();
        verify(view, never()).showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.REQUIRED);
        verify(view).showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.NOT_MATCH);
        verify(view, never()).showRepeatPasswordOk();
        verify(view, never()).showRegistering();
        verify(api, never()).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the behavior when register is called and only the name has error.
     */
    @Test
    public void testRegisterWIthNameError() {
        presenter.onViewLayoutCreated();
        presenter.register(null, email, password, password);
        verify(view).showNameError();
        verify(view, never()).showNameOk();
        verify(view, never()).showEmailError(any(RegisterView.EMAIL_ERROR.class));
        verify(view).showEmailOk();
        verify(view, never()).showPasswordError();
        verify(view).showPasswordOk();
        verify(view, never())
                .showRepeatPasswordError(any(RegisterView.REPEAT_PASSWORD_ERROR.class));
        verify(view).showRepeatPasswordOk();
        verify(view, never()).showRegistering();
        verify(api, never()).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the behavior when register is called and only the email has error.
     */
    @Test
    public void testRegisterWIthEmailError() {
        presenter.onViewLayoutCreated();
        presenter.register(name, "user@", password, password);
        verify(view, never()).showNameError();
        verify(view).showNameOk();
        verify(view).showEmailError(RegisterView.EMAIL_ERROR.INVALID);
        verify(view, never()).showEmailError(RegisterView.EMAIL_ERROR.REQUIRED);
        verify(view, never()).showEmailOk();
        verify(view, never()).showPasswordError();
        verify(view).showPasswordOk();
        verify(view, never())
                .showRepeatPasswordError(any(RegisterView.REPEAT_PASSWORD_ERROR.class));
        verify(view).showRepeatPasswordOk();
        verify(view, never()).showRegistering();
        verify(api, never()).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the behavior when register is called and only the password has error.
     */
    @Test
    public void testRegisterWIthPasswordError() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, null, password);
        verify(view, never()).showNameError();
        verify(view).showNameOk();
        verify(view, never()).showEmailError(any(RegisterView.EMAIL_ERROR.class));
        verify(view).showEmailOk();
        verify(view).showPasswordError();
        verify(view, never()).showPasswordOk();
        verify(view, never())
                .showRepeatPasswordError(any(RegisterView.REPEAT_PASSWORD_ERROR.class));
        verify(view).showRepeatPasswordOk();
        verify(view, never()).showRegistering();
        verify(api, never()).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the behavior when register is called and only the repeat password has error.
     */
    @Test
    public void testRegisterWIthRepeatPasswordError() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password + "test");
        verify(view, never()).showNameError();
        verify(view).showNameOk();
        verify(view, never()).showEmailError(any(RegisterView.EMAIL_ERROR.class));
        verify(view).showEmailOk();
        verify(view, never()).showPasswordError();
        verify(view).showPasswordOk();
        verify(view).showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.NOT_MATCH);
        verify(view, never()).showRepeatPasswordError(RegisterView.REPEAT_PASSWORD_ERROR.REQUIRED);
        verify(view, never()).showRepeatPasswordOk();
        verify(view, never()).showRegistering();
        verify(api, never()).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the behavior when the register request is successful, the user email already exists
     * in the local database and deleting the user data from the database failed.
     */
    @Test
    public void testRegisterSuccessAndUserDeletingFailed() {
        when(dbService.getByEmail(anyString())).thenReturn(user);
        doThrow(new UserException("")).when(dbService).delete(anyInt());
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(api).register(requestCaptor.capture());
        assertEquals(name, requestCaptor.getValue().getName());
        assertEquals(email, requestCaptor.getValue().getEmail());
        assertEquals(password, requestCaptor.getValue().getPassword());
        assertEquals(password, requestCaptor.getValue().getPasswordConfirmation());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(dbService);
        inOrder.verify(dbService).open();
        inOrder.verify(dbService).getByEmail(user.getEmail());
        inOrder.verify(dbService).delete(user.getId());
        inOrder.verify(dbService).close();
        verify(dbService, never()).store(any(User.class));
        verify(view).showRegisteringFailed();
        verify(view).showInternalServerError();
        verify(view, never()).showRegisteringSuccess();
    }

    /**
     * Test the behavior when the registering is successful, storing the user data failed and the
     * view layout is destroyed when the response from the server is received.
     */
    @Test
    public void testRegisterSuccessAndUserStringFailedAndViewLayoutDestroyed() {
        when(dbService.getByEmail(anyString())).thenReturn(null);
        doThrow(new UserException("")).when(dbService).store(any(User.class));
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(dbService);
        inOrder.verify(dbService).open();
        inOrder.verify(dbService).getByEmail(user.getEmail());
        inOrder.verify(dbService).store(userCaptor.capture());
        assertEquals(user.getId(), userCaptor.getValue().getId());
        assertEquals(user.getName(), userCaptor.getValue().getName());
        assertEquals(user.getEmail(), userCaptor.getValue().getEmail());
        assertEquals(user.getPassword(), userCaptor.getValue().getPassword());
        inOrder.verify(dbService).close();
        verify(view, never()).showRegisteringFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showRegisteringSuccess();
    }

    /**
     * Test the behavior when the register request is successful and storing the user data in the
     * local database is successful.
     */
    @Test
    public void testRegisterSuccessAndUserStoringSuccess() {
        when(dbService.getByEmail(anyString())).thenReturn(null);
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(dbService);
        inOrder.verify(dbService).open();
        inOrder.verify(dbService).getByEmail(user.getEmail());
        inOrder.verify(dbService).store(userCaptor.capture());
        inOrder.verify(dbService).close();
        verify(editor).putInt(AUTH_USER_ID_KEY, user.getId());
        verify(editor).putString(JWT_TOKEN_KEY, response.getToken());
        verify(editor).apply();
        verify(view, never()).showRegisteringFailed();
        verify(view, never()).showInternalServerError();
        verify(view).showRegisteringSuccess();
    }

    /**
     * Test the behavior when the register request is successful, storing the user data in the
     * local database is successful and the view layout is destroyed when the server response is
     * received.
     */
    @Test
    public void testRegisterSuccessAndUserStoringSuccessAndViewLayoutDestroyed() {
        when(dbService.getByEmail(anyString())).thenReturn(null);
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        InOrder inOrder = inOrder(dbService);
        inOrder.verify(dbService).open();
        inOrder.verify(dbService).getByEmail(user.getEmail());
        inOrder.verify(dbService).store(userCaptor.capture());
        inOrder.verify(dbService).close();
        verify(editor).putInt(AUTH_USER_ID_KEY, user.getId());
        verify(editor).putString(JWT_TOKEN_KEY, response.getToken());
        verify(editor).apply();
        verify(view, never()).showRegisteringFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showRegisteringSuccess();
    }

    /**
     * Test the behavior when the register request failed and the view layout is destroyed when
     * the server response is received.
     */
    @Test
    public void testRegisterFailedAndViewLayoutDestroyed() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onFailure(call, new Throwable());
        verify(view, never()).showRegisteringFailed();
        verify(view, never()).showRegisteringFailed(anyListOf(String.class));
        verify(view, never()).showRegisteringSuccess();
    }

    /**
     * Test the behavior when registering request failed with a exception that is not
     * UnProcessableEntityException.
     */
    @Test
    public void testRegisterFailedWithNotBadRequestException() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new UnknownHostException());
        verify(view).showRegisteringFailed();
        verify(view, never()).showRegisteringFailed(anyListOf(String.class));
        verify(view).showNoInternetConnection();
        verify(view, never()).showRegisteringSuccess();
    }

    /**
     * Test the behavior when registering request failed with a UnProcessableEntityException and
     * the server response is null.
     */
    @Test
    public void testRegisterFailedWithBadRequestExceptionAndNullServerResponse() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new UnProcessableEntityException());
        verify(view).showRegisteringFailed();
        verify(view, never()).showRegisteringFailed(anyListOf(String.class));
        verify(view).showInternalServerError();
        verify(view, never()).showRegisteringSuccess();
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
     * Test the behavior when registering request failed with a UnProcessableEntityException and
     * the server response is not expected JSON.
     */
    @Test
    public void testRegisterFailedWithBadRequestExceptionAndInvalidJSON() {
        String body = "Invalid JSON";
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call,
                new UnProcessableEntityException(createResponse(body)));
        verify(view).showRegisteringFailed();
        verify(view, never()).showRegisteringFailed(anyListOf(String.class));
        verify(view).showInternalServerError();
        verify(view, never()).showRegisteringSuccess();
    }

    /**
     * Test the behavior when registering request failed with a UnProcessableEntityException and
     * the server response is expected JSON.
     */
    @Test
    public void testRegisterFailedWithBadRequestExceptionAndValidJSON() {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("field_1", Arrays.asList("Field 1 Error 1", "Field 1 Error 2"));
        errors.put("field_2", Arrays.asList("Field 2 Error 1", "Field 2 Error 2"));
        String body = "{\"errors\": {";
        for (Map.Entry<String, List<String>> entry: errors.entrySet()) {
            body += "\"" + entry.getKey() + "\": [";
            for (String error : entry.getValue()) {
                body += "\"" + error + "\",";
            }
            body = body.substring(0, body.length() - 1);
            body += "],";
        }
        body = body.substring(0, body.length() - 1);
        body += "}}";
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call,
                new UnProcessableEntityException(createResponse(body)));
        verify(view, never()).showRegisteringFailed();
        verify(view).showRegisteringFailed(listCaptor.capture());
        verify(view, never()).showRegisteringSuccess();
        List<String> aErrors = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
            aErrors.addAll(entry.getValue());
        }
        assertEquals(aErrors.size(), listCaptor.getValue().size());
        for (int i = 0; i < errors.size(); i++) {
            assertEquals(aErrors.get(i), listCaptor.getValue().get(i));
        }
    }

    /**
     * Test the behavior when the register is called before the last register request response
     * is received.
     */
    @Test
    public void testRegisterCalledBeforeTheLatRequestResponseIsRecevide() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
    }

    /**
     * Test the behavior when the register is called after the last register request succeeded.
     */
    @Test
    public void testRegisterCalledAfterTheLastRequestSucceeded() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(response));
        presenter.register(name, email, password, password);
        verify(call, times(2)).enqueue(callbackCaptor.capture());
    }

    /**
     * Test the behavior when the register is called after the last register request failed.
     */
    @Test
    public void testRegisterCalledAfterTheLastRequestFailed() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        presenter.register(name, email, password, password);
        verify(call, times(2)).enqueue(callbackCaptor.capture());
    }

    @Test
    public void testRegisterRequestIsCanceledWhenTheViewIsDestroyed() {
        presenter.onViewLayoutCreated();
        presenter.register(name, email, password, password);
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewDestroyed();
        verify(call).cancel();
    }
}
