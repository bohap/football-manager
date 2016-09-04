package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.R;

/**
 * Created by Borce on 07.08.2016.
 */
public class BaseActivity extends AppCompatActivity {

    private String appErrorMessage;
    private String connectionTimeoutMessage;
    private String serverErrorMessage;

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved instance state when the activity is recreated
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appErrorMessage = this.getString(R.string.app_errorMessage);
        connectionTimeoutMessage = this.getString(R.string.server_connectionTimeoutMessage);
        serverErrorMessage = this.getString(R.string.server_errorMessage);
    }

    /**
     * Show a Toast message when a app unknown error happened.
     */
    public void showAppErrorMessage() {
        Toast.makeText(this, appErrorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show a Toast message when a connection timeout error happened.
     */
    public void showConnectionTimeoutMessage() {
        Toast.makeText(this, connectionTimeoutMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show a Toast message when a server error happened.
     */
    public void showServerErrorMessage() {
        Toast.makeText(this, serverErrorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Change the given view visibility.
     *
     * @param view view to the changed
     * @param visible whatever hte view should be visible or not
     */
    void toggleVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
