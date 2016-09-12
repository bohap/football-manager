package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.CommentRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.CommentResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.CommentsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 05.09.2016.
 */
public class CommentsViewPresenter extends BasePresenter {

    private static final Logger logger = LoggerFactory.getLogger(CommentsViewPresenter.class);
    private CommentsView view;
    private LineupApi api;
    private User user;
    private int lineupId = -1;
    private Call<List<Comment>> loadCommentsCall;
    private Queue<CommentCall> calls = new ArrayDeque<>();
    private boolean loadCommentsRequestSending = false;
    private boolean addCommentRequestSending = false;
    private boolean viewLayoutCreated = false;

    public CommentsViewPresenter(CommentsView view, LineupApi api, User user) {
        this.view = view;
        this.api = api;
        this.user = user;
    }

    /**
     * Called when the view is created.
     *
     * @param args view arguments
     */
    public void onViewCreated(Bundle args) {
        lineupId = args.getInt(CommentsView.LINEUP_ID_KEY, -1);
        if (lineupId == -1) {
            throw new IllegalArgumentException("lineup id is not set");
        }
        this.loadComments();
    }

    /**
     * Called when the view layout is created.
     */
    public void onViewLayoutCreated() {
        this.viewLayoutCreated = true;
        if (loadCommentsRequestSending) {
            view.showCommentsLoading();
        }
    }

    /**
     * Called when the view layout is destroyed.
     */
    public void onViewLayoutDestroyed() {
        this.viewLayoutCreated = false;
    }

    /**
     * Called when the view is destroyed.
     */
    public void onViewDestroyed() {
        if (loadCommentsCall != null) {
            loadCommentsCall.cancel();
        }
        CommentCall call = calls.peek();
        if (call != null) {
            call.cancel();
        }
    }

    /**
     * Load the comments from the server.
     */
    public void loadComments() {
        if (lineupId == -1) {
            throw new IllegalArgumentException("lineup id is not set");
        }
        if (!loadCommentsRequestSending) {
            logger.info(String.format("loading comment for lineup with id %d", lineupId));
            loadCommentsRequestSending = true;
            if (viewLayoutCreated) {
                view.showCommentsLoading();
            }
            loadCommentsCall = api.comments(lineupId, true, null, null);
            loadCommentsCall.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call,
                                       Response<List<Comment>> response) {
                    onLoadingCommentsSuccess(response);
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    onLoadingCommentsFailed(call, t);
                }
            });
        }
    }

    /**
     * Called when loading the comments from the server is successful.
     *
     * @param response server response
     */
    public void onLoadingCommentsSuccess(Response<List<Comment>> response) {
        logger.info("comments loaded successfully");
        loadCommentsRequestSending = false;
        loadCommentsCall = null;
        if (viewLayoutCreated) {
            view.showCommentsLoadingSuccess(response.body());
        }
    }

    /**
     * Called when loading the comments from the server failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    public void onLoadingCommentsFailed(Call<List<Comment>> call, Throwable t) {
        logger.info("comments loading failed");
        loadCommentsRequestSending = false;
        if (call.isCanceled()) {
            logger.info("comments loading request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showCommentLoadingFailed();
                super.onRequestFailed(view, t);
            }
        }
        loadCommentsCall = null;
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
     * Execute a call from the queue of call.
     */
    private void executeCall() {
        CommentCall call = calls.peek();
        if (call != null && !call.isSending()) {
            call.execute();
        }
    }

    /**
     * Send a request for adding new comment for the lineup.
     *
     * @param body comment body
     */
    public void addComment(String body) {
        if (lineupId == -1) {
            throw new IllegalArgumentException("lineup id not set");
        }
        if (body == null) {
            throw new IllegalArgumentException("body is required");
        }
        if (!addCommentRequestSending) {
            addCommentRequestSending = true;
            calls.add(new AddCommentCall(body));
            this.executeCall();
        }
    }

    /**
     * Called when adding the comment is successful.
     *
     * @param response server response
     */
    private void onCommentAddingSuccess(Response<CommentResponse> response) {
        logger.info("adding comment success");
        addCommentRequestSending = false;
        Comment comment = response.body().getComment();
        comment.setLineupId(lineupId);
        comment.setUser(user);
        if (viewLayoutCreated) {
            view.showCommentAddingSuccess(comment);
        }
        calls.poll();
        this.executeCall();
    }

    /**
     * Called when adding the comment failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void onCommentAddingFailed(Call<CommentResponse> call, Throwable t) {
        logger.info("adding comment failed");
        addCommentRequestSending = false;
        if (call.isCanceled()) {
            logger.info("add comment request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showCommentAddingFailed();
                super.onRequestFailed(view, t);
            }
        }
        calls.poll();
        this.executeCall();
    }

    /**
     * Send a request for updating the comment.
     *
     * @param comment comment to be updated
     * @param newBody new comment body
     */
    public void updateComment(Comment comment, String newBody) {
        calls.add(new UpdateCommentCall(comment, newBody));
        this.executeCall();
    }

    /**
     * Called when updating the comment is successful.
     *
     * @param comment  comment that was updating
     * @param response server response
     */
    public void onCommentUpdatingSuccess(Comment comment, Response<CommentResponse> response) {
        logger.info(String.format("onUpdateSuccess comment success, comment id %d", comment.getId()));
        if (viewLayoutCreated) {
            view.showCommentUpdatingSuccess(comment, response.body().getComment());
        }
        calls.poll();
        this.executeCall();
    }

    /**
     * Called when updating the comment failed.
     *
     * @param comment comment that was updating
     * @param call    retrofit call
     * @param t       exception that has been thrown
     */
    public void onCommentUpdatingFailed(Comment comment, Call<CommentResponse> call, Throwable t) {
        logger.info(String.format("onUpdateSuccess comment failed, comment id %d", comment.getId()));
        if (call.isCanceled()) {
            logger.info(String.format("onUpdateSuccess comment canceled, comment id %d", comment.getId()));
        } else {
            if (viewLayoutCreated) {
                view.showCommentUpdatingFailed(comment);
                super.onRequestFailed(view, t);
            }
        }
        calls.poll();
        this.executeCall();
    }

    /**
     * Send a request for deleting the comment.
     *
     * @param comment comment to be deleted
     */
    public void deleteComment(Comment comment) {
        calls.add(new DeleteCommentCall(comment));
        this.executeCall();
    }

    /**
     * Called when deleting the comment is successful.
     *
     * @param comment failed deleted comment
     */
    public void onCommentDeletingSuccess(Comment comment) {
        logger.info(String.format("delete comment success, comment %d", comment.getId()));
        if (viewLayoutCreated) {
            view.showCommentDeletingSuccess(comment);
        }
        calls.poll();
        this.executeCall();
    }

    /**
     * Called when deleting the comment failed.
     *
     * @param comment failed deleted comment
     * @param call    retrofit call
     * @param t       exception that has been thrown
     */
    public void onCommentDeletingFailed(Comment comment, Call<Void> call, Throwable t) {
        logger.info(String.format("delete comment failed, comment %d", comment.getId()));
        if (call.isCanceled()) {
            logger.info(String.format("delete comment canceled, comment %d", comment.getId()));
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showCommentDeletingFailed(comment);
                super.onRequestFailed(view, t);
            }
        }
        calls.poll();
        this.executeCall();
    }

    /**
     * Wrapper class for the add, onUpdateSuccess and delete comment calls.
     */
    public abstract class CommentCall {

        public abstract void execute();

        public abstract void cancel();

        public abstract boolean isSending();
    }

    /**
     * Wrapper class for adding comment call.
     */
    public class AddCommentCall extends CommentCall implements Callback<CommentResponse> {

        private String body;
        private Call<CommentResponse> call;

        public AddCommentCall(String body) {
            this.body = body;
        }

        /**
         * Send a request for adding a new comment.
         */
        @Override
        public void execute() {
            logger.info("add comment request");
            call = api.addComment(lineupId, new CommentRequest(body));
            call.enqueue(this);
        }

        /**
         * Called when adding the comment is successful.
         *
         * @param call     retrofit call
         * @param response server response
         */
        @Override
        public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
            onCommentAddingSuccess(response);
            this.call = null;
        }

        /**
         * Called when adding the comment failed.
         *
         * @param call retrofit call
         * @param t    exception that has been thrown
         */
        @Override
        public void onFailure(Call<CommentResponse> call, Throwable t) {
            onCommentAddingFailed(call, t);
            this.call = null;
        }

        /**
         * Cancel the request.
         */
        @Override
        public void cancel() {
            if (this.call != null) {
                this.call.cancel();
            }
        }

        /**
         * Checks if a request is sending.
         *
         * @return whatever a request is sending
         */
        @Override
        public boolean isSending() {
            return call != null;
        }
    }

    /**
     * Wrapper class for updating comment call.
     */
    public class UpdateCommentCall extends CommentCall implements Callback<CommentResponse> {

        private Comment comment;
        private String newBody;
        private Call<CommentResponse> call;

        public UpdateCommentCall(Comment comment, String newBody) {
            this.comment = comment;
            this.newBody = newBody;
        }

        /**
         * Send a request for updating the comment.
         */
        public void execute() {
            logger.info(String.format("onUpdateSuccess comment request, comment id %d", comment.getId()));
            call = api.updateComment(lineupId, comment.getId(), new CommentRequest(newBody));
            call.enqueue(this);
        }

        /**
         * Called when updating the comment is successful.
         *
         * @param call     retrofit call
         * @param response server response
         */
        @Override
        public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
            this.call = null;
            onCommentUpdatingSuccess(comment, response);
        }

        /**
         * Called when updating the comment failed.
         *
         * @param call retrofit call
         * @param t    exception that has been thrown
         */
        @Override
        public void onFailure(Call<CommentResponse> call, Throwable t) {
            onCommentUpdatingFailed(comment, call, t);
            this.call = null;
        }

        /**
         * Cancel the call.
         */
        public void cancel() {
            if (this.call != null) {
                call.cancel();
            }
        }

        /**
         * Checks if a request is sending.
         *
         * @return whatever a request is sending
         */
        @Override
        public boolean isSending() {
            return call != null;
        }
    }

    /**
     * Wrapper class for deleting comment call.
     */
    public class DeleteCommentCall extends CommentCall implements Callback<Void> {

        private Comment comment;
        private Call<Void> call;

        public DeleteCommentCall(Comment comment) {
            this.comment = comment;
        }

        /**
         * Send a request for deleting the comment.
         */
        public void execute() {
            logger.info(String.format("delete comment request, comment id %d", comment.getId()));
            call = api.deleteComment(lineupId, comment.getId());
            call.enqueue(this);
        }

        /**
         * Called when deleting the comment is successful.
         *
         * @param call     retrofit call
         * @param response server response
         */
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            this.call = null;
            onCommentDeletingSuccess(comment);
        }

        /**
         * Called when updating the comment failed.
         *
         * @param call retrofit call
         * @param t    exception that has been thrown
         */
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            onCommentDeletingFailed(comment, call, t);
            this.call = null;
        }

        /**
         * Cancel the request.
         */
        @Override
        public void cancel() {
            if (call != null) {
                call.cancel();
            }
        }

        /**
         * Checks if a request is sending.
         *
         * @return whatever a request is sending
         */
        @Override
        public boolean isSending() {
            return call != null;
        }
    }
}
