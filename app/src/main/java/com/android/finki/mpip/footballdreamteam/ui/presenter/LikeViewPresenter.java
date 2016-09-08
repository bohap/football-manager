package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;
import com.android.finki.mpip.footballdreamteam.rest.response.ServerResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.LikeView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 15.08.2016.
 */
public class LikeViewPresenter extends BasePresenter implements Callback<List<UserLike>> {

    private static Logger logger = LoggerFactory.getLogger(LikeViewPresenter.class);

    private LikeView view;
    private LineupApi api;
    private User user;

    private Lineup lineup;
    private boolean viewCreated = false;
    private boolean requestSending = false;
    private boolean likeAdded = false;


    public LikeViewPresenter(LikeView view, LineupApi api, User user) {
        this.view = view;
        this.api = api;
        this.user = user;
    }

    /**
     * Load the lineup likes.
     *
     * @param args view arguments
     */
    public void loadLikes(Bundle args) {
        if (args == null) {
            String message = "bundle can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        Serializable serializable = args.getSerializable(LikeView.LINEUP_KEY);
        if (!(serializable instanceof Lineup)) {
            String message = "lineup is required for this view";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.lineup = (Lineup) serializable;
        this.loadData();
    }

    /**
     * Load the current lineup likes.
     */
    public void loadLikes() {
        if (this.lineup == null) {
            String message = "lineup can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.loadData();
    }

    /**
     * Send a request to load the data from the server.
     */
    private void loadData() {
        logger.info("sending likes request");
        requestSending = true;
        Call<List<UserLike>> call = api.likes(lineup.getId(), true, null, null);
        call.enqueue(this);
        if (viewCreated) {
            view.showLoading();
        }
    }

    /**
     * Called when the view layout is fully created.
     */
    public void onViewCreated() {
        viewCreated = true;
        if (requestSending) {
            view.showLoading();
        }
    }

    /**
     * Called when loading the likes is successful.
     *
     * @param call     retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<List<UserLike>> call, Response<List<UserLike>> response) {
        logger.info("likes request success");
        requestSending = false;
        List<UserLike> likes = response.body();
        view.showLoadingSuccess(likes);
        final UserLike userLike = new UserLike(user.getId(), user.getName(), null);
        if (likes.contains(userLike)) {
            view.showRemoveLikeButton();
            likeAdded = true;
        } else {
            view.showAddLikeButton();
            likeAdded = false;
        }
    }

    /**
     * Called when loading the likes failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    @Override
    public void onFailure(Call<List<UserLike>> call, Throwable t) {
        logger.info("likes request failed");
        requestSending = false;
        t.printStackTrace();
        view.showLoadingFailed();
        super.onRequestFailed(view, t);
    }

    /**
     * Add a new like from the authenticated user to the lineup.
     */
    public void addLike() {
        if (this.lineup == null) {
            throw new IllegalArgumentException("lineup is not set yet");
        }
        if (likeAdded) {
            throw new IllegalArgumentException("like already added");
        }
        logger.info("sending add like request");
        view.showLikeAdding();
        api.addLike(lineup.getId()).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                logger.info("add like request success");
                likeAdded = true;
                view.showLikeAddingSuccess(new UserLike(user.getId(), user.getName(),
                        new LineupLike(user, lineup, new Date())));
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                logger.info("add like request failed");
                t.printStackTrace();
                view.showLikeAddingFailed();
                onRequestFailed(view, t);
            }
        });
    }

    /**
     * Remove the like from the lineup.
     */
    public void removeLike() {
        if (this.lineup == null) {
            throw new IllegalArgumentException("lineup is not set yet");
        }
        if (!this.likeAdded) {
            throw new IllegalArgumentException("lineup not liked");
        }
        logger.info("sending remove like request");
        view.showLikeRemoving();
        api.deleteLike(lineup.getId()).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                logger.info("remove like request success");
                likeAdded = false;
                view.showLikeRemovingSuccess(new UserLike(user.getId(), user.getName(), null));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                logger.info("remove like request failed");
                t.printStackTrace();
                view.showLikeRemovingFailed();
                onRequestFailed(view, t);
            }
        });
    }
}