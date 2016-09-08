package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashViewModule;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.component.SplashView;
import com.android.finki.mpip.footballdreamteam.ui.dialog.InfoDialog;
import com.android.finki.mpip.footballdreamteam.ui.presenter.SplashViewPresenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Borce on 25.07.2016.
 */
public class SplashActivity extends AppCompatActivity implements SplashView, InfoDialog.Listener {

    private SplashViewPresenter presenter;

    @BindString(R.string.firstTimeStarted_message)
    String infoDialogText;

    @BindString(R.string.firstTimeStarted_title)
    String infoDialogTitle;

    /**
     * Set the presenter for the activity.
     *
     * @param presenter activity presenter
     */
    @Inject
    public void setPresenter(SplashViewPresenter presenter) {
        this.presenter = presenter;
    }

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
                .plus(new SplashViewModule(this)).inject(this);
        presenter.onActivityCreated();
    }

    /**
     * Show the info dialog.
     */
    @Override
    public void showInfoDialog() {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        InfoDialog dialog = InfoDialog.newInstance(infoDialogTitle, infoDialogText);
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
    @Override
    public void showLoginView() {
        this.startActivity(new Intent(this, LoginActivity.class));
        super.finish();
    }

    /**
     * Show the HomeActivity.
     */
    @Override
    public void showHomeView() {
        this.startActivity(new Intent(this, HomeActivity.class));
        super.finish();
    }

    /**
     * Crete the UserComponent for the application.
     *
     * @param user authenticated user
     */
    @Override
    public void createUserComponent(User user) {
        ((MainApplication)this.getApplication()).createUserComponent(user);
    }
}
