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
public class LikeViewPresenter extends BasePresenter {

    private static Logger logger = LoggerFactory.getLogger(LikeViewPresenter.class);
    private LikeView view;
    private LineupApi api;
    private User user;
    private Lineup lineup;
    private UserLike userLike;
    private List<UserLike> likes;
    private Call<List<UserLike>> likesCall;
    private Call<ServerResponse> addLikeCall;
    private Call<Void> removeLikeCall;
    private boolean viewLayoutCreated = false;
    private boolean loadLikesRequestSending = false;
    private boolean addLikeRequestSending = false;
    private boolean removeLikeRequestSending = false;

    public LikeViewPresenter(LikeView view, LineupApi api, User user) {
        this.view = view;
        this.api = api;
        this.user = user;
        userLike = new UserLike(user.getId(), user.getName(), new LineupLike());
    }

    /**
     * Called when the view is created.
     *
     * @param args view arguments
     */
    public void onViewCreated(Bundle args) {
        logger.info("onViewCreated");
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
        userLike.getPivot().setUser(user);
        userLike.getPivot().setLineup(lineup);
        this.loadLikes();
    }

    /**
     * Called when the view is visible to the user.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        this.viewLayoutCreated = true;
        if (this.loadLikesRequestSending) {
            view.showLoading();
        }
    }

    /**
     * Get all loaded likes.
     *
     * @return loaded likes
     */
    public List<UserLike> getLikes() {
        return likes;
    }

    /**
     * Load the current lineup likes.
     */
    public void loadLikes() {
        if (this.lineup == null) {
            throw new IllegalArgumentException("lineup is not set");
        }
        if (!loadLikesRequestSending) {
            logger.info("sending likes request");
            loadLikesRequestSending = true;
            likesCall = api.likes(lineup.getId(), true, null, null);
            if (viewLayoutCreated) {
                view.showLoading();
            }
            likesCall.enqueue(new Callback<List<UserLike>>() {
                @Override
                public void onResponse(Call<List<UserLike>> call, Response<List<UserLike>> response) {
                    onLikesLoadingSuccess(response);
                }

                @Override
                public void onFailure(Call<List<UserLike>> call, Throwable t) {
                    onLikesLoadingFailed(call, t);
                }
            });
        }
    }

    /**
     * Called when loading the likes is successful.
     *
     * @param response server response
     */
    public void onLikesLoadingSuccess(Response<List<UserLike>> response) {
        logger.info("likes request success");
        likesCall = null;
        loadLikesRequestSending = false;
        List<UserLike> likes = response.body();
        this.likes = likes;
        if (viewLayoutCreated) {
            view.showLoadingSuccess(likes);
            if (likes.contains(userLike)) {
                view.showRemoveLikeButton();
            } else {
                view.showAddLikeButton();
            }
        }
    }

    /**
     * Called when loading the likes failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    public void onLikesLoadingFailed(Call<List<UserLike>> call, Throwable t) {
        logger.info("likes request failed");
        loadLikesRequestSending = false;
        if (call.isCanceled()) {
            logger.info("likes request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showLoadingFailed();
                super.onRequestFailed(view, t);
            }
        }
        likesCall = null;
    }

    /**
     * Add a new like from the authenticated user to the lineup.
     */
    public void addLike() {
        if (this.lineup == null) {
            throw new IllegalArgumentException("lineup is not set yet");
        }
        if (!addLikeRequestSending) {
            if (likes.contains(userLike)) {
                throw new IllegalArgumentException("like already added");
            }
            logger.info("sending add like request");
            addLikeRequestSending = true;
            if (viewLayoutCreated) {
                view.showLikeAdding();
            }
            addLikeCall = api.addLike(lineup.getId());
            addLikeCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call,
                                       Response<ServerResponse> response) {
                    addLikeSuccess();
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    addLikeFailed(call, t);
                }
            });
        }
    }

    /**
     * Called when adding the like is successful.
     */
    private void addLikeSuccess() {
        logger.info("add like request success");
        addLikeRequestSending = false;
        addLikeCall = null;
        userLike.getPivot().setCreatedAt(new Date());
        this.likes.add(userLike);
        if (viewLayoutCreated) {
            view.showLikeAddingSuccess(userLike);
        }
    }

    /**
     * Called when adding the like failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void addLikeFailed(Call<ServerResponse> call, Throwable t) {
        logger.info("add like request failed");
        addLikeRequestSending = false;
        if (call.isCanceled()) {
            logger.info("add like request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showLikeAddingFailed();
                onRequestFailed(view, t);
            }
        }
        addLikeCall = null;
    }

    /**
     * Remove the like from the lineup.
     */
    public void removeLike() {
        if (this.lineup == null) {
            throw new IllegalArgumentException("lineup is not set yet");
        }
        if (!removeLikeRequestSending) {
            if (!likes.contains(userLike)) {
                throw new IllegalArgumentException("lineup not liked");
            }
            logger.info("sending onRemoveSuccess like request");
            removeLikeRequestSending = true;
            if (viewLayoutCreated) {
                view.showLikeRemoving();
            }
            removeLikeCall = api.deleteLike(lineup.getId());
            removeLikeCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    removeLikeSuccess();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    removeLikeFailed(call, t);
                }
            });
        }
    }

    /**
     * Called when removing the like is successful.
     */
    private void removeLikeSuccess() {
        logger.info("remove like request success");
        removeLikeRequestSending = false;
        removeLikeCall = null;
        this.likes.remove(userLike);
        if (viewLayoutCreated) {
            view.showLikeRemovingSuccess(userLike);
        }
    }

    /**
     * Called when removing the liek failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void removeLikeFailed(Call<Void> call, Throwable t) {
        logger.info("remove like request failed");
        removeLikeRequestSending = false;
        if (call.isCanceled()) {
            logger.info("remove like request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showLikeRemovingFailed();
                onRequestFailed(view, t);
            }
        }
        removeLikeCall = null;
    }

    /**
     * Called when the view is not anymore visible to the user.
     */
    public void onViewLayoutDestroyed() {
        logger.info("onViewLayoutDestroyed");
        this.viewLayoutCreated = false;
    }

    /**
     * Called when the view is destroyed.
     */
    public void onViewDestroyed() {
        logger.info("onViewDestroyed");
        if (likesCall != null) {
            likesCall.cancel();
        }
        if (addLikeCall != null) {
            addLikeCall.cancel();
        }
        if (removeLikeCall != null) {
            removeLikeCall.cancel();
        }
    }
}