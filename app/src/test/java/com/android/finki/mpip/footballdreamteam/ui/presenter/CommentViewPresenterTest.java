package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.exception.InternalServerErrorException;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.CommentRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.CommentResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.CommentsView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 25.09.2016.
 */
public class CommentViewPresenterTest {

    @Mock
    private CommentsView view;

    @Mock
    private LineupApi api;

    @Mock
    private Bundle args;

    @Mock
    private Call<List<Comment>> commentsCall;

    @Mock
    private Call<CommentResponse> addCommentCall;

    @Mock
    private Call<CommentResponse> updateCommentCall;

    @Mock
    private Call<Void> deleteCommentCall;

    @Captor
    private ArgumentCaptor<Callback<List<Comment>>> commentsCallbackCaptor;

    @Captor
    private ArgumentCaptor<Callback<CommentResponse>> commentResponseCaptor;

    @Captor
    private ArgumentCaptor<Callback<Void>> emptyResponseCaptor;

    @Captor
    private ArgumentCaptor<Integer> intCaptor;

    @Captor
    private ArgumentCaptor<CommentRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<Comment> commentCaptor;

    private CommentsViewPresenter presenter;
    private Calendar calendar = new GregorianCalendar(2016, 25, 0, 16, 43, 35);
    private Date date = calendar.getTime();
    private User user = new User(1, "User");
    private Lineup lineup = new Lineup(1, 1);
    private List<Comment> comments = Arrays.asList(new Comment(1, 1, 1, "Comment 1", date, date),
            new Comment(2, 2, 1, "Comment 1", date, date),
            new Comment(3, 1, 1, "Comment 2", date, date),
            new Comment(4, 2, 1, "Comment 3", date, date));
    private CommentResponse response =
            new CommentResponse(new Comment(5, 1, 1, "CommentResponse", null, null));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(api.comments(anyInt(), anyBoolean(), anyInt(), anyInt())).thenReturn(commentsCall);
        when(api.addComment(anyInt(), any(CommentRequest.class))).thenReturn(addCommentCall);
        when(api.updateComment(anyInt(), anyInt(), any(CommentRequest.class)))
                .thenReturn(updateCommentCall);
        when(api.deleteComment(anyInt(), anyInt())).thenReturn(deleteCommentCall);
        presenter = new CommentsViewPresenter(view, api, user);
    }

    /**
     * Init the mocked view arguments to return specific value.
     */
    private void initArgs() {
        when(args.getInt(CommentsView.LINEUP_ID_KEY, -1)).thenReturn(lineup.getId());
    }

    /**
     * Test the behavior when onViewCreated is called with invalid lineupId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithInvalidLineupId() {
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior when loadComments is called and the lineup id is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadCommentsWhenLineupIdNotSet() {
        presenter.loadComments();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request to load comments is
     * sending at the moment.
     */
    @Test
    public void testOnViewLayoutCreatedWhenLoadCommentsRequestIsSending() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).comments(lineup.getId(), true, null, null);
        verify(view, never()).showCommentsLoading();
        presenter.onViewLayoutCreated();
        verify(view).showCommentsLoading();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request to load comments is not
     * sending at the moment.
     */
    @Test
    public void testOnViewLayoutCreatedWhenLoadCommentsRequestIsNotSending() {
        presenter.onViewLayoutCreated();
        verify(view, never()).showCommentsLoading();
    }

    /**
     * Test the behavior when loading the comments failed and the view layout is created before the
     * request is send.
     */
    @Test
    public void testLoadCommentsFailedAndViewLayoutCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(api).comments(lineup.getId(), true, null, null);
        verify(view).showCommentsLoading();
        verify(commentsCall).enqueue(commentsCallbackCaptor.capture());
        commentsCallbackCaptor.getValue().onFailure(commentsCall, new SocketTimeoutException());
        verify(view).showCommentLoadingFailed();
        verify(view).showSocketTimeout();
        verify(view, never()).showCommentsLoadingSuccess(anyListOf(Comment.class));
    }

    /**
     * Test the behavior when loading the comments failed and the view layout is never created.
     */
    @Test
    public void testLoadCommentsFailedAndViewLayoutNotCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).comments(lineup.getId(), true, null, null);
        verify(view, never()).showCommentsLoading();
        verify(commentsCall).enqueue(commentsCallbackCaptor.capture());
        commentsCallbackCaptor.getValue().onFailure(commentsCall, new SocketTimeoutException());
        verify(view, never()).showCommentLoadingFailed();
        verify(view, never()).showSocketTimeout();
        verify(view, never()).showCommentsLoadingSuccess(anyListOf(Comment.class));
    }

    /**
     * Test the behavior when loading the comments is successful and the view layout is not
     * destroyed before the response is received.
     */
    @Test
    public void testLoadCommentsSuccessAndViewLayoutNotDestroyed() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(api).comments(lineup.getId(), true, null, null);
        verify(view).showCommentsLoading();
        verify(commentsCall).enqueue(commentsCallbackCaptor.capture());
        commentsCallbackCaptor.getValue().onResponse(commentsCall, Response.success(comments));
        verify(view).showCommentsLoadingSuccess(comments);
        verify(view, never()).showCommentLoadingFailed();
        assertSame(comments, presenter.getComments());
    }

    /**
     * Test the behavior when loading the comments is successful and the view layout is
     * destroyed before the response is received.
     */
    @Test
    public void testLoadCommentsSuccessAndViewLayoutDestroyed() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(api).comments(lineup.getId(), true, null, null);
        verify(view).showCommentsLoading();
        verify(commentsCall).enqueue(commentsCallbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        commentsCallbackCaptor.getValue().onResponse(commentsCall, Response.success(comments));
        verify(view, never()).showCommentsLoadingSuccess(comments);
        verify(view, never()).showCommentLoadingFailed();
        assertSame(comments, presenter.getComments());
    }

    /**
     * Test the behavior when loadComments is called and the previous request to load comments is
     * not yet finished.
     */
    @Test
    public void testLoadCommentsCalledWhenThePreviousLoadRequestIsNotFinished() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.loadComments();
        verify(api).comments(anyInt(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when loadComment is called after the previous request to load them
     * succeeded.
     */
    @Test
    public void testLoadCommentAfterPreviousLoadingRequestSucceeded() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(commentsCall).enqueue(commentsCallbackCaptor.capture());
        commentsCallbackCaptor.getValue().onResponse(commentsCall, Response.success(comments));
        presenter.loadComments();
        verify(api, times(2)).comments(anyInt(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when loadComment is called after the previous request to load them
     * failed.
     */
    @Test
    public void testLoadCommentAfterPreviousLoadingRequestFailed() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(commentsCall).enqueue(commentsCallbackCaptor.capture());
        commentsCallbackCaptor.getValue().onFailure(commentsCall, new Throwable());
        presenter.loadComments();
        verify(api, times(2)).comments(anyInt(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when addComment is called and the lineup id is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddCommentWhenLineupIdIsNotSet() {
        presenter.addComment("Test");
    }

    /**
     * Test the behavior when addComment is called with null param..
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddCommentWithNullParam() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.addComment(null);
    }

    /**
     * Test the behavior when adding the comment failed and the view layout is created before the
     * request is send.
     */
    @Test
    public void testAddCommentFailedAndViewLayoutCreated() {
        final String body = response.getComment().getBody();
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.addComment(body);
        verify(api).addComment(intCaptor.capture(), requestCaptor.capture());
        assertEquals(lineup.getId(), intCaptor.getValue());
        assertEquals(body, requestCaptor.getValue().getBody());
        verify(addCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onFailure(addCommentCall, new UnknownHostException());
        verify(view).showCommentAddingFailed();
        verify(view).showNoInternetConnection();
        verify(view, never()).showCommentAddingSuccess(any(Comment.class));
    }

    /**
     * Test the behavior when adding the comment failed and the view layout is destroyed before the
     * response is received.
     */
    @Test
    public void testAddCommentFailedAndViewLayoutDestroyed() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.addComment(response.getComment().getBody());
        verify(addCommentCall).enqueue(commentResponseCaptor.capture());
        presenter.onViewLayoutDestroyed();
        commentResponseCaptor.getValue().onFailure(addCommentCall, new UnknownHostException());
        verify(view, never()).showCommentAddingFailed();
        verify(view, never()).showNoInternetConnection();
        verify(view, never()).showCommentAddingSuccess(any(Comment.class));
    }

    /**
     * Test the behavior when adding the comment is successful adn the view layout is not destroyed
     * before the response is received.
     */
    @Test
    public void testAddCommentSuccessAndViewLayoutNotDestroyed() {
        final Comment comment = response.getComment();
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.addComment(comment.getBody());
        verify(api).addComment(intCaptor.capture(), requestCaptor.capture());
        assertEquals(lineup.getId(), intCaptor.getValue());
        assertEquals(comment.getBody(), requestCaptor.getValue().getBody());
        verify(addCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onResponse(addCommentCall, Response.success(response));
        verify(view).showCommentAddingSuccess(commentCaptor.capture());
        assertEquals(comment.getId(), commentCaptor.getValue().getId());
        assertEquals(comment.getUserId(), commentCaptor.getValue().getUserId());
        assertEquals(comment.getBody(), commentCaptor.getValue().getBody());
        verify(view, never()).showCommentAddingFailed();
    }

    /**
     * Test the behavior when adding the comment is successful adn the view layout is destroyed
     * before the response is received.
     */
    @Test
    public void testAddCommentSuccessAndViewLayoutDestroyed() {
        final Comment comment = response.getComment();
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.addComment(comment.getBody());
        verify(addCommentCall).enqueue(commentResponseCaptor.capture());
        presenter.onViewLayoutDestroyed();
        commentResponseCaptor.getValue().onResponse(addCommentCall, Response.success(response));
        verify(view, never()).showCommentAddingSuccess(any(Comment.class));
        verify(view, never()).showCommentAddingFailed();
    }

    /**
     * Test the behavior when updateComment is called and the lineup id is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCommentWhenLineupIdIsNotSet() {
        presenter.updateComment(new Comment(), "Test");
    }

    /**
     * Test the behavior when updateComment is called with null comment param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCommentWithNullCommentParam() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.updateComment(null, "Test");
    }

    /**
     * Test the behavior when updateComment is called with null body param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCommentCalledWithNullBodyParam() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.updateComment(comments.get(0), null);
    }

    /**
     * Test the behavior when the updating the comment failed and the view layout is not destroyed
     * before the response is received.
     */
    @Test
    public void testCommentUpdateFailedAndViewLayoutNotDestroyed() {
        final Comment comment = comments.get(1);
        final String newBody = "Test";
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.updateComment(comment, newBody);
        verify(api).updateComment(intCaptor.capture(),
                intCaptor.capture(), requestCaptor.capture());
        assertEquals(lineup.getId(), intCaptor.getAllValues().get(0));
        assertEquals(comment.getId(), intCaptor.getAllValues().get(1));
        assertEquals(newBody, requestCaptor.getValue().getBody());
        verify(updateCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onFailure(updateCommentCall, new SocketTimeoutException());
        verify(view).showCommentUpdatingFailed(comment);
        verify(view).showSocketTimeout();
        verify(view, never()).showCommentUpdatingSuccess(any(Comment.class), any(Comment.class));
    }

    /**
     * Test the behavior when the updating the comment failed and the view layout is destroyed
     * before the response is received.
     */
    @Test
    public void testCommentUpdateFailedAndViewLayoutDestroyed() {
        final Comment comment = comments.get(1);
        final String newBody = "Test";
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.updateComment(comment, newBody);
        verify(updateCommentCall).enqueue(commentResponseCaptor.capture());
        presenter.onViewLayoutDestroyed();
        commentResponseCaptor.getValue().onFailure(updateCommentCall, new SocketTimeoutException());
        verify(view, never()).showCommentUpdatingFailed(comment);
        verify(view, never()).showSocketTimeout();
        verify(view, never()).showCommentUpdatingSuccess(any(Comment.class), any(Comment.class));
    }

    /**
     * Test the behavior when updating the comment is successful and the view layout is created
     * before the request is send.
     */
    @Test
    public void testCommentsUpdateSuccessAndViewLayoutCreated() {
        final Comment comment = comments.get(2);
        final String newBody = "Test";
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.updateComment(comment, newBody);
        verify(api).updateComment(intCaptor.capture(),
                intCaptor.capture(), requestCaptor.capture());
        assertEquals(lineup.getId(), intCaptor.getAllValues().get(0));
        assertEquals(comment.getId(), intCaptor.getAllValues().get(1));
        assertEquals(newBody, requestCaptor.getValue().getBody());
        verify(updateCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onResponse(updateCommentCall, Response.success(response));
        verify(view).showCommentUpdatingSuccess(commentCaptor.capture(), commentCaptor.capture());
        assertSame(comment, commentCaptor.getAllValues().get(0));
        assertSame(response.getComment(), commentCaptor.getAllValues().get(1));
        verify(view, never()).showCommentUpdatingFailed(any(Comment.class));
    }

    /**
     * Test the behavior when updating the comment is successful and the view layout is never
     * created.
     */
    @Test
    public void testCommentsUpdateSuccessAndViewLayoutNotCreated() {
        final Comment comment = comments.get(2);
        final String newBody = "Test";
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.updateComment(comment, newBody);
        verify(updateCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onResponse(updateCommentCall, Response.success(response));
        verify(view, never()).showCommentUpdatingSuccess(any(Comment.class), any(Comment.class));
        verify(view, never()).showCommentUpdatingFailed(any(Comment.class));
    }

    /**
     * Test the behavior when deleteComment is called and the lineup id is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteCommentWhenLineupIdNotSet() {
        presenter.deleteComment(new Comment());
    }

    /**
     * Test the behavior when deleteComment is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteCommentWithNullParam() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.deleteComment(null);
    }

    /**
     * Test the behavior when deleting the comment failed and the view layout is created before
     * the request is send.
     */
    @Test
    public void testDeleteCommentFailedAndViewLayoutCreated() {
        final Comment comment = comments.get(2);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.deleteComment(comment);
        verify(api).deleteComment(intCaptor.capture(), intCaptor.capture());
        assertEquals(lineup.getId(), intCaptor.getAllValues().get(0));
        assertEquals(comment.getId(), intCaptor.getAllValues().get(1));
        verify(deleteCommentCall).enqueue(emptyResponseCaptor.capture());
        emptyResponseCaptor.getValue()
                .onFailure(deleteCommentCall, new InternalServerErrorException());
        verify(view).showCommentDeletingFailed(comment);
        verify(view).showInternalServerError();
        verify(view, never()).showCommentDeletingSuccess(any(Comment.class));
    }

    /**
     * Test the behavior when deleting the comment failed and the view layout is never created.
     */
    @Test
    public void testDeleteCommentFailedAndViewLayoutNoyCreated() {
        final Comment comment = comments.get(2);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.deleteComment(comment);
        verify(deleteCommentCall).enqueue(emptyResponseCaptor.capture());
        emptyResponseCaptor.getValue()
                .onFailure(deleteCommentCall, new InternalServerErrorException());
        verify(view, never()).showCommentDeletingFailed(any(Comment.class));
        verify(view, never()).showInternalServerError();
        verify(view, never()).showCommentDeletingSuccess(any(Comment.class));
    }

    /**
     * Test the behavior when deleting the comment is successful and the view layout is not
     * destroyed before the response is received.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testCommentDeleteSuccessAndViewLayoutNotDestroyed() {
        final Comment comment = comments.get(1);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.deleteComment(comment);
        verify(api).deleteComment(intCaptor.capture(), intCaptor.capture());
        assertEquals(lineup.getId(), intCaptor.getAllValues().get(0));
        assertEquals(comment.getId(), intCaptor.getAllValues().get(1));
        verify(deleteCommentCall).enqueue(emptyResponseCaptor.capture());
        Void response = null;
        emptyResponseCaptor.getValue().onResponse(deleteCommentCall, Response.success(response));
        verify(view).showCommentDeletingSuccess(comment);
        verify(view, never()).showCommentDeletingFailed(any(Comment.class));
    }

    /**
     * Test the behavior when deleting the comment is successful and the view layout is
     * destroyed before the response is received.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testCommentDeleteSuccessAndViewLayoutDestroyed() {
        final Comment comment = comments.get(1);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.deleteComment(comment);
        verify(deleteCommentCall).enqueue(emptyResponseCaptor.capture());
        presenter.onViewLayoutDestroyed();
        Void response = null;
        emptyResponseCaptor.getValue().onResponse(deleteCommentCall, Response.success(response));
        verify(view, never()).showCommentDeletingSuccess(comment);
        verify(view, never()).showCommentDeletingFailed(any(Comment.class));
    }

    /**
     * Test the behavior when addComment is called and a request for adding a comment is
     * already sending and that request succeeded.
     */
    @Test
    public void testAddCalledBeforeThePreviousAddRequestFinishedAndThatRequestSucceeded() {
        final String body1 = "Test Comment Body 1";
        final String body2 = "Test Comment Body 2";
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.addComment(body1);
        presenter.addComment(body2);
        verify(api).addComment(anyInt(), requestCaptor.capture());
        assertEquals(body1, requestCaptor.getValue().getBody());
        verify(addCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onResponse(addCommentCall, Response.success(response));
        verify(api, times(2)).addComment(anyInt(), requestCaptor.capture());
        assertEquals(body2, requestCaptor.getAllValues().get(2).getBody());
    }

    /**
     * Test the behavior when addComment is called and a request for adding a comment is
     * already sending and that request failed.
     */
    @Test
    public void testAddCalledBeforeThePreviousAddRequestFinishedAndThatRequestFailed() {
        final String body1 = "Test Comment Body 1";
        final String body2 = "Test Comment Body 2";
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.addComment(body1);
        presenter.addComment(body2);
        verify(api).addComment(anyInt(), requestCaptor.capture());
        assertEquals(body1, requestCaptor.getValue().getBody());
        verify(addCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onFailure(addCommentCall, new Throwable());
        verify(api, times(2)).addComment(anyInt(), requestCaptor.capture());
        assertEquals(body2, requestCaptor.getAllValues().get(2).getBody());
    }

    /**
     * Test the behavior when addComment is called and a request for updating a comment is
     * already sending and that request succeeded.
     */
    @Test
    public void testAddCalledBeforeThePreviousUpdateRequestFinishedAndThatRequestSucceeded() {
        final Comment comment = comments.get(1);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.updateComment(comment, "");
        presenter.addComment("");
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(api, never()).addComment(anyInt(), any(CommentRequest.class));
        verify(updateCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onResponse(updateCommentCall, Response.success(response));
        verify(api).addComment(anyInt(), any(CommentRequest.class));
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
    }

    /**
     * Test the behavior when addComment is called and a request for updating a comment is
     * already sending and that request succeeded.
     */
    @Test
    public void testAddCalledBeforeThePreviousUpdateRequestFinishedAndThatRequestFailed() {
        final Comment comment = comments.get(1);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.updateComment(comment, "");
        presenter.addComment("");
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(api, never()).addComment(anyInt(), any(CommentRequest.class));
        verify(updateCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onFailure(updateCommentCall, new Throwable());
        verify(api).addComment(anyInt(), any(CommentRequest.class));
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
    }

    /**
     * Test the behavior when updateComment is called and a request for deleting a comment is
     * already sending and that request succeeded.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testUpdateCalledBeforeThePreviousDeleteRequestFinishedAndThatRequestSucceeded() {
        final Comment comment = comments.get(1);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.deleteComment(comment);
        presenter.updateComment(comment, "");
        verify(api).deleteComment(anyInt(), anyInt());
        verify(api, never()).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(deleteCommentCall).enqueue(emptyResponseCaptor.capture());
        Void response = null;
        emptyResponseCaptor.getValue().onResponse(deleteCommentCall, Response.success(response));
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(api).deleteComment(anyInt(), anyInt());
    }

    /**
     * Test the behavior when updateComment is called and a request for updating a comment is
     * already sending and that request succeeded.
     */
    @Test
    public void testUpdateCalledBeforeThePreviousDeleteRequestFinishedAndThatRequestFailed() {
        final Comment comment = comments.get(1);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.deleteComment(comment);
        presenter.updateComment(comment, "");
        verify(api).deleteComment(anyInt(), anyInt());
        verify(api, never()).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(deleteCommentCall).enqueue(emptyResponseCaptor.capture());
        emptyResponseCaptor.getValue().onFailure(deleteCommentCall, new Throwable());
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(api).deleteComment(anyInt(), anyInt());
    }

    /**
     * Test that all add/update/delete call will be execute only one at a time and the other call
     * will be executed only after the previous finished.
     */
    @Test
    public void testAllCallWillBeExecuteOneByOneAfterThePreviousIsFinished() {
        final String body1 = "Test Comment Body 1";
        final String body2 = "Test Comment Body 2";
        final Comment comment = comments.get(0);
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.addComment(body1);
        presenter.updateComment(comment, "");
        presenter.addComment(body2);
        presenter.deleteComment(comment);
        verify(api).addComment(anyInt(), requestCaptor.capture());
        assertEquals(body1, requestCaptor.getValue().getBody());
        verify(api, never()).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(api, never()).deleteComment(anyInt(), anyInt());
        verify(addCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getValue().onResponse(addCommentCall, Response.success(response));
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(api).addComment(anyInt(), any(CommentRequest.class));
        verify(api, never()).deleteComment(anyInt(), anyInt());
        verify(updateCommentCall).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getAllValues().get(1)
                .onResponse(updateCommentCall, Response.success(response));
        verify(api, times(2)).addComment(anyInt(), requestCaptor.capture());
        assertEquals(body2, requestCaptor.getAllValues().get(2).getBody());
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(api, never()).deleteComment(anyInt(), anyInt());
        verify(addCommentCall, times(2)).enqueue(commentResponseCaptor.capture());
        commentResponseCaptor.getAllValues().get(3)
                .onResponse(addCommentCall, Response.success(response));
        verify(api).deleteComment(anyInt(), anyInt());
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        verify(api, times(2)).addComment(anyInt(), requestCaptor.capture());
    }

    /**
     * Test the behavior when onViewDestroyed is called and a request to load comment is sending.
     */
    @Test
    public void testOnViewDestroyedWhenLoadingRequestIsSending() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).comments(anyInt(), anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(commentsCall).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and the previous request to load them
     * succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterLoadingRequestSucceeded() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(commentsCall).enqueue(commentsCallbackCaptor.capture());
        commentsCallbackCaptor.getValue().onResponse(commentsCall, Response.success(comments));
        verify(api).comments(anyInt(), anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(commentsCall, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and the previous request to load them
     * failed.
     */
    @Test
    public void testOnViewDestroyedAfterLoadingRequestFailed() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(commentsCall).enqueue(commentsCallbackCaptor.capture());
        commentsCallbackCaptor.getValue().onFailure(commentsCall, new Throwable());
        verify(api).comments(anyInt(), anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(commentsCall, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and add comment call is sending.
     */
    @Test
    public void testOnViewDestroyedWhenAddCommentCallIsSending() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.addComment("");
        verify(api).addComment(anyInt(), any(CommentRequest.class));
        presenter.onViewDestroyed();
        verify(addCommentCall).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and update comment call is sending.
     */
    @Test
    public void testOnViewDestroyedWhenUpdateCommentCallIsSending() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.updateComment(comments.get(1), "");
        verify(api).updateComment(anyInt(), anyInt(), any(CommentRequest.class));
        presenter.onViewDestroyed();
        verify(updateCommentCall).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and delete comment call is sending.
     */
    @Test
    public void testOnViewDestroyedWhenDeleteCommentCallIsSending() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.deleteComment(comments.get(1));
        verify(api).deleteComment(anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(deleteCommentCall).cancel();
    }

    /**
     * Test the behavior when onVIewDestroyed is called and none add/update/delete request is
     * sending.
     */
    @Test
    public void testOnViewDestroyedWhenNoneRequestForCommentChangingIsSending() {
        presenter.onViewDestroyed();
        verify(addCommentCall, never()).cancel();
        verify(updateCommentCall, never()).cancel();
        verify(deleteCommentCall, never()).cancel();
    }
}
