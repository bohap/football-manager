package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.Comment;

import java.util.List;

/**
 * Created by Borce on 05.09.2016.
 */
public interface CommentView {

    String LINEUP_ID_KEY = "lineup_id";

    void showCommentsLoading();

    void showCommentsLoadingSuccess(List<Comment> comments);

    void showCommentLoadingFailed();
}
