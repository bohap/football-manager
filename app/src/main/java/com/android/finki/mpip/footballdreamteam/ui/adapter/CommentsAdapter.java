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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Borce on 05.09.2016.
 */
public class CommentsAdapter extends BaseAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CommentsAdapter.class);
    private Context context;
    private List<Comment> comments;
    private Map<Integer, Item> items;
    private User user;
    private Listener listener;

    public CommentsAdapter(Context context, List<Comment> comments, User user, Listener listener) {
        this.context = context;
        this.comments = comments;
        this.user = user;
        this.listener = listener;
        items = new HashMap<>();
        for (Comment comment : comments) {
            items.put(comment.getId(), new Item());
        }
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
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.comments_list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.setPosition(i);
        return view;
    }

    /**
     * Add a new comment in the adapter.
     *
     * @param comment new comment
     */
    public void add(Comment comment) {
        logger.info(String.format("add comment, id %d", comment.getId()));
        comments.add(0, comment);
        items.put(comment.getId(), new Item());
        super.notifyDataSetChanged();
    }

    /**
     * Called when updating the comment value is successful.
     *
     * @param comment comment that was updating
     * @param newComment new comment value
     */
    public void onUpdateSuccess(Comment comment, Comment newComment) {
        logger.info(String.format("onUpdateSuccess comment, id %d", comment.getId()));
        comments.set(comments.indexOf(comment), newComment);
        items.get(comment.getId()).setEditing(false);
        items.get(comment.getId()).setSending(false);
        super.notifyDataSetChanged();
    }

    /**
     * Called when updating the comment failed.
     *
     * @param comment comment that was updating
     */
    public void onUpdateFailed(Comment comment) {
        logger.info(String.format("onUpdateFailed comment, id %d", comment.getId()));
        items.get(comment.getId()).setEditing(true);
        items.get(comment.getId()).setSending(false);
        super.notifyDataSetChanged();
    }

    /**
     * Called when removing the comment is successful .
     *
     * @param comment comment that was removing
     */
    public void onRemoveSuccess(Comment comment) {
        logger.info(String.format("onRemoveSuccess comment, id %d", comment.getId()));
        comments.remove(comment);
        items.remove(comment.getId());
        super.notifyDataSetChanged();
    }

    /**
     * Called when removing the comment failed.
     *
     * @param comment comment that was removing
     */
    public void onRemoveFailed(Comment comment) {
        logger.info(String.format("onRemoveFailed comment, id %d", comment.getId()));
        items.get(comment.getId()).setEditing(false);
        items.get(comment.getId()).setSending(false);
        super.notifyDataSetChanged();
    }

    /**
     * ViewHolder class for the adapter.
     */
    public class ViewHolder {

        private int position;
        private Comment comment;

        @BindView(R.id.commentListItem_content)
        RelativeLayout content;

        @BindView(R.id.commentListItem_spinner)
        LinearLayout spinner;

        @BindView(R.id.commentListItem_user)
        TextView txtUser;

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

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        /**
         * Set the holder position in the list.
         *
         * @param position holder position
         */
        public void setPosition(int position) {
            this.position = position;
            this.comment = comments.get(position);
            if (comment.getUser() != null) {
                this.txtUser.setText(comment.getUser().getName());
            }
            this.body.setText(comment.getBody());
            String editedBody = items.get(comment.getId()).getEditedBody();
            this.txtBody.setText(editedBody != null ? editedBody : comment.getBody());
            this.showEditing(items.get(comment.getId()).isEditing());
            this.showRequestSending(items.get(comment.getId()).isSending());
        }

        /**
         * Show the views when the comment is editing.
         *
         * @param editing whatever the comment is editing or not
         */
        private void showEditing(boolean editing) {
            int commentUserId = comment.getUserId();
            if (commentUserId == 0 && comment.getUser() != null) {
                commentUserId = comment.getUser().getId();
            }
            boolean canEdit = commentUserId == user.getId();
            body.setVisibility(editing ? View.GONE : View.VISIBLE);
            txtBody.setVisibility(editing ? View.VISIBLE : View.GONE);
            boolean equalBody = txtBody.getText().toString().equals(comment.getBody());
            btnUpdate.setVisibility(editing && !equalBody ? View.VISIBLE : View.GONE);
            btnUpdateCancel.setVisibility(editing ? View.VISIBLE : View.GONE);
            btnEdit.setVisibility(editing ? View.GONE : View.VISIBLE);
            btnRemove.setVisibility(editing ? View.GONE : View.VISIBLE);
            if (!canEdit) {
                btnEdit.setVisibility(View.GONE);
                btnRemove.setVisibility(View.GONE);
            }
            if (!editing) {
                ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(txtBody.getWindowToken(), 0);
            }
        }

        /**
         * Show the views when a request is sending for the comment.
         *
         * @param sending whatever the request is sending for the comment or not
         */
        private void showRequestSending(boolean sending) {
            spinner.setVisibility(sending ? View.VISIBLE : View.GONE);
            content.setVisibility(sending ? View.GONE : View.VISIBLE);
            ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(txtBody.getWindowToken(), 0);
        }

        /**
         * Handle click on the comment body.
         */
        @OnClick(R.id.commentListItem_body)
        void onBodyClick() {
            logger.info(String.format("onBodyClick, position %d", position));
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
            logger.info(String.format("onBtnEditClick, position %d", position));
            items.get(comment.getId()).setEditing(true);
            this.showEditing(true);
        }

        /**
         * Handle changing the content on the body edit text.
         */
        @OnTextChanged(R.id.commentListItem_txtBody)
        void onTxtBodyChanged() {
            String content = txtBody.getText().toString();
            if (content.equals(comment.getBody())) {
                btnUpdate.setVisibility(View.GONE);
                items.get(comment.getId()).setEditedBody(null);
            } else {
                btnUpdate.setVisibility(View.VISIBLE);
                items.get(comment.getId()).setEditedBody(content);
            }
        }

        /**
         * Handle click on the button for canceling onUpdateSuccess.
         */
        @OnClick(R.id.commentListItem_btnCancelUpdate)
        void onBtnCancelUpdateClick() {
            logger.info(String.format("onBtnCancelUpdateClick, position %d", position));
            items.get(comment.getId()).setEditing(false);
            this.showEditing(false);
        }

        /**
         * Handle click on button for updating the comment.
         */
        @OnClick(R.id.commentListItem_btnUpdate)
        void onBtnUpdateClick() {
            logger.info(String.format("onBtnUpdateClick, position %d", position));
            items.get(comment.getId()).setEditing(false);
            items.get(comment.getId()).setSending(true);
            listener.updateComment(position, txtBody.getText().toString());
            this.showEditing(false);
            this.showRequestSending(true);
        }

        /**
         * Handle click on the button for removing the comment.
         */
        @OnClick(R.id.commentListItem_btnRemove)
        void onBtnRemoveClick() {
            logger.info(String.format("onBtnRemoveClick, positions %d", position));
            items.get(comment.getId()).setSending(true);
            listener.deleteComment(position);
            this.showRequestSending(true);
        }
    }

    /**
     * Wrapper class for each comment holding information about if the comment
     * is editing, updating or none at the moment.
     */
    private class Item {

        boolean editing = false;
        boolean sending = false;
        private String editedBody = null;

        public void setEditing(boolean editing) {
            this.editing = editing;
        }

        public boolean isEditing() {
            return editing;
        }

        public void setSending(boolean sending) {
            this.sending = sending;
        }

        public boolean isSending() {
            return sending;
        }

        public void setEditedBody(String editedBody) {
            this.editedBody = editedBody;
        }

        public String getEditedBody() {
            return editedBody;
        }
    }

    /**
     * Listener class used for communication with the view using the adapter.
     */
    public interface Listener {

        void updateComment(int position, String newBOdy);

        void deleteComment(int position);
    }
}
