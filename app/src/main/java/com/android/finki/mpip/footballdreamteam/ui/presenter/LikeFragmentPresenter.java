package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.LikeDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;
import com.android.finki.mpip.footballdreamteam.rest.response.ServerResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 15.08.2016.
 */
public class LikeFragmentPresenter implements Callback<List<UserLike>> {

    private static Logger logger = LoggerFactory.getLogger(LikeFragmentPresenter.class);

    private LikeFragment fragment;
    private LineupApi api;
    private User user;
    private final LineupDBService lineupDBService;
    private final LikeDBService likeDBService;

    private Lineup lineup;
    private boolean viewCreated = false;
    private boolean requestSending = false;
    private boolean likeAdded = false;


    public LikeFragmentPresenter(LikeFragment fragment, LineupApi api, User user,
                                 LineupDBService lineupDBService, LikeDBService likeDBService) {
        this.fragment = fragment;
        this.api = api;
        this.user = user;
        this.lineupDBService = lineupDBService;
        this.likeDBService = likeDBService;
    }

    /**
     * Load the lineup likes.
     *
     * @param args view arguments
     */
    public void loadLikes(Bundle args) {
        if (args == null) {
            String message  = "bundle can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        Serializable serializable = args.getSerializable(LikeFragment.LINEUP_KEY);
        if (!(serializable instanceof Lineup)) {
            String message = "lineup is required for this fragment";
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
            fragment.showLoading();
        }
    }

    /**
     * Called when the view layout is fully created.
     */
    public void onViewCreated() {
        viewCreated = true;
        if (requestSending) {
            fragment.showLoading();
        }
    }

    /**
     * Called when loading the likes is successful.
     *
     * @param call retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<List<UserLike>> call, Response<List<UserLike>> response) {
        if (response.isSuccessful()) {
            logger.info("likes request success");
            requestSending = false;
            List<UserLike> likes = response.body();
            fragment.showLoadingSuccess(likes);
            final UserLike userLike = new UserLike(user.getId(), user.getName(), null);
            if (likes.contains(userLike)) {
                fragment.showRemoveLikeButton();
                likeAdded = true;
            } else {
                fragment.showAddLikeButton();
                likeAdded = false;
            }
        } else {
            logger.info("likes request failed");
            requestSending = false;
            fragment.showLoadingFailed();
        }
    }

    /**
     * Called when loading the likes failed.
     *
     * @param call retrofit call
     * @param t exception that has been thrown
     */
    @Override
    public void onFailure(Call<List<UserLike>> call, Throwable t) {
        logger.info("likes request failed");
        requestSending = false;
        t.printStackTrace();
        fragment.showLoadingFailed();
    }

    /**
     * Add a new like from the authenticated user to the lineup.
     */
    public void addLike() {
        if (this.lineup == null) {
            String message = "lineup is not set yet";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (likeAdded) {
            String message = "like already added";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        logger.info("sending add like request");
        fragment.showLikeAdding();
        api.addLike(lineup.getId()).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    logger.info("add like request success");
                    addLikeSuccess();
                } else {
                    logger.info("add like request failed");
                    fragment.showLikeAddingFailed();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                logger.info("add like request failed");
                t.printStackTrace();
                fragment.showLikeAddingFailed();
            }
        });
    }

    /**
     * Called when adding the like is successful.
     */
    private void addLikeSuccess() {
        this.likeAdded = true;
        fragment.showLikeAddingSuccess(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
    }

    /**
     * Remove the like from the lineup.
     */
    public void removeLike() {
        if (this.lineup == null) {
            String message = "lineup is not set yet";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (!this.likeAdded) {
            String message = "lineup not liked";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        logger.info("sending remove like request");
        fragment.showLikeRemoving();
        api.deleteLike(lineup.getId()).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    likeRemovingSuccess();
                } else {
                    likeRemovingFailed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                likeRemovingFailed();
            }
        });
    }

    /**
     * Called when removing the like is successful.
     */
    private void likeRemovingSuccess() {
        logger.info("remove like request success");
        this.likeAdded = false;
        fragment.showLikeRemovingSuccess(new UserLike(user.getId(), user.getName(), null));
    }

    /**
     * Called when removing the like failed.
     */
    private void likeRemovingFailed() {
        logger.info("remove like request failed");
        fragment.showLikeRemovingFailed();
    }
}