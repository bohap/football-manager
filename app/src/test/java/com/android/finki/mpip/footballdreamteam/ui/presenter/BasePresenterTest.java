package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.exception.InternalServerErrorException;
import com.android.finki.mpip.footballdreamteam.exception.NotAuthenticatedException;
import com.android.finki.mpip.footballdreamteam.ui.component.BaseView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.verify;

/**
 * Created by Borce on 19.09.2016.
 */
public class BasePresenterTest {

    @Mock
    private BaseView view;

    private BasePresenter presenter = new BasePresenter();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test the behavior on onRequestFailed called with NotAuthenticated exception.
     */
    @Test
    public void testOnRequestFailedOnNotAuthenticatedException() {
        presenter.onRequestFailed(view, new NotAuthenticatedException());
        verify(view).showNotAuthenticated();
    }

    /**
     * Test the behavior when onRequestFailed is called with InternalServerError exception
     * and the response is null.
     */
    @Test
    public void testOnRequestFailedWithInternalServerErrorExceptionAndNullResponse() {
        presenter.onRequestFailed(view, new InternalServerErrorException());
        verify(view).showInternalServerError();
    }

    /**
     * Create a test response.
     *
     * @param body response body
     * @return test response
     */
    private Response createResponse(String body) {
        HttpUrl url = new HttpUrl.Builder().scheme("https").host("example.com").build();
        Request request = new Request.Builder().url(url).build();
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_0)
                .code(500)
                .body(ResponseBody.create(MediaType.parse("application/json"), body))
                .build();
    }

    /**
     * Test the behavior when onRequestFailed is called with InternalServerError exception
     * and the server response don't contains valid JSON body.
     */
    @Test
    public void testOnRequestFailedWithInternalServerErrorExceptionAndInvalidJSONBody() {
        String body = "JSON body";
        Response response = this.createResponse(body);
        presenter.onRequestFailed(view, new InternalServerErrorException(response));
        verify(view).showInternalServerError();
    }

    /**
     * Test the behavior when onRequestFailed is called with InternalServerError exception and
     * the server response is valid JSON string.
     */
    @Test
    public void testOnRequestFailedWithInternalServerErrorExceptionAndValidServerResponse() {
        String message = "Message";
        String body = "{\"code\": 500, \"status\": \"InternalServerError\", " +
                "\"message\": " + message + "}";
        Response response = this.createResponse(body);
        presenter.onRequestFailed(view, new InternalServerErrorException(response));
        verify(view).showInternalServerError(message);
    }

    /**
     * Test the behavior when request failed is called SocketTimeout exception.
     */
    @Test
    public void testOnRequestFailedWithSocketTimeoutException() {
        presenter.onRequestFailed(view, new SocketTimeoutException());
        verify(view).showSocketTimeout();
    }

    /**
     * Test the behavior when request failed is called UnknownHost exception.
     */
    @Test
    public void testOnRequestFailedWithUnknownHostException() {
        presenter.onRequestFailed(view, new UnknownHostException());
        verify(view).showNoInternetConnection();
    }
}
