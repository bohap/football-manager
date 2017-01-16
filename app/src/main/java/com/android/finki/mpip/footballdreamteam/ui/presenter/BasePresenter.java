package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.exception.InternalServerErrorException;
import com.android.finki.mpip.footballdreamteam.exception.NotAuthenticatedException;
import com.android.finki.mpip.footballdreamteam.rest.response.ServerResponse;
import com.android.finki.mpip.footballdreamteam.ui.component.BaseView;
import com.android.finki.mpip.footballdreamteam.ui.component.LoginView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Response;

/**
 * Created by Borce on 08.09.2016.
 */
public class BasePresenter {

    /**
     * Get the message from the given response.
     *
     * @param response server response
     * @return response message
     */
    private String getResponseMessage(Response response) {
        if (response == null) {
            return null;
        }
        String message;
        try {
            Gson gson = new Gson();
            ServerResponse error = gson.fromJson(response.body().string(), ServerResponse.class);
            message = error.getMessage();
        } catch (IOException | JsonSyntaxException exp) {
            exp.printStackTrace();
            message = null;
        }
        return message;
    }

    /**
     * Handle failed request.
     *
     * @param view the view that is visible to the user
     * @param t    exception that has been thrown
     */
    public void onRequestFailed(BaseView view, Throwable t) {
        if (t instanceof NotAuthenticatedException && !(view instanceof LoginView)) {
            view.showNotAuthenticated();
        } else if (t instanceof InternalServerErrorException) {
            Response response = ((InternalServerErrorException) t).getResponse();
            String message = this.getResponseMessage(response);
            if (message == null) {
                view.showInternalServerError();
            } else {
                view.showInternalServerError(message);
            }
        } else if (t instanceof SocketTimeoutException) {
            view.showSocketTimeout();
        } else if (t instanceof UnknownHostException) {
            view.showNoInternetConnection();
        }
    }
}
