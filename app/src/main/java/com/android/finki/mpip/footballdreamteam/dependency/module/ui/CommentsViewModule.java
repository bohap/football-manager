package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.CommentView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CommentsViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 05.09.2016.
 */
@Module
public class CommentsViewModule {

    private CommentView view;

    public CommentsViewModule(CommentView view) {
        this.view = view;
    }

    /**
     * Provides instance of the CommentView presenter.
     *
     * @param api instance of the LineupApi
     * @param user authenticated user
     * @return CommentView presenter
     */
    @Provides
    @ViewScope
    public CommentsViewPresenter provideCommentViewPresenter(LineupApi api, User user) {
        return new CommentsViewPresenter(view, api, user);
    }
}
