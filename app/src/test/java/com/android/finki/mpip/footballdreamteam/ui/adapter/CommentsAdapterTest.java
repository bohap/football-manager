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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
    private List<Comment> comments = new ArrayList<>();
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
        comments.add(authUserComment1);
        comments.add(comment1);
        comments.add(comment2);
        comments.add(authUserComment2);
        comments.add(comment3);
        comments.add(comment4);
        context = RuntimeEnvironment.application.getApplicationContext();
        adapter = new CommentsAdapter(context, comments, authUser, listener);
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
     * Get all main children views from the view.
     *
     * @param view view containing the children
     */
    private void initViews(View view) {
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
     * Test the getView correctly sets the views for the comment at the given position.
     */
    @Test
    public void testGetView() {
        final Comment comment = comment1;
        View view = adapter.getView(comments.indexOf(comment), null, null);
        this.initViews(view);
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
        this.initViews(view);
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
     * Test the behavior when button 'Edit' is clicked for the item.
     */
    @Test
    public void testBtnEditClick() {
        View view = adapter.getView(comments.indexOf(authUserComment2), null, null);
        this.initViews(view);
        assertTrue(btnEdit.performClick());
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                view = adapter.getView(comments.indexOf(authUserComment2), null, null);
                this.initViews(view);
            }
            assertEquals(View.VISIBLE, mainContent.getVisibility());
            assertEquals(View.GONE, spinner.getVisibility());
            assertEquals(View.GONE, txtBody.getVisibility());
            assertEquals(View.VISIBLE, edBody.getVisibility());
            assertEquals(authUserComment2.getBody(), edBody.getText().toString());
            assertEquals(View.GONE, btnEdit.getVisibility());
            assertEquals(View.GONE, btnRemove.getVisibility());
            assertEquals(View.GONE, btnUpdate.getVisibility());
            assertEquals(View.VISIBLE, btnCancelUpdate.getVisibility());
        }
    }

    /**
     * Test the behavior when the edit text field content for the comment has changed.
     */
    @Test
    public void testTxtBodyChanged() {
        String newBody = "New comment txtBody";
        View view = adapter.getView(comments.indexOf(authUserComment1), null, null);
        this.initViews(view);
        assertTrue(btnEdit.performClick());
        edBody.setText(newBody);
        assertEquals(View.VISIBLE, btnUpdate.getVisibility());
        view = adapter.getView(comments.indexOf(authUserComment1), null, null);
        this.initViews(view);
        assertEquals(View.GONE, txtBody.getVisibility());
        assertEquals(View.VISIBLE, edBody.getVisibility());
        assertEquals(newBody, edBody.getText().toString());
        assertEquals(View.VISIBLE, btnUpdate.getVisibility());
        assertEquals(View.VISIBLE, btnCancelUpdate.getVisibility());
        assertEquals(View.GONE, btnEdit.getVisibility());
        assertEquals(View.GONE, btnRemove.getVisibility());
    }

    /**
     * Test the behavior when button 'Cancel Update' is clicked.
     */
    @Test
    public void testBtnCancelUpdateClick() {
        View view = adapter.getView(comments.indexOf(authUserComment2), null, null);
        this.initViews(view);
        assertTrue(btnEdit.performClick());
        assertTrue(btnCancelUpdate.performClick());
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                view = adapter.getView(comments.indexOf(authUserComment2), null, null);
                this.initViews(view);
            }
            assertEquals(View.VISIBLE, mainContent.getVisibility());
            assertEquals(View.GONE, spinner.getVisibility());
            assertEquals(View.VISIBLE, txtBody.getVisibility());
            assertEquals(View.GONE, edBody.getVisibility());
            assertEquals(View.VISIBLE, btnEdit.getVisibility());
            assertEquals(View.VISIBLE, btnRemove.getVisibility());
            assertEquals(View.GONE, btnUpdate.getVisibility());
            assertEquals(View.GONE, btnCancelUpdate.getVisibility());
        }
    }

    /**
     * Test that when editing is canceled the edit text txtBody field new content is remembered.
     */
    @Test
    public void testEditTextBodyNewContentIsRememberedOnCancel() {
        String newBody = "New comment txtBody";
        View view = adapter.getView(comments.indexOf(authUserComment2), null, null);
        this.initViews(view);
        assertTrue(btnEdit.performClick());
        edBody.setText(newBody);
        assertTrue(btnCancelUpdate.performClick());
        view = adapter.getView(comments.indexOf(authUserComment2), null, null);
        this.initViews(view);
        assertEquals(newBody, edBody.getText().toString());
    }

    /**
     * Test the behavior when button 'Update' is clicked for the comment.
     */
    @Test
    public void testBtnUpdateClick() {
        int index = comments.indexOf(authUserComment1);
        String newBody = "Comment updated";
        View view = adapter.getView(index, null, null);
        this.initViews(view);
        edBody.setText(newBody);
        assertTrue(btnUpdate.performClick());
        verify(listener).updateComment(index, newBody);
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                view = adapter.getView(index, null, null);
                this.initViews(view);
            }
            assertEquals(View.GONE, mainContent.getVisibility());
            assertEquals(View.VISIBLE, spinner.getVisibility());
        }
    }

    /**
     * Test the behavior when button 'Remove' is clicked for the comment.
     */
    @Test
    public void testBtnRemoveClick() {
        int index = comments.indexOf(authUserComment2);
        View view = adapter.getView(index, null, null);
        this.initViews(view);
        assertTrue(btnRemove.performClick());
        verify(listener).deleteComment(index);
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                view = adapter.getView(index, null, null);
                this.initViews(view);
            }
            assertEquals(View.GONE, mainContent.getVisibility());
            assertEquals(View.VISIBLE, spinner.getVisibility());
        }
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
    }

    /**
     * Test the behavior when onUpdateSuccess is called.
     */
    @Test
    public void testOnUpdateSuccess() {
        Comment newComment = new Comment(authUserComment1.getId(), authUserComment1.getUser(),
                authUserComment1.getLineup(), "New Comment BOdy", date, date);
        int index = comments.indexOf(authUserComment1);
        View view = adapter.getView(index, null, null);
        this.initViews(view);
        assertTrue(btnUpdate.performClick());
        adapter.onUpdateSuccess(authUserComment1, newComment);
        assertEquals(NUMBER_OF_COMMENTS, adapter.getCount());
        assertSame(newComment, adapter.getItem(index));
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        view = adapter.getView(index, null, null);
        this.initViews(view);
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.VISIBLE, txtBody.getVisibility());
        assertEquals(newComment.getBody(), txtBody.getText());
        assertEquals(View.GONE, edBody.getVisibility());
        assertEquals(newComment.getBody(), edBody.getText().toString());
        assertEquals(View.VISIBLE, btnEdit.getVisibility());
        assertEquals(View.VISIBLE, btnRemove.getVisibility());
        assertEquals(View.GONE, btnUpdate.getVisibility());
        assertEquals(View.GONE, btnCancelUpdate.getVisibility());
    }

    /**
     * Test the behavior when onUpdateFailed is called.
     */
    @Test
    public void testOnUpdateFailed() {
        String newBody = "Edited Body";
        int index = comments.indexOf(authUserComment2);
        View view = adapter.getView(index, null, null);
        this.initViews(view);
        edBody.setText(newBody);
        assertTrue(btnUpdate.performClick());
        adapter.onUpdateFailed(authUserComment2);
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        view = adapter.getView(index, null, null);
        this.initViews(view);
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, txtBody.getVisibility());
        assertEquals(View.VISIBLE, edBody.getVisibility());
        assertEquals(newBody, edBody.getText().toString());
        assertEquals(View.VISIBLE, btnUpdate.getVisibility());
        assertEquals(View.VISIBLE, btnCancelUpdate.getVisibility());
        assertEquals(View.GONE, btnEdit.getVisibility());
        assertEquals(View.GONE, btnRemove.getVisibility());
    }

    /**
     * Test the behavior when onRemoveSuccess is called.
     */
    @Test
    public void testOnRemoveSuccess() {
        int index = 2;
        List<Comment> comments = new ArrayList<>(this.comments);
        adapter.onRemoveSuccess(comments.get(index));
        assertEquals(NUMBER_OF_COMMENTS - 1, adapter.getCount());
        assertSame(comments.get(index + 1), adapter.getItem(index));
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
    }

    /**
     * Test the behavior when onRemoveFailed is called.
     */
    @Test
    public void testOnRemoveFailed() {
        View view = adapter.getView(comments.indexOf(authUserComment2), null, null);
        this.initViews(view);
        assertTrue(btnRemove.performClick());
        adapter.onRemoveFailed(authUserComment2);
        assertEquals(NUMBER_OF_COMMENTS, adapter.getCount());
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        view = adapter.getView(comments.indexOf(authUserComment2), null, null);
        this.initViews(view);
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
    }
}
