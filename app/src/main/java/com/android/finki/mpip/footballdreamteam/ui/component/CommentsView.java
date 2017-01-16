package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.Comment;

import java.util.List;

/**
 * Created by Borce on 05.09.2016.
 */
public interface CommentsView extends BaseView {

    String LINEUP_ID_KEY = "lineup_id";

    void showCommentsLoading();

    void showCommentsLoadingSuccess(List<Comment> comments);

    void showCommentLoadingFailed();

    void showCommentAddingSuccess(Comment comment);

    void showCommentAddingFailed();

    void showCommentUpdatingSuccess(Comment comment, Comment newComment);

    void showCommentUpdatingFailed(Comment comment);

    void showCommentDeletingSuccess(Comment comment);

    void showCommentDeletingFailed(Comment comment);
}
