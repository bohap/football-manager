package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;

import java.util.List;

/**
 * Created by Borce on 09.09.2016.
 */
public interface LikeView extends BaseView {

    String LINEUP_KEY = "lineup_id";

    void showLoading();

    void showLoadingSuccess(List<UserLike> likes);

    void showLoadingFailed();

    void showAddLikeButton();

    void showRemoveLikeButton();

    void showLikeAdding();

    void showLikeAddingSuccess(UserLike like);

    void showLikeAddingFailed();

    void showLikeRemoving();

    void showLikeRemovingSuccess(UserLike like);

    void showLikeRemovingFailed();
}
