package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.support.v4.app.Fragment;

import com.android.finki.mpip.footballdreamteam.ui.component.BaseView;

/**
 * Created by Borce on 08.09.2016.
 */
public class BaseFragment extends Fragment implements BaseView {

    /**
     * Called when the user token has expired or some other has occurred and the
     * user need to login again.
     */
    @Override
    public void showNotAuthenticated() {
        if (this.getActivity() instanceof BaseView) {
            ((BaseView) this.getActivity()).showNotAuthenticated();
        }
    }

    /**
     * Called when the server responded with 500 error code.
     */
    @Override
    public void showInternalServerError(String message) {
        if (this.getActivity() instanceof BaseView) {
            ((BaseView) this.getActivity()).showInternalServerError(message);
        }
    }

    /**
     * Called when the server responded with 500 error code.
     */
    @Override
    public void showInternalServerError() {
        if (this.getActivity() instanceof BaseView) {
            ((BaseView) this.getActivity()).showInternalServerError();
        }
    }

    /**
     * Called when the request to the server timeout out.
     */
    @Override
    public void showSocketTimeout() {
        if (this.getActivity() instanceof BaseView) {
            ((BaseView) this.getActivity()).showSocketTimeout();
        }
    }

    /**
     * Called when the internet connection is lost.
     */
    @Override
    public void showNoInternetConnection() {
        if (this.getActivity() instanceof BaseView) {
            ((BaseView) this.getActivity()).showNoInternetConnection();
        }
    }
}
