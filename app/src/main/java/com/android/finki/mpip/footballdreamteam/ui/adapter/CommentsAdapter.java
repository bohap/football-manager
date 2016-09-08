package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by Borce on 05.09.2016.
 */
public class CommentsAdapter extends BaseAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CommentsAdapter.class);
    private Context context;
    private List<Comment> comments;
    private User user;
    private Listener listener;
    private int selectedPosition = -1;

    public CommentsAdapter(Context context, List<Comment> comments, User user, Listener listener) {
        this.context = context;
        this.comments = comments;
        this.user = user;
        this.listener = listener;
    }

    /**
     * Get the number of items in the adapter.
     *
     * @return number of items in the adapter
     */
    @Override
    public int getCount() {
        return comments.size();
    }

    /**
     * Get the item in the adapter at the given position.
     *
     * @param position item position
     * @return item in the adapter at the given position
     */
    @Override
    public Comment getItem(int position) {
        if (position > comments.size() - 1) {
            throw new IllegalArgumentException(String
                    .format("invalid position, position %d, size %d", position, comments.size()));
        }
        return comments.get(position);
    }

    /**
     * Get the item id.
     *
     * @param id item id
     * @return item id
     */
    @Override
    public long getItemId(int id) {
        return id;
    }

    /**
     * Get the view for the item at the given position.
     *
     * @param i         item position
     * @param view      item view
     * @param viewGroup item parent container
     * @return item view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        logger.info("Get view CommentsAdapter");
        ViewHolder holder;
        boolean viewNull = false;
        if (view == null) {
            viewNull = true;
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.comments_list_item, viewGroup, false);
            holder = new ViewHolder(view, i);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Comment comment = comments.get(i);
        if (comment.getUser() != null) {
            holder.user.setText(comment.getUser().getName());
        }
        holder.body.setText(comment.getBody());
        holder.txtBody.setText(comment.getBody());
        int commentUserId = comment.getUserId();
        if (commentUserId == 0 && comment.getUser() != null) {
            commentUserId = comment.getUser().getId();
        }
        boolean canEdit = commentUserId == user.getId();
        /**
         * When the keyboard is showed android redraws the list view by calling on every
         * item getView method, so we check if the view is null and if that is true we
         * show the buttons.
         */
        if (canEdit && viewNull) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnRemove.setVisibility(View.VISIBLE);
        }
        return view;
    }

    /**
     * Get the position of the selected comment.
     *
     * @return position of the selected comment
     */
    public int getSelectedPosition() {
        if (selectedPosition == -1) {
            throw new IllegalArgumentException("selected position is not yet set");
        }
        return selectedPosition;
    }

    /**
     * Called when a request has been send to aether update or delete comment.
     *
     * @param view comment view
     */
    public void showCommentUpdating(View view) {
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            return;
        }
        ((ViewHolder) view.getTag()).onCommentUpdating();
    }

    /**
     * Called when the request to update or delete comment responded either successfully or not.
     *
     * @param view comment view
     */
    public void showCommentUpdatingDone(View view) {
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            return;
        }
        ((ViewHolder) view.getTag()).onCommentUpdatingDone();
    }

    /**
     * Remove the comment at the selected position.
     */
    public void removeComment() {
        comments.remove(selectedPosition);
        selectedPosition = -1;
        super.notifyDataSetChanged();
    }

    public void addComment() {
        //TODO
    }

    public class ViewHolder {

        private int position;

        @BindView(R.id.commentListItem_content)
        RelativeLayout content;

        @BindView(R.id.commentListItem_spinner)
        LinearLayout spinner;

        @BindView(R.id.commentListItem_user)
        TextView user;

        @BindView(R.id.commentListItem_body)
        TextView body;

        @BindView(R.id.commentListItem_txtBody)
        EditText txtBody;

        @BindView(R.id.commentListItem_btnEdit)
        Button btnEdit;

        @BindView(R.id.commentListItem_btnUpdate)
        Button btnUpdate;

        @BindView(R.id.commentListItem_btnCancelUpdate)
        Button btnUpdateCancel;

        @BindView(R.id.commentListItem_btnRemove)
        Button btnRemove;

        public ViewHolder(View view, int position) {
            ButterKnife.bind(this, view);
            this.position = position;
        }

        /**
         * Handle click on the comment body.
         */
        @OnClick(R.id.commentListItem_body)
        void onBodyClick() {
            if (body.getLineCount() > 2) {
                body.setMaxLines(2);
            } else {
                body.setMaxLines(Integer.MAX_VALUE);
            }
        }

        /**
         * Handle click on the button editing comment.
         */
        @OnClick(R.id.commentListItem_btnEdit)
        void onBtnEditClick() {
            CommentsAdapter.this.selectedPosition = position;
            body.setVisibility(View.GONE);
            txtBody.setVisibility(View.VISIBLE);
            txtBody.requestFocus();
            btnUpdate.setVisibility(View.VISIBLE);
            btnUpdateCancel.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        }

        /**
         * Called when EditText body focus has been changed.
         */
        @OnFocusChange(R.id.commentListItem_txtBody)
        public void onTxtBodyFocusChange() {
            btnUpdate.setVisibility(View.VISIBLE);
            btnUpdateCancel.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        }

        /**
         * Handle click on the button for canceling update.
         */
        @OnClick(R.id.commentListItem_btnCancelUpdate)
        void onBtnCancelUpdateClick() {
            CommentsAdapter.this.selectedPosition = -1;
            body.setVisibility(View.VISIBLE);
            txtBody.setVisibility(View.GONE);
            /* Hide the keyboard */
            InputMethodManager imm = (InputMethodManager)context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txtBody.getWindowToken(), 0);
            btnUpdate.setVisibility(View.GONE);
            btnUpdateCancel.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.VISIBLE);
        }

        /**
         * Handle click on button for updating the comment.
         */
        @OnClick(R.id.commentListItem_btnUpdate)
        void onBtnUpdateClick() {
            CommentsAdapter.this.listener.updateComment();
        }

        /**
         * Handle click on the button for removing the comment.
         */
        @OnClick(R.id.commentListItem_btnRemove)
        void onBtnRemoveClick() {
            CommentsAdapter.this.selectedPosition = position;
            CommentsAdapter.this.listener.deleteComment();
        }

        /**
         * Called when the comment is updating.
         */
        public void onCommentUpdating() {
            spinner.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        }

        /**
         * Called when the comment updating is done.
         */
        public void onCommentUpdatingDone() {
            CommentsAdapter.this.selectedPosition = -1;
            spinner.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            body.setVisibility(View.VISIBLE);
            txtBody.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
            btnUpdateCancel.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.VISIBLE);
        }
    }

    public interface Listener {

        void updateComment();

        void deleteComment();
    }
}
