package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashActivityModule;
import com.android.finki.mpip.footballdreamteam.ui.dialog.InfoDialog;
import com.android.finki.mpip.footballdreamteam.ui.presenter.SplashActivityPresenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Borce on 25.07.2016.
 */
public class SplashActivity extends AppCompatActivity implements InfoDialog.Listener {

    @BindString(R.string.firstTimeStarted_message)
    String infoDialogText;

    @BindString(R.string.firstTimeStarted_title)
    String infoDIalogTitle;

    @Inject
    SplashActivityPresenter presenter;

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved state from when the activity is recreated
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ((MainApplication)this.getApplication()).getAppComponent()
                .plus(new SplashActivityModule(this)).inject(this);
        presenter.onActivityCreated();
    }

    /**
     * Show the info dialog.
     */
    public void showInfoDialog() {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        InfoDialog dialog = InfoDialog.newInstance(infoDIalogTitle, infoDialogText);
        dialog.show(transaction, InfoDialog.TAG);
    }

    /**
     * Called when InfoDialog is closed.
     */
    @Override
    public void onDialogDone() {
        presenter.checkIfUserIsAuthenticated();
    }

    /**
     * Show the LoginActivity.
     */
    public void showLoginActivity() {
        this.startActivity(new Intent(this, LoginActivity.class));
        super.finish();
    }

    /**
     * Show the HomeActivity.
     */
    public void showHomeActivity() {
        this.startActivity(new Intent(this, HomeActivity.class));
        super.finish();
    }
}
