package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CommentsViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.adapter.CommentsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.component.CommentsView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CommentsViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 25.09.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class CommentFragmentTest {

    @Mock
    private CommentsViewComponent component;

    @Mock
    private CommentsViewPresenter presenter;

    @Mock
    private static BaseFragment.Listener bfListener;

    private MockApplication application;
    private CommentsFragment fragment;
    private User user = new User(1, "User");
    private Lineup lineup = new Lineup(1, 1);
    private List<Comment> comments = Arrays.asList(
            new Comment(1, 1, 1, "Comment", null, null),
            new Comment(2, 2, 1, "Comment", null, null),
            new Comment(3, 1, 1, "Comment", null, null));
    private RelativeLayout spinner;
    private TextView txtSpinner;
    private RelativeLayout error;
    private TextView txtError;
    private RelativeLayout content;
    private ListView listView;
    private Button btnAddComment;
    private RelativeLayout addCommentContent;
    private EditText txtComment;
    private ButtonAwesome btnSubmitComment;
    private ButtonAwesome btnCancelAddingComment;
    private LinearLayout spinnerSubmittingComment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        application = (MockApplication) RuntimeEnvironment.application;
        application.setCommentsViewComponent(component);
        application.createAuthComponent();
        this.mockDependencies();
        when(presenter.getUser()).thenReturn(user);
    }

    /**
     * Mock the dependencies for the fragment.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CommentsFragment fragment = (CommentsFragment) invocation.getArguments()[0];
                fragment.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(CommentsFragment.class));
    }

    /**
     * Start a new instance of the fragment.
     *
     * @param aClass fragment activity class, if null is passed default class will be user
     */
    private <T extends AppCompatActivity> void startFragment(Class<T> aClass) {
        fragment = CommentsFragment.newInstance(lineup.getId());
        if (aClass != null) {
            SupportFragmentTestUtil.startFragment(fragment, aClass);
        } else {
            SupportFragmentTestUtil.startFragment(fragment);
        }
    }

    /**
     * Get the main view from the fragment layout.
     */
    private void getViews() {
        View view = fragment.getView();
        assertNotNull(view);
        spinner = (RelativeLayout) view.findViewById(R.id.spinner);
        assertNotNull(spinner);
        txtSpinner = (TextView) view.findViewById(R.id.spinner_text);
        assertNotNull(txtSpinner);
        error = (RelativeLayout) view.findViewById(R.id.error);
        assertNotNull(error);
        txtError = (TextView) view.findViewById(R.id.txtError);
        assertNotNull(txtError);
        content = (RelativeLayout) view.findViewById(R.id.commentsLayout_mainContent);
        assertNotNull(content);
        listView = (ListView) view.findViewById(R.id.commentsLayout_listView);
        assertNotNull(listView);
        btnAddComment = (Button) view.findViewById(R.id.commentsLayout_btnAddComment);
        assertNotNull(btnAddComment);
        addCommentContent = (RelativeLayout)
                view.findViewById(R.id.commentsLayout_addCommentMainContent);
        assertNotNull(addCommentContent);
        txtComment = (EditText) view.findViewById(R.id.commentsLayout_txtComment);
        assertNotNull(txtComment);
        btnSubmitComment = (ButtonAwesome) view.findViewById(R.id.commentsLayout_btnSubmitComment);
        assertNotNull(btnSubmitComment);
        btnCancelAddingComment = (ButtonAwesome)
                view.findViewById(R.id.commentsLayout_btnCancelAddingComment);
        assertNotNull(btnCancelAddingComment);
        spinnerSubmittingComment = (LinearLayout)
                view.findViewById(R.id.commentsLayout_spinnerSubmittingComment);
        assertNotNull(spinnerSubmittingComment);
    }

    /**
     * Test that the fragment is created correctly.
     */
    @Test
    public void testFragmentIsCreated() {
        String title = application.getString(R.string.commentsLayout_title);
        this.startFragment(MockActivity.class);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertEquals(lineup.getId().intValue(), args.getInt(CommentsView.LINEUP_ID_KEY, -1));
        verify(bfListener).onFragmentActive();
        verify(bfListener).changeTitle(title);
        verify(presenter).onViewCreated(args);
        verify(presenter).onViewLayoutCreated();
        View view = fragment.getView();
        assertNotNull(view);
        listView = (ListView) view.findViewById(R.id.commentsLayout_listView);
        assertNotNull(listView);
        assertTrue(listView.getAdapter() instanceof CommentsAdapter);
    }

    /**
     * Test that the fragment is created correctly when the fragment activity does't implements
     * BaseFragment.Listener interface.
     */
    @Test
    public void testFragmentIsCreatedWhenActivityDoesNotImplementsListener() {
        String title = application.getString(R.string.commentsLayout_title);
        this.startFragment(null);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertEquals(lineup.getId().intValue(), args.getInt(CommentsView.LINEUP_ID_KEY, -1));
        verify(bfListener, never()).onFragmentActive();
        verify(bfListener, never()).changeTitle(title);
        verify(presenter).onViewCreated(args);
        verify(presenter).onViewLayoutCreated();
        View view = fragment.getView();
        assertNotNull(view);
        listView = (ListView) view.findViewById(R.id.commentsLayout_listView);
        assertNotNull(listView);
        assertTrue(listView.getAdapter() instanceof CommentsAdapter);
    }

    /**
     * Test the behavior when showCommentsLoading is called.
     */
    @Test
    public void testShowCommentsLoading() {
        String text = application.getString(R.string.commentsLayout_spinnerText);
        this.startFragment(null);
        this.getViews();
        spinner.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        txtSpinner.setText(null);
        fragment.showCommentsLoading();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtSpinner.getText());
    }

    /**
     * Test the behavior when showLoadingSuccess is called.
     */
    @Test
    public void testShowLoadingSuccess() {
        this.startFragment(null);
        this.getViews();
        Adapter adapter = listView.getAdapter();
        assertNotNull(adapter);
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        assertEquals(0, adapter.getCount());
        fragment.showCommentsLoadingSuccess(comments);
        assertEquals(View.VISIBLE, content.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(comments.size(), adapter.getCount());
    }

    /**
     * Test the behavior when showLoadingFailed is called.
     */
    @Test
    public void testShowLoadingFailed() {
        String text = application.getString(R.string.commentsLayout_loadingCommentsFailed_text);
        this.startFragment(null);
        this.getViews();
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        txtError.setText(null);
        fragment.showCommentLoadingFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when button 'Try Again' is clicked.
     */
    @Test
    public void testBtnTryAgainClick() {
        this.startFragment(null);
        View view = fragment.getView();
        assertNotNull(view);
        Button btn = (Button) view.findViewById(R.id.error_btnTryAgain);
        assertNotNull(btn);
        assertTrue(btn.performClick());
        verify(presenter).loadComments();
    }

    /**
     * Test the behavior when button 'Add Comment' is clicked.
     */
    @SuppressLint("SetTextI18n")
    @Test
    public void testBtnAddCommentClick() {
        this.startFragment(null);
        this.getViews();
        btnAddComment.setVisibility(View.VISIBLE);
        addCommentContent.setVisibility(View.GONE);
        txtComment.setEnabled(false);
        txtComment.setText("Test");
        spinnerSubmittingComment.setVisibility(View.VISIBLE);
        btnSubmitComment.setVisibility(View.VISIBLE);
        btnCancelAddingComment.setVisibility(View.GONE);
        assertTrue(btnAddComment.performClick());
        assertEquals(View.GONE, btnAddComment.getVisibility());
        assertEquals(View.VISIBLE, addCommentContent.getVisibility());
        assertEquals(View.GONE, spinnerSubmittingComment.getVisibility());
        assertEquals(View.GONE, btnSubmitComment.getVisibility());
        assertEquals(View.VISIBLE, btnCancelAddingComment.getVisibility());
        assertTrue(txtComment.isEnabled());
        assertEquals("", txtComment.getText().toString());
    }

    /**
     * Test the behavior when EditText comment text is changed and the new text is a empty string.
     */
    @Test
    public void testTxtCommentTextChangedWithEmptyText() {
        this.startFragment(null);
        this.getViews();
        btnSubmitComment.setVisibility(View.VISIBLE);
        txtComment.setText("     ");
        assertEquals(View.GONE, btnSubmitComment.getVisibility());
    }

    /**
     * Test the behavior when EditText comment text is changed and the new text is not a
     * empty string.
     */
    @SuppressLint("SetTextI18n")
    @Test
    public void testTxtCommentTextChangedWithNotEmptyText() {
        this.startFragment(null);
        this.getViews();
        btnSubmitComment.setVisibility(View.GONE);
        txtComment.setText("Test");
        assertEquals(View.VISIBLE, btnSubmitComment.getVisibility());
    }

    /**
     * Test the behavior when button 'Cancel Adding' is clicked.
     */
    @Test
    public void testBtnCancelAddingCommentClick() {
        this.startFragment(null);
        this.getViews();
        btnAddComment.setVisibility(View.GONE);
        addCommentContent.setVisibility(View.VISIBLE);
        assertTrue(btnCancelAddingComment.performClick());
        assertEquals(View.VISIBLE, btnAddComment.getVisibility());
        assertEquals(View.GONE, addCommentContent.getVisibility());
    }

    /**
     * Test the behavior when button 'Submit Comment' is clicked.
     */
    @Test
    public void testBtnSubmitCommentClick() {
        String text = "Test";
        this.startFragment(null);
        this.getViews();
        txtComment.setEnabled(true);
        txtComment.setText(text);
        btnSubmitComment.setVisibility(View.VISIBLE);
        btnCancelAddingComment.setVisibility(View.VISIBLE);
        spinnerSubmittingComment.setVisibility(View.GONE);
        assertTrue(btnSubmitComment.performClick());
        assertEquals(View.GONE, btnSubmitComment.getVisibility());
        assertEquals(View.GONE, btnCancelAddingComment.getVisibility());
        assertEquals(View.VISIBLE, spinnerSubmittingComment.getVisibility());
        assertFalse(txtComment.isEnabled());
        verify(presenter).addComment(text);
    }

    /**
     * Test the behavior when showCommentAddingSuccess is called.
     */
    @Test
    public void testShowCommentAddingSuccess() {
        String text = application.getString(R.string.commentsLayout_addingCommentSuccess_text);
        final Comment comment = comments.get(1);
        this.startFragment(null);
        this.getViews();
        Adapter adapter = listView.getAdapter();
        assertEquals(0, adapter.getCount());
        btnAddComment.setVisibility(View.GONE);
        addCommentContent.setVisibility(View.VISIBLE);
        fragment.showCommentAddingSuccess(comment);
        assertEquals(View.VISIBLE, btnAddComment.getVisibility());
        assertEquals(View.GONE, addCommentContent.getVisibility());
        assertEquals(text, ShadowToast.getTextOfLatestToast());
        assertEquals(1, adapter.getCount());
        assertSame(comment, adapter.getItem(0));
    }

    /**
     * Test the behavior when showCommentAddingFailed is called.
     */
    @Test
    public void testCommentAddingFailed() {
        String text = application.getString(R.string.commentsLayout_addingCommentFailed_text);
        this.startFragment(null);
        this.getViews();
        Adapter adapter = listView.getAdapter();
        assertEquals(0, adapter.getCount());
        txtComment.setEnabled(false);
        spinnerSubmittingComment.setVisibility(View.VISIBLE);
        btnSubmitComment.setVisibility(View.GONE);
        btnCancelAddingComment.setVisibility(View.GONE);
        fragment.showCommentAddingFailed();
        assertEquals(View.GONE, spinnerSubmittingComment.getVisibility());
        assertEquals(View.VISIBLE, btnSubmitComment.getVisibility());
        assertEquals(View.VISIBLE, btnCancelAddingComment.getVisibility());
        assertTrue(txtComment.isEnabled());
        assertEquals(text, ShadowToast.getTextOfLatestToast());
        assertEquals(0, adapter.getCount());
    }

    /**
     * Test the behavior when updateComment is called.
     */
    @Test
    public void testUpdateComment() {
        final int index = 1;
        final String newBody = "New Test Body";
        this.startFragment(null);
        this.getViews();
        assertTrue(listView.getAdapter() instanceof CommentsAdapter);
        CommentsAdapter adapter = (CommentsAdapter) listView.getAdapter();
        adapter.update(comments);
        assertEquals(comments.size(), adapter.getCount());
        fragment.updateComment(index, newBody);
        verify(presenter).updateComment(comments.get(index), newBody);
    }

    /**
     * Test the behavior when showCommentUpdateSuccess is called.
     */
    @Test
    public void testShowCommentUpdatingSuccess() {
        String text = application.getString(R.string.commentsLayout_updatingCommentSuccess_text);
        final int index = 2;
        this.startFragment(null);
        this.getViews();
        assertTrue(listView.getAdapter() instanceof CommentsAdapter);
        CommentsAdapter adapter = (CommentsAdapter) listView.getAdapter();
        adapter.update(comments);
        View view = adapter.getView(index, null, listView);
        assertNotNull(view);
        ButtonAwesome btnUpdate = (ButtonAwesome) view.findViewById(R.id.commentListItem_btnUpdate);
        assertNotNull(btnUpdate);
        assertTrue(btnUpdate.performClick());
        assertTrue(adapter.isSending(comments.get(index)));
        fragment.showCommentUpdatingSuccess(comments.get(index), comments.get(index));
        assertFalse(adapter.isSending(comments.get(index)));
        assertEquals(text, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test the behavior when showCommentUpdatingFailed is called.
     */
    @Test
    public void testShowCommentUpdatingFailed() {
        String text = application.getString(R.string.commentsLayout_updatingCommentFailed_text);
        final int index = 0;
        this.startFragment(null);
        this.getViews();
        assertTrue(listView.getAdapter() instanceof CommentsAdapter);
        CommentsAdapter adapter = (CommentsAdapter) listView.getAdapter();
        adapter.update(comments);
        View view = adapter.getView(index, null, listView);
        assertNotNull(view);
        ButtonAwesome btnUpdate = (ButtonAwesome) view.findViewById(R.id.commentListItem_btnUpdate);
        assertNotNull(btnUpdate);
        assertTrue(btnUpdate.performClick());
        assertTrue(adapter.isSending(comments.get(index)));
        fragment.showCommentUpdatingFailed(comments.get(index));
        assertFalse(adapter.isSending(comments.get(index)));
        assertEquals(text, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test the behavior when showCommentDeletingSuccess is called.
     */
    @Test
    public void testShowCommentDeletingSuccess() {
        String text = application.getString(R.string.commentLayout_deletingCommentSuccess_text);
        final int index = 2;
        this.startFragment(null);
        this.getViews();
        CommentsAdapter adapter = (CommentsAdapter) listView.getAdapter();
        adapter.update(comments);
        fragment.showCommentDeletingSuccess(comments.get(index));
        assertEquals(comments.size() - 1, adapter.getCount());
        assertEquals(text, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test the behavior when showCommentDeletingFailed is called.
     */
    @Test
    public void testShowCommentDeletingFailed() {
        String text = application.getString(R.string.commentLayout_deletingCommentFailed_text);
        final int index = 1;
        this.startFragment(null);
        this.getViews();
        CommentsAdapter adapter = (CommentsAdapter) listView.getAdapter();
        adapter.update(comments);
        View view = adapter.getView(index, null, listView);
        assertNotNull(view);
        ButtonAwesome btnRemove = (ButtonAwesome) view.findViewById(R.id.commentListItem_btnRemove);
        assertNotNull(btnRemove);
        assertFalse(adapter.isSending(comments.get(index)));
        assertTrue(btnRemove.performClick());
        assertTrue(adapter.isSending(comments.get(index)));
        fragment.showCommentDeletingFailed(comments.get(index));
        assertFalse(adapter.isSending(comments.get(index)));
        assertEquals(text, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Mock activity class for the fragment.
     */
    public static class MockActivity extends AppCompatActivity implements BaseFragment.Listener {

        @Override
        public void onFragmentActive() {
            bfListener.onFragmentActive();
        }

        @Override
        public void changeTitle(String title) {
            bfListener.changeTitle(title);
        }
    }
}