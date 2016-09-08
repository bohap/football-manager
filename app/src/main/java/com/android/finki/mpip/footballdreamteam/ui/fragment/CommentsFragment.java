package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CommentsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CommentsViewModule;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.ui.adapter.CommentsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.component.CommentsView;
import com.android.finki.mpip.footballdreamteam.ui.listener.ActivityTitleSetterListener;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CommentsViewPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;

/**
 * Created by Borce on 15.08.2016.
 */
public class CommentsFragment extends Fragment implements CommentsView, CommentsAdapter.Listener {

    private static Logger logger = LoggerFactory.getLogger(CommentsFragment.class);

    private CommentsViewPresenter presenter;
    private CommentsViewComponent component;
    private Unbinder unbinder;

    @BindString(R.string.commentsLayout_title)
    String title;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindString(R.string.commentsLayout_spinnerText)
    String spinnerText;

    @BindView(R.id.error_loading)
    RelativeLayout failedRequestLayout;

    @BindView(R.id.commentsLayout_mainContent)
    RelativeLayout mainLayout;

    @BindView(R.id.commentsLayout_listView)
    ListView listView;

    private CommentsAdapter adapter;

    /**
     * Create a new instance of the fragment.
     *
     * @param lineupId id of the lineup
     * @return new instance of the fragment
     */
    public static CommentsFragment newInstance(int lineupId) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putInt(CommentsView.LINEUP_ID_KEY, lineupId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Set the presenter for the fragment.
     *
     * @param presenter instance of fragment presenter
     */
    @Inject
    public void setPresenter(CommentsViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the fragment is ready to be created.
     *
     * @param savedInstanceState saved state for when the fragment is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new CommentsViewModule(this));
        component.inject(this);
        presenter.loadComments(this.getArguments());
    }

    /**
     * Called when the fragment view is ready to be creted.
     *
     * @param inflater           system layout inflater service
     * @param container          fragment parent container
     * @param savedInstanceState saved state from when the fragment is recreated
     * @return fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.onViewCrated();
        if (this.getActivity() instanceof ActivityTitleSetterListener) {
            ((ActivityTitleSetterListener) this.getActivity()).setTitle(title);
        }
        return view;
    }

    /**
     * Called before the fragment view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Called when the comments data has been started loading form the server.
     */
    @Override
    public void showCommentsLoading() {
        if (this.isVisible()) {
            txtSpinner.setText(spinnerText);
            spinner.setVisibility(View.VISIBLE);
            failedRequestLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Called when loading the comments is successful.
     *
     * @param comments all lineup comments
     */
    @Override
    public void showCommentsLoadingSuccess(List<Comment> comments) {
        if (this.isVisible()) {
            spinner.setVisibility(View.GONE);
            failedRequestLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);

            adapter = new CommentsAdapter(this.getActivity(), comments, presenter.getUser(), this);
            listView.setAdapter(adapter);
        }
    }

    /**
     * Called when a error occurred while loading comments data.
     */
    @Override
    public void showCommentLoadingFailed() {
        if (this.isVisible()) {
            spinner.setVisibility(View.GONE);
            failedRequestLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Handle click on the button "Try Again".
     */
    @OnClick(R.id.error_loading_btn_tryAgain)
    void reload() {
        presenter.loadComments();
    }

    /**
     * Handle click on a comment item.
     *
     * @param position comment position in the list view
     */
    @OnItemClick(R.id.commentsLayout_listView)
    void onCommentSelected(int position) {
        //TODO
        listView.getChildAt(position);
    }

    /**
     * Called when the button for updating the comment is clicked.
     */
    @Override
    public void updateComment() {
        //TODO
        adapter.showCommentUpdating(listView.getChildAt(adapter.getSelectedPosition()));
    }

    /**
     * Called when the button for removing the comment is clicked.
     */
    @Override
    public void deleteComment() {
        //TODO
        adapter.showCommentUpdating(listView.getChildAt(adapter.getSelectedPosition()));
    }
}
