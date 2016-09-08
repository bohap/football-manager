package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.CommentsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 05.09.2016.
 */
public class CommentsViewPresenter implements Callback<List<Comment>> {

    private static final Logger logger = LoggerFactory.getLogger(CommentsViewPresenter.class);

    private CommentsView view;
    private LineupApi api;
    private User user;

    private int lineupId = -1;
    private boolean loadingComments = false;
    private boolean viewCreated = false;

    public CommentsViewPresenter(CommentsView view, LineupApi api, User user) {
        this.view = view;
        this.api = api;
        this.user = user;
    }

    /**
     * Get the authenticated user.
     *
     * @return authenticated user
     */
    public User getUser() {
        return user;
    }

    /**
     * Called when the view has been crated.
     *
     * @param args view bundle state
     */
    public void loadComments(Bundle args) {
        lineupId = args.getInt(CommentsView.LINEUP_ID_KEY, 1);
        if (lineupId == -1) {
            throw new IllegalArgumentException("lineup id is not set");
        }
        this.loadComments();
    }

    /**
     * Load the comments from the server.
     */
    public void loadComments() {
        if (lineupId == -1) {
            throw new IllegalArgumentException("lineup id is not set");
        }
        logger.info(String.format("loading comment for lineup with id %d", lineupId));
        if (viewCreated) {
            view.showCommentsLoading();
        }
        loadingComments = true;
        api.comments(lineupId, true, null, null).enqueue(this);
    }

    /**
     * Called when the view is visible to the user.
     */
    public void onViewCrated() {
        viewCreated = true;
        if (loadingComments) {
            view.showCommentsLoading();
        }
    }

    /**
     * Called when loading the comments from the server is successful.
     *
     * @param call     retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
        logger.info("comments loaded successfully");
        loadingComments = false;
        view.showCommentsLoadingSuccess(response.body());
    }

    /**
     * Called when loading the comments from the server failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    @Override
    public void onFailure(Call<List<Comment>> call, Throwable t) {
        logger.info("comments loading failed");
        loadingComments = false;
        view.showCommentLoadingFailed();
    }
}
