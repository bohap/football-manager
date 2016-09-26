package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CommentsViewModule;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.ui.adapter.CommentsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.component.CommentsView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CommentsViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;
import com.android.finki.mpip.footballdreamteam.utility.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Created by Borce on 15.08.2016.
 */
public class CommentsFragment extends BaseFragment implements CommentsView, CommentsAdapter.Listener {

    private static Logger logger = LoggerFactory.getLogger(CommentsFragment.class);
    private CommentsViewPresenter presenter;
    private Unbinder unbinder;

    @BindString(R.string.commentsLayout_title)
    String title;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindString(R.string.commentsLayout_spinnerText)
    String spinnerText;

    @BindView(R.id.error)
    RelativeLayout error;

    @BindView(R.id.txtError)
    TextView txtError;

    @BindString(R.string.commentsLayout_loadingCommentsFailed_text)
    String loadingCommentsFailedText;

    @BindString(R.string.commentsLayout_addingCommentSuccess_text)
    String addingCommentSuccessText;

    @BindString(R.string.commentsLayout_addingCommentFailed_text)
    String addingCommentFailedText;

    @BindString(R.string.commentsLayout_updatingCommentSuccess_text)
    String updatingCommentSuccessText;

    @BindString(R.string.commentsLayout_updatingCommentFailed_text)
    String updatingCommentFailedText;

    @BindString(R.string.commentLayout_deletingCommentSuccess_text)
    String deletingCommentSuccessText;

    @BindString(R.string.commentLayout_deletingCommentFailed_text)
    String deletingCommentFailedText;

    @BindView(R.id.commentsLayout_mainContent)
    RelativeLayout content;

    @BindView(R.id.commentsLayout_listView)
    ListView listView;

    @BindView(R.id.commentsLayout_addCommentMainContent)
    RelativeLayout addCommentContent;

    @BindView(R.id.commentsLayout_btnAddComment)
    Button btnAddComment;

    @BindView(R.id.commentsLayout_txtComment)
    EditText txtComment;

    @BindView(R.id.commentsLayout_btnSubmitComment)
    ButtonAwesome btnSubmitComment;

    @BindView(R.id.commentsLayout_btnCancelAddingComment)
    ButtonAwesome btnCancelAddingComment;

    @BindView(R.id.commentsLayout_spinnerSubmittingComment)
    LinearLayout spinnerSubmittingComment;

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
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).onFragmentActive();
        }
        ((MainApplication) this.getActivity().getApplication()).getAuthComponent()
                .plus(new CommentsViewModule(this)).inject(this);
        presenter.onViewCreated(this.getArguments());
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
        logger.info("onCreateView");
        View view = inflater.inflate(R.layout.comment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.onViewLayoutCreated();
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).changeTitle(title);
        }
        adapter = new CommentsAdapter(this.getActivity(), presenter.getComments(),
                presenter.getUser(), this);
        listView.setAdapter(adapter);
        return view;
    }

    /**
     * Called before the fragment view is destroyed.
     */
    @Override
    public void onDestroyView() {
        logger.info("onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
        presenter.onViewLayoutDestroyed();
    }

    /**
     * Called before the fragment is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroyed();
    }

    /**
     * Called when the comments data has been started loading form the server.
     */
    @Override
    public void showCommentsLoading() {
        logger.info("showCommentsLoading");
        txtSpinner.setText(spinnerText);
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    /**
     * Called when loading the comments is successful.
     *
     * @param comments all lineup comments
     */
    @Override
    public void showCommentsLoadingSuccess(List<Comment> comments) {
        logger.info("showCommentsLoadingSuccess");
        spinner.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        adapter.update(comments);
    }

    /**
     * Called when a error occurred while loading comments data.
     */
    @Override
    public void showCommentLoadingFailed() {
        logger.info("showCommentsLoadingFailed");
        txtError.setText(loadingCommentsFailedText);
        spinner.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    /**
     * Handle click on the button "Try Again".
     */
    @OnClick(R.id.error_btnTryAgain)
    void onBtnTryAgainClick() {
        logger.info("btn 'Try Again' clicked");
        presenter.loadComments();
    }

    /**
     * Handle click on the button 'Add Comment'.
     */
    @OnClick(R.id.commentsLayout_btnAddComment)
    void onBtnAddCommentClick() {
        logger.info("btn 'Add Comment' clicked");
        btnAddComment.setVisibility(View.GONE);
        addCommentContent.setVisibility(View.VISIBLE);
        txtComment.setText(null);
        txtComment.setEnabled(true);
        btnCancelAddingComment.setVisibility(View.VISIBLE);
        btnSubmitComment.setVisibility(View.GONE);
        spinnerSubmittingComment.setVisibility(View.GONE);
    }

    /**
     * Handle text changing on the edit text for adding comment.
     */
    @OnTextChanged(R.id.commentsLayout_txtComment)
    void onTxtAddCommentChanged() {
        if (!StringUtils.isEmpty(txtComment.getText().toString())) {
            btnSubmitComment.setVisibility(View.VISIBLE);
        } else {
            btnSubmitComment.setVisibility(View.GONE);
        }
    }

    /**
     * Handle click on the button for canceling adding the comment.
     */
    @OnClick(R.id.commentsLayout_btnCancelAddingComment)
    void onBtnCancelAddingCommentClick() {
        logger.info("btn 'Cancel Adding Comment' clicked");
        addCommentContent.setVisibility(View.GONE);
        btnAddComment.setVisibility(View.VISIBLE);
    }

    /**
     * Handle click on the button to submit the comment.
     */
    @OnClick(R.id.commentsLayout_btnSubmitComment)
    void onBtnSubmitCommentClick() {
        logger.info("btn 'Submit Commnet' clicked");
        txtComment.setEnabled(false);
        btnSubmitComment.setVisibility(View.GONE);
        btnCancelAddingComment.setVisibility(View.GONE);
        spinnerSubmittingComment.setVisibility(View.VISIBLE);
        presenter.addComment(txtComment.getText().toString());
    }

    /**
     * Called when adding the comment is successful.
     *
     * @param comment new added comment
     */
    @Override
    public void showCommentAddingSuccess(Comment comment) {
        logger.info("showCommentAddingSuccess");
        Toast.makeText(this.getActivity(), addingCommentSuccessText, Toast.LENGTH_SHORT).show();
        btnAddComment.setVisibility(View.VISIBLE);
        addCommentContent.setVisibility(View.GONE);
        adapter.add(comment);
    }

    /**
     * Called when adding the comment failed.
     */
    @Override
    public void showCommentAddingFailed() {
        logger.info("showCommentAddingFailed");
        Toast.makeText(this.getActivity(), addingCommentFailedText, Toast.LENGTH_SHORT).show();
        spinnerSubmittingComment.setVisibility(View.GONE);
        btnSubmitComment.setVisibility(View.VISIBLE);
        btnCancelAddingComment.setVisibility(View.VISIBLE);
        txtComment.setEnabled(true);
    }

    /**
     * Called when the onUpdateSuccess button has been clicked on the comment at the given position
     * .
     *
     * @param position comment position in the adapter
     * @param newBody comment new body
     */
    @Override
    public void updateComment(int position, String newBody) {
        logger.info("updateComment");
        Comment comment = adapter.getItem(position);
        presenter.updateComment(comment, newBody);
    }

    /**
     * Called when updating the comment is successful.
     *
     * @param comment comment that was updating
     * @param newComment new comment value
     */
    @Override
    public void showCommentUpdatingSuccess(Comment comment, Comment newComment) {
        logger.info("showRequestSendingDone");
        Toast.makeText(this.getActivity(), updatingCommentSuccessText, Toast.LENGTH_SHORT).show();
        adapter.onUpdateSuccess(comment, newComment);
    }

    /**
     * Called when updating the comment failed.
     */
    @Override
    public void showCommentUpdatingFailed(Comment comment) {
        logger.info("showCommentUpdatingFailed");
        Toast.makeText(this.getActivity(), updatingCommentFailedText, Toast.LENGTH_SHORT).show();
        adapter.onUpdateFailed(comment);
    }

    /**
     * Called when the onRemoveSuccess button has been clicked on the comment at the given position.
     */
    @Override
    public void deleteComment(int position) {
        logger.info("deleteComment");
        presenter.deleteComment(adapter.getItem(position));
    }

    /**
     * Called when deleting the comment is successful.
     *
     * @param comment deleted comment
     */
    @Override
    public void showCommentDeletingSuccess(Comment comment) {
        logger.info("showCommentDeletingSuccess");
        Toast.makeText(this.getActivity(), deletingCommentSuccessText, Toast.LENGTH_SHORT).show();
        adapter.onRemoveSuccess(comment);
    }

    /**
     * Called when deleting the comment failed.
     *
     * @param comment failed deleted comment
     */
    @Override
    public void showCommentDeletingFailed(Comment comment) {
        logger.info("showCommentDeletingFailed");
        Toast.makeText(this.getActivity(), deletingCommentFailedText, Toast.LENGTH_SHORT).show();
        adapter.onRemoveFailed(comment);
    }
}
