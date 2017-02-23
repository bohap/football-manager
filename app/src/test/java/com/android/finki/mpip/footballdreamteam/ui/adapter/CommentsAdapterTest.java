package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowBaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 18.09.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class CommentsAdapterTest {

    @Mock
    private CommentsAdapter.Listener listener;

    private Context context;
    private CommentsAdapter adapter;
    private ShadowBaseAdapter shadow;

    private Calendar calendar = new GregorianCalendar(2016, 8, 18, 21, 52, 40);
    private Date date = calendar.getTime();
    private final Lineup lineup = new Lineup(1, 1);
    private User authUser = new User(1, "AuthUser");
    private User user1 = new User(2, "User 2");
    private User user2 = new User(3, "User 3");
    private User user3 = new User(4, "User 4");
    private final int NUMBER_OF_COMMENTS = 6;
    private Comment authUserComment1 = new Comment(1, authUser, lineup, "Comment 1", date, date);
    private Comment comment1 = new Comment(2, user1, lineup, "Comment 2", date, date);
    private Comment comment2 = new Comment(3, user2, lineup, "Comment 3", date, date);
    private Comment authUserComment2 = new Comment(4, authUser, lineup, "Comment 4", date, date);
    private Comment comment3 = new Comment(5, user3, lineup, "Comment 5", date, date);
    private Comment comment4 = new Comment(6, user2, lineup, "Comment 6", date, date);
    private Comment unExistingComment =
            new Comment(7, user1, lineup, "Un Existing Comment", date, date);
    private List<Comment> comments = Arrays.asList(authUserComment1, comment1, comment2,
            authUserComment2, comment3, comment4);
    private RelativeLayout mainContent;
    private LinearLayout spinner;
    private TextView txtBody;
    private EditText edBody;
    private ButtonAwesome btnEdit;
    private ButtonAwesome btnRemove;
    private ButtonAwesome btnUpdate;
    private ButtonAwesome btnCancelUpdate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        context = RuntimeEnvironment.application.getApplicationContext();
        adapter = new CommentsAdapter(context, new ArrayList<>(comments), authUser, listener);
        shadow = shadowOf(adapter);
    }

    /**
     * Test the getCount returns the number of items in the adapter.
     */
    @Test
    public void testGetCount() {
        assertEquals(NUMBER_OF_COMMENTS, adapter.getCount());
    }

    /**
     * Test the behavior on getItem called with un existing position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetItemOnUnExistingPosition() {
        adapter.getItem(NUMBER_OF_COMMENTS);
    }

    /**
     * Test the getItem return the item in the adapter at the given position.
     */
    @Test
    public void testGetItem() {
        int index = NUMBER_OF_COMMENTS - 1;
        Comment comment = adapter.getItem(index);
        assertSame(comments.get(index), comment);
    }

    /**
     * Test that all items in the adapter are correctly set.
     */
    @Test
    public void testGetItems() {
        assertEquals(comments.size(), adapter.getCount());
        for (int i = 0; i < comments.size(); i++) {
            assertSame(comments.get(i), adapter.getItem(i));
            assertFalse(adapter.isSending(comments.get(i)));
            assertFalse(adapter.isEditing(comments.get(i)));
        }
    }

    /**
     * Test the behavior when isSending is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsSendingCalledWithNullParam() {
        adapter.isSending(null);
    }

    /**
     * Test the behavior when isSending is called with comment that is not in the adapter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsSendingCalledWithUnExistingComment() {
        adapter.isSending(unExistingComment);
    }

    /**
     * Test the behavior when isSending is called with comment that is in the adapter.
     */
    @Test
    public void testIsSendingCalledWithExistingComment() {
        assertFalse(adapter.isSending(comment1));
    }

    /**
     * Test the behavior when isEditing is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsEditingCalledWithNullParam() {
        adapter.isEditing(null);
    }

    /**
     * Test the behavior when isEditing is called with comment that is not in the adapter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsEditingCalledWithUnExistingComment() {
        adapter.isEditing(unExistingComment);
    }

    /**
     * Test the behavior when isEditing is called with comment that is in the adapter.
     */
    @Test
    public void testIsEditingCalledWithExistingComment() {
        assertFalse(adapter.isEditing(comment1));
    }

    /**
     * Get all main children views from the view.
     *
     * @param view view containing the children
     */
    private void getViews(View view) {
        assertNotNull(view);
        mainContent = (RelativeLayout) view.findViewById(R.id.commentListItem_content);
        assertNotNull(mainContent);
        spinner = (LinearLayout) view.findViewById(R.id.commentListItem_spinner);
        assertNotNull(spinner);
        txtBody = (TextView) view.findViewById(R.id.commentListItem_body);
        assertNotNull(txtBody);
        edBody = (EditText) view.findViewById(R.id.commentListItem_txtBody);
        assertNotNull(edBody);
        btnEdit = (ButtonAwesome) view.findViewById(R.id.commentListItem_btnEdit);
        assertNotNull(btnEdit);
        btnRemove = (ButtonAwesome) view.findViewById(R.id.commentListItem_btnRemove);
        assertNotNull(btnRemove);
        btnUpdate = (ButtonAwesome) view.findViewById(R.id.commentListItem_btnUpdate);
        assertNotNull(btnUpdate);
        btnCancelUpdate = (ButtonAwesome) view.findViewById(R.id.commentListItem_btnCancelUpdate);
        assertNotNull(btnCancelUpdate);
    }

    /**
     * Test the getView correctly sets the views for the comment at the given position when the
     * comment is not editing and a request is not sending for him.
     */
    @Test
    public void testGetView() {
        final Comment comment = comment1;
        View view = adapter.getView(comments.indexOf(comment), null, null);
        this.getViews(view);
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        TextView txtUser = (TextView) view.findViewById(R.id.commentListItem_user);
        assertNotNull(txtUser);
        assertEquals(comment.getUser().getName(), txtUser.getText());
        assertEquals(comment.getBody(), txtBody.getText());
        assertEquals(comment.getBody(), edBody.getText().toString());
        assertEquals(View.GONE, edBody.getVisibility());
        assertEquals(View.GONE, btnEdit.getVisibility());
        assertEquals(View.GONE, btnRemove.getVisibility());
        assertEquals(View.GONE, btnUpdate.getVisibility());
        assertEquals(View.GONE, btnCancelUpdate.getVisibility());
    }

    /**
     * Test the behavior when getView is called with a comment on which the user is null.
     */
    @Test
    public void testGetViewOnNullCommentUser() {
        comment2.setUser(null);
        View view = adapter.getView(comments.indexOf(comment2), null, null);
        TextView txtUser = (TextView) view.findViewById(R.id.commentListItem_user);
        assertNotNull(txtUser);
        assertEquals("", txtUser.getText());
    }

    /**
     * Test the getView correctly sets the views when the comment is created by the
     * authenticated user.
     */
    @Test
    public void testGetViewOnCommentCreatedByAuthUser() {
        View view = adapter.getView(comments.indexOf(authUserComment1), null, null);
        this.getViews(view);
        assertEquals(View.VISIBLE, btnEdit.getVisibility());
        assertEquals(View.VISIBLE, btnRemove.getVisibility());
        assertEquals(View.GONE, btnUpdate.getVisibility());
        assertEquals(View.GONE, btnCancelUpdate.getVisibility());
    }

    /**
     * Test that getView will not create a new view when the passed to the method is not null.
     */
    @Test
    public void testGetViewOnRecycledView() {
        View view = View.inflate(context, R.layout.comments_list_item, null);
        CommentsAdapter.ViewHolder holder = adapter.new ViewHolder(view);
        view.setTag(holder);
        View recycled = adapter.getView(0, view, null);
        assertSame(view, recycled);
    }

    /**
     * Test that getVIew will correctly set the views for the comment when he is editing.
     */
    @Test
    public void testGetViewWhenCommentIsEditing() {
        final Comment comment = authUserComment1;
        final int index = comments.indexOf(comment);
        View view = adapter.getView(index, null, null);
        this.getViews(view);
        assertFalse(adapter.isEditing(comment));
        assertTrue(btnEdit.performClick());
        assertTrue(adapter.isEditing(comment));
        view = adapter.getView(index, null, null);
        this.getViews(view);
        assertEquals(View.GONE, txtBody.getVisibility());
        assertEquals(View.VISIBLE, edBody.getVisibility());
        assertEquals(View.GONE, btnEdit.getVisibility());
        assertEquals(View.GONE, btnRemove.getVisibility());
        assertEquals(View.GONE, btnUpdate.getVisibility());
        assertEquals(View.VISIBLE, btnCancelUpdate.getVisibility());
        assertEquals(comment.getBody(), edBody.getText().toString());
    }

    /**
     * Test that getVIew will correctly set the views for the comment when he is editing and the
     * EditText body content is different then comment body.
     */
    @Test
    public void testGetViewWhenCommentIsEditingEditTextContentIsDifferentThenCommentBody() {
        final Comment comment = authUserComment1;
        final int index = comments.indexOf(comment);
        final String newBody = "New Test Body";
        View view = adapter.getView(index, null, null);
        this.getViews(view);
        assertFalse(adapter.isEditing(comment));
        assertTrue(btnEdit.performClick());
        assertTrue(adapter.isEditing(comment));
        edBody.setText(newBody);
        view = adapter.getView(index, null, null);
        this.getViews(view);
        assertEquals(View.GONE, txtBody.getVisibility());
        assertEquals(View.VISIBLE, edBody.getVisibility());
        assertEquals(View.GONE, btnEdit.getVisibility());
        assertEquals(View.GONE, btnRemove.getVisibility());
        assertEquals(View.VISIBLE, btnUpdate.getVisibility());
        assertEquals(View.VISIBLE, btnCancelUpdate.getVisibility());
        assertEquals(newBody, edBody.getText().toString());
    }

    /**
     * Test that getView will correctly set the view for the comment when a request is sending
     * for him.
     */
    @Test
    public void testGetViewWhenRequestForTheCommentIsSending() {
        final Comment comment = authUserComment2;
        final int index = comments.indexOf(comment);
        View view = adapter.getView(index, null, null);
        this.getViews(view);
        assertFalse(adapter.isSending(comment));
        assertTrue(btnUpdate.performClick());
        assertTrue(adapter.isSending(comment));
        view = adapter.getView(index, null, null);
        this.getViews(view);
        assertEquals(View.GONE, mainContent.getVisibility());
        assertEquals(View.VISIBLE, spinner.getVisibility());
    }

    /**
     * Test the behavior when button 'Edit' is clicked for the item.
     */
    @Test
    public void testBtnEditClick() {
        final Comment comment = authUserComment2;
        View view = adapter.getView(comments.indexOf(comment), null, null);
        this.getViews(view);
        assertFalse(adapter.isEditing(comment));
        assertTrue(btnEdit.performClick());
        assertTrue(adapter.isEditing(comment));
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, txtBody.getVisibility());
        assertEquals(View.VISIBLE, edBody.getVisibility());
        assertEquals(comment.getBody(), edBody.getText().toString());
        assertEquals(View.GONE, btnEdit.getVisibility());
        assertEquals(View.GONE, btnRemove.getVisibility());
        assertEquals(View.GONE, btnUpdate.getVisibility());
        assertEquals(View.VISIBLE, btnCancelUpdate.getVisibility());
    }

    /**
     * Test the behavior when the edit text field content for the comment has changed.
     */
    @Test
    public void testTxtBodyChanged() {
        String newBody = "New comment Body";
        View view = adapter.getView(comments.indexOf(authUserComment1), null, null);
        this.getViews(view);
        assertTrue(btnEdit.performClick());
        assertEquals(View.GONE, btnUpdate.getVisibility());
        edBody.setText(newBody);
        assertEquals(View.VISIBLE, btnUpdate.getVisibility());
    }

    /**
     * Test the behavior when button 'Cancel Update' is clicked.
     */
    @Test
    public void testBtnCancelUpdateClick() {
        final Comment comment = authUserComment2;
        View view = adapter.getView(comments.indexOf(comment), null, null);
        this.getViews(view);
        assertTrue(btnEdit.performClick());
        assertTrue(adapter.isEditing(comment));
        assertTrue(btnCancelUpdate.performClick());
        assertFalse(adapter.isEditing(comment));
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.VISIBLE, txtBody.getVisibility());
        assertEquals(View.GONE, edBody.getVisibility());
        assertEquals(View.VISIBLE, btnEdit.getVisibility());
        assertEquals(View.VISIBLE, btnRemove.getVisibility());
        assertEquals(View.GONE, btnUpdate.getVisibility());
        assertEquals(View.GONE, btnCancelUpdate.getVisibility());
    }

    /**
     * Test the behavior when button 'Update' is clicked for the comment.
     */
    @Test
    public void testBtnUpdateClick() {
        final Comment comment = authUserComment1;
        int index = comments.indexOf(comment);
        String newBody = "Comment updated";
        View view = adapter.getView(index, null, null);
        this.getViews(view);
        edBody.setText(newBody);
        assertTrue(btnEdit.performClick());
        assertTrue(adapter.isEditing(comment));
        assertFalse(adapter.isSending(comment));
        assertTrue(btnUpdate.performClick());
        assertFalse(adapter.isEditing(comment));
        assertTrue(adapter.isSending(comment));
        verify(listener).updateComment(index, newBody);
        assertEquals(View.GONE, mainContent.getVisibility());
        assertEquals(View.VISIBLE, spinner.getVisibility());
    }

    /**
     * Test the behavior when button 'Remove' is clicked for the comment.
     */
    @Test
    public void testBtnRemoveClick() {
        final Comment comment = authUserComment2;
        int index = comments.indexOf(comment);
        View view = adapter.getView(index, null, null);
        this.getViews(view);
        assertFalse(adapter.isSending(comment));
        assertTrue(btnRemove.performClick());
        assertTrue(adapter.isSending(comment));
        verify(listener).deleteComment(index);
        assertEquals(View.GONE, mainContent.getVisibility());
        assertEquals(View.VISIBLE, spinner.getVisibility());
    }

    /**
     * Test that add method will add a new item in the adapter.
     */
    @Test
    public void testAdd() {
        adapter.add(unExistingComment);
        assertEquals(NUMBER_OF_COMMENTS + 1, adapter.getCount());
        assertEquals(unExistingComment, adapter.getItem(0));
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        assertNotNull(adapter.getView(0, null, null));
        assertFalse(adapter.isSending(unExistingComment));
        assertFalse(adapter.isEditing(unExistingComment));
    }

    /**
     * Test the behavior when onUpdateSuccess is called.
     */
    @Test
    public void testOnUpdateSuccess() {
        final Comment comment = authUserComment1;
        Comment newComment = new Comment(comment.getId(), comment.getUser(), comment.getLineup(),
                "New Comment Body", date, date);
        int index = comments.indexOf(comment);
        View view = adapter.getView(index, null, null);
        this.getViews(view);
        assertFalse(adapter.isSending(comment));
        assertTrue(btnUpdate.performClick());
        assertTrue(adapter.isSending(comment));
        adapter.onUpdateSuccess(comment, newComment);
        assertEquals(NUMBER_OF_COMMENTS, adapter.getCount());
        assertSame(newComment, adapter.getItem(index));
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        assertFalse(adapter.isEditing(comment));
        assertFalse(adapter.isSending(comment));
    }

    /**
     * Test the behavior when onUpdateFailed is called.
     */
    @Test
    public void testOnUpdateFailed() {
        final Comment comment = authUserComment2;
        String newBody = "Edited Body";
        int index = comments.indexOf(comment);
        View view = adapter.getView(index, null, null);
        this.getViews(view);
        edBody.setText(newBody);
        assertTrue(btnEdit.performClick());
        assertTrue(adapter.isEditing(comment));
        assertFalse(adapter.isSending(comment));
        assertTrue(btnUpdate.performClick());
        assertTrue(adapter.isSending(comment));
        adapter.onUpdateFailed(comment);
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        assertTrue(adapter.isEditing(comment));
        assertFalse(adapter.isSending(comment));
        view = adapter.getView(index, null, null);
        this.getViews(view);
        assertEquals(newBody, edBody.getText().toString());
    }

    /**
     * Test the behavior when onRemoveSuccess is called.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnRemoveSuccess() {
        int index = 2;
        List<Comment> comments = new ArrayList<>(this.comments);
        final Comment comment = comments.get(index);
        adapter.onRemoveSuccess(comment);
        assertEquals(NUMBER_OF_COMMENTS - 1, adapter.getCount());
        assertSame(comments.get(index + 1), adapter.getItem(index));
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        adapter.isSending(comment);
    }

    /**
     * Test the behavior when onRemoveFailed is called.
     */
    @Test
    public void testOnRemoveFailed() {
        final Comment comment = authUserComment2;
        View view = adapter.getView(comments.indexOf(comment), null, null);
        this.getViews(view);
        assertTrue(btnRemove.performClick());
        assertTrue(adapter.isSending(comment));
        adapter.onRemoveFailed(comment);
        assertEquals(NUMBER_OF_COMMENTS, adapter.getCount());
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        assertFalse(adapter.isSending(comment));
        assertFalse(adapter.isEditing(comment));
    }
}
