package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.ui.component.BaseView;
import com.android.finki.mpip.footballdreamteam.ui.dialog.InfoDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by Borce on 07.08.2016.
 */
public class BaseActivity extends AppCompatActivity implements BaseView {

    private Logger logger = LoggerFactory.getLogger(BaseActivity.class);
    private SharedPreferences preferences;
    private String internalServerErrorDialogTitle;
    private String internalServerErrorDialogMessage;
    private String socketTimeoutDialogTitle;
    private String socketTimeoutDialogMessage;
    private String noInternetConnectionDialogTitle;
    private String noInternetConnectionDialogMessage;
    private String AUTH_USER_KEY;
    private String JWT_TOKEN;

    /**
     * Set the application shared preferences.
     *
     * @param preferences application shared preferences
     */
    @Inject
    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved instance state when the activity is recreated
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        ((MainApplication) this.getApplication()).getAppComponent().plus().inject(this);

        internalServerErrorDialogTitle = this.getString(R.string.internalServerError_title);
        internalServerErrorDialogMessage = this.getString(R.string.internalServerError_message);
        socketTimeoutDialogTitle = this.getString(R.string.socketTimeout_title);
        socketTimeoutDialogMessage = this.getString(R.string.socketTimeout_message);
        noInternetConnectionDialogTitle = this.getString(R.string.noInternetConnection_title);
        noInternetConnectionDialogMessage = this.getString(R.string.noInternetConnection_message);
        this.AUTH_USER_KEY = this.getString(R.string.preference_auth_user_id_key);
        this.JWT_TOKEN = this.getString(R.string.preference_jwt_token_key);
    }

    /**
     * Change the given view visibility.
     *
     * @param view    view to the changed
     * @param visible whatever hte view should be visible or not
     */
    void toggleVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * Called when the user token has expired or some other has occurred and the
     * user need to login again.
     */
    @Override
    public void showNotAuthenticated() {
        ((MainApplication) this.getApplication()).releaseAuthComponent();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    /**
     * Called when the server responded with 500 error code.
     */
    @Override
    public void showInternalServerError() {
        this.showInternalServerError(null);
    }

    /**
     * Called when the server responded with 500 error code.
     *
     * @param message server response message
     */
    @Override
    public void showInternalServerError(String message) {
        String dialogMessage = String.format("%s\n%s", internalServerErrorDialogMessage, message);
        this.showInfoDialog(internalServerErrorDialogTitle, dialogMessage);
    }

    /**
     * Called when the request to the server timeout out.
     */
    @Override
    public void showSocketTimeout() {
        this.showInfoDialog(socketTimeoutDialogTitle, socketTimeoutDialogMessage);
    }

    /**
     * Called when the internet connection is lost.
     */
    @Override
    public void showNoInternetConnection() {
        this.showInfoDialog(noInternetConnectionDialogTitle, noInternetConnectionDialogMessage);
    }

    /**
     * Show the info dialog with the given title and message.
     *
     * @param title   dialog title
     * @param message dialog message
     */
    private void showInfoDialog(String title, String message) {
        InfoDialog dialog = InfoDialog.newInstance(title, message);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, InfoDialog.TAG);
    }
}
