package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.exception.InternalServerErrorException;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;
import com.android.finki.mpip.footballdreamteam.rest.response.ServerResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 26.08.2016.
 */
public class LikeViewPresenterTest {

    @Mock
    private LikeFragment view;

    @Mock
    private LineupApi api;

    @Mock
    private Bundle args;

    @Mock
    private Call<List<UserLike>> callListUserLikes;

    @Mock
    private Call<ServerResponse> callAddLike;

    @Mock
    private Call<Void> callDeleteLike;

    @Captor
    private ArgumentCaptor<Callback<List<UserLike>>> callbackLikesCaptor;

    @Captor
    private ArgumentCaptor<Callback<ServerResponse>> callbackAddLikeCaptor;

    @Captor
    private ArgumentCaptor<Callback<Void>> callbackDeleteLikeCaptor;

    @Captor
    private ArgumentCaptor<UserLike> userLikeCaptor;

    private LikeViewPresenter presenter;
    private final User user = new User(214, "User 214");
    private final UserLike userLike = new UserLike(user.getId(), user.getName(), null);
    private final Lineup lineup = new Lineup(1, 1);
    private List<UserLike> likes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new LikeViewPresenter(view, api, user);
        when(api.likes(anyInt(), anyBoolean(), anyInt(), anyInt())).thenReturn(callListUserLikes);
        when(api.addLike(anyInt())).thenReturn(callAddLike);
        when(api.deleteLike(anyInt())).thenReturn(callDeleteLike);
        likes = new ArrayList<>();
        likes.add(new UserLike(1, "User 1", null));
        likes.add(new UserLike(2, "User 2", null));
        likes.add(new UserLike(3, "User 3", null));
    }

    /**
     * Init the mocked view arguments to return specific value.
     */
    private void initArgs() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
    }

    /**
     * Test the behavior when onViewCreated is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadLikesOnNullBundle() {
        presenter.onViewCreated(null);
    }

    /**
     * Test the behavior when onViewCrated is called with invalid bundle data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadLikesOnInvalidBundleData() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(new User());
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request for loading the likes
     * is sending.
     */
    @Test
    public void testOnViewLayoutCreatedWhenLoadLikesRequestIsSending() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(view, never()).showLoading();
        presenter.onViewLayoutCreated();
        verify(view).showLoading();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request for loading the likes
     * is not sending.
     */
    @Test
    public void testOnViewLayoutCreatedWhenLoadLikesRequestIsNotSending() {
        this.initArgs();
        presenter.onViewLayoutCreated();
        verify(view, never()).showLoading();
    }

    /**
     * Test the behavior when loadLikes is called and the lineup is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadLikesWhenLineupNotSet() {
        presenter.loadLikes();
    }

    /**
     * Test the behavior when loadLikes is called and the view layout is not yet created.
     */
    @Test
    public void testLoadLikesWhenViewLayoutIsNotYetCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).likes(lineup.getId(), true, null, null);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        verify(view, never()).showLoading();
    }

    /**
     * Test the behavior when loadData is called and the view layout is created.
     */
    @Test
    public void testLoadDataWhenViewLayoutIsCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(api).likes(lineup.getId(), true, null, null);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        verify(view).showLoading();
    }

    /**
     * Test the behavior when loadLikes is called and the previous load likes request response
     * is not yet received.
     */
    @Test
    public void testLoadLikesCalledWhenThePreviousLikesRequestIsNotYetReceived() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).likes(lineup.getId(), true, null, null);
        presenter.loadLikes();
        verify(api, times(1)).likes(lineup.getId(), true, null, null);
    }

    /**
     * Test the behavior when loading the likes is successful and the view layout is not yet
     * created when the response is received.
     */
    @Test
    public void testLikeLoadingSuccessWhenViewLayoutNotCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).likes(lineup.getId(), true, null, null);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        verify(view, never()).showLoading();
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        verify(view, never()).showLoadingSuccess(anyListOf(UserLike.class));
    }

    /**
     * Test the behavior when loading the likes is successful and the view layout is created before
     * the response is received and the user has already liked the lineup.
     */
    @Test
    public void testLikeLoadingSuccessWhenViewLayoutCreatedBeforeResponseAndUserLikedTheLineup() {
        likes.add(new UserLike(user.getId(), user.getName(), null));
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).likes(lineup.getId(), true, null, null);
        verify(view, never()).showLoading();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        presenter.onViewLayoutCreated();
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        verify(view).showLoadingSuccess(likes);
        verify(view).showRemoveLikeButton();
        verify(view, never()).showAddLikeButton();
    }

    /**
     * Test the behavior when loading the likes is successful, the view layout is created and the
     * user hasn't already liked the lineup.
     */
    @Test
    public void testLikeLoadingSuccessAndUserHasNotLikedTheLineup() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).likes(lineup.getId(), true, null, null);
        verify(view, never()).showLoading();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        presenter.onViewLayoutCreated();
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        verify(view).showLoadingSuccess(likes);
        verify(view).showAddLikeButton();
        verify(view, never()).showRemoveLikeButton();
    }

    /**
     * Test the behavior when loading the likes failed and the view layout is not created when the
     * response is received.
     */
    @Test
    public void testLikesLoadingFailedAndViewLayoutNotCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onFailure(callListUserLikes, new UnknownHostException());
        verify(view, never()).showLoadingFailed();
        verify(view, never()).showNoInternetConnection();
    }

    /**
     * Test the behavior when loading the likes failed and the view layout is created.
     */
    @Test
    public void testLikesLoadingFailedAndViewLayoutIsCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(view).showLoading();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onFailure(callListUserLikes, new UnknownHostException());
        verify(view).showLoadingFailed();
        verify(view).showNoInternetConnection();
    }

    /**
     * Test the behavior when add is called and the lineup is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddLikeWhenLineupNotYerSet() {
        presenter.addLike();
    }

    /**
     * Test the behavior when add like is called and the user has already liked the lineup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddLikeWhenUserHasLikedTheLineup() {
        likes.add(userLike);
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
    }

    /**
     * Test the behavior when adding the like is successful and the view layout is destroyed before
     * the response is received.
     */
    @Test
    public void testAddLikeSuccessAndViewLayoutNotCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        verify(view).showLoading();
        presenter.addLike();
        verify(view).showLikeAdding();
        verify(api).addLike(lineup.getId());
        presenter.onViewLayoutDestroyed();
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike,
                Response.success(new ServerResponse()));
        verify(view, never()).showLikeAddingSuccess(any(UserLike.class));
        verify(view, never()).showLikeAddingFailed();
    }

    /**
     * Test the behavior when adding the like is successful and the view layout is created.
     */
    @Test
    public void testAddLikeSuccessAndViewLayoutCreated() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        verify(view).showLoading();
        presenter.addLike();
        verify(view).showLikeAdding();
        verify(api).addLike(lineup.getId());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike,
                Response.success(new ServerResponse()));
        verify(view).showLikeAddingSuccess(userLikeCaptor.capture());
        assertEquals(user.getId(), Integer.valueOf(userLikeCaptor.getValue().getId()));
        assertEquals(user.getName(), userLikeCaptor.getValue().getName());
        Date createdAt = userLikeCaptor.getValue().getPivot().getCreatedAt();
        assertNotNull(createdAt);
        assertTrue(System.currentTimeMillis() - createdAt.getTime() < 1000);
        verify(view, never()).showLikeAddingFailed();
    }

    /**
     * Test the behavior when adding the like failed and the view layout is destroyed when the
     * response is received.
     */
    @Test
    public void testAddLikesFailedAndViewLayoutDestroyed() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
        verify(view).showLikeAdding();
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackAddLikeCaptor.getValue().onFailure(callAddLike, new SocketTimeoutException());
        verify(view, never()).showLikeAddingFailed();
        verify(view, never()).showSocketTimeout();
    }

    /**
     * Test the behavior when adding the like failed and the view layout is not destroyed when the
     * response is received.
     */
    @Test
    public void testAddLikesFailedAndViewLayoutNotDestroyed() {
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
        verify(view).showLikeAdding();
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onFailure(callAddLike, new SocketTimeoutException());
        verify(view).showLikeAddingFailed();
        verify(view).showSocketTimeout();
    }

    /**
     * Test the behavior when remove is called and the user didn't liked the lineup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveLikeWhenLineupIsNotYetSet() {
        presenter.removeLike();
    }

    /**
     * Test the behavior when onRemoveSuccess like is called and the user didn't liked the lineup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveLikeWhenTheUserDidNotLikedTheLineup() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
    }

    /**
     * Test the behavior when removing the like is successful and the view layout is never created.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testLikeRemovingSuccessAndViewLayoutNotCreated() {
        likes.add(userLike);
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(view, never()).showLikeRemoving();
        verify(api).deleteLike(lineup.getId());
        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        Void response = null;
        callbackDeleteLikeCaptor.getValue().onResponse(callDeleteLike, Response.success(response));
        verify(view, never()).showLikeRemovingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when removing the like is successful and the view layout is never created.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testLikeRemovingSuccessAndViewLayoutNotDestroyed() {
        likes.add(userLike);
        assertTrue(likes.contains(userLike));
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(view).showLikeRemoving();
        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        Void response = null;
        callbackDeleteLikeCaptor.getValue().onResponse(callDeleteLike, Response.success(response));
        verify(view).showLikeRemovingSuccess(userLikeCaptor.capture());
        assertEquals(userLike.getId(), userLikeCaptor.getValue().getId());
        assertEquals(userLike.getName(), userLikeCaptor.getValue().getName());
    }

    /**
     * Test the behavior when removing the like failed and the view layout is destroyed when the
     * response is received.
     */
    @Test
    public void testRemoveLikeFailedAndViewLayoutDestroyed() {
        likes.add(userLike);
        assertTrue(likes.contains(userLike));
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(view).showLikeRemoving();
        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackDeleteLikeCaptor.getValue().onFailure(callDeleteLike,
                new InternalServerErrorException());
        verify(view, never()).showLikeRemovingFailed();
        verify(view, never()).showInternalServerError();
    }

    /**
     * Test the behavior when removing the like failed and the view layout is not destroyed when
     * the response is received.
     */
    @Test
    public void testRemoveLikeFailedAndViewLayoutNotDestroyed() {
        likes.add(userLike);
        assertTrue(likes.contains(userLike));
        this.initArgs();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(view).showLikeRemoving();
        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        callbackDeleteLikeCaptor.getValue().onFailure(callDeleteLike,
                new InternalServerErrorException());
        verify(view).showLikeRemovingFailed();
        verify(view).showInternalServerError();
    }

    /**
     * Test the behavior when loadLikes is called after the previous likes loading succeeded.
     */
    @Test
    public void testLoadLikesCalledAfterPreviousLoadingSucceeded() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.loadLikes();
        verify(api, times(2)).likes(anyInt(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when loadLikes is called after the previous likes loading failed.
     */
    @Test
    public void testLoadLikesCalledAfterPreviousLoadingFailed() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onFailure(callListUserLikes, new Throwable());
        presenter.loadLikes();
        verify(api, times(2)).likes(anyInt(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when addLike is called after the previous like adding succeeded.
     */
    @Test
    public void testAddLikeCalledAfterPreviousAddingSucceeded() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike,
                Response.success(new ServerResponse()));
        try {
            presenter.addLike();
            fail();
        } catch (IllegalArgumentException exp) {
            verify(api, times(1)).addLike(anyInt());
        }
    }

    /**
     * Test the behavior when addLike is called after the previous like adding failed.
     */
    @Test
    public void testAddLikeCalledAfterPreviousAddingFailed() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onFailure(callAddLike, new Throwable());
        presenter.addLike();
        verify(api, times(2)).addLike(anyInt());
    }

    /**
     * Test the behavior when removeLike is called after the previous like removing succeeded.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testRemoveLikeCalledAfterPreviousRemovingSucceeded() {
        likes.add(userLike);
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        Void result = null;
        callbackDeleteLikeCaptor.getValue().onResponse(callDeleteLike, Response.success(result));
        try {
            presenter.removeLike();
            fail();
        } catch (IllegalArgumentException exp) {
            verify(api, times(1)).deleteLike(anyInt());
        }
    }

    /**
     * Test the behavior when removeLike is called after the previous like removing failed.
     */
    @Test
    public void testRemoveLikeCalledAfterPreviousRemovingFailed() {
        likes.add(userLike);
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        callbackDeleteLikeCaptor.getValue().onFailure(callDeleteLike, new Throwable());
        presenter.removeLike();
        verify(api, times(2)).deleteLike(anyInt());
    }

    /**
     * Test the behavior when the view is destroyed and the load likes request is sending.
     */
    @Test
    public void testOnViewDestroyedWhenLoadLikesRequestIsSending() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).likes(anyInt(), anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(callListUserLikes).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after a request to load likes that
     * succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterLoadLikesRequestSucceeded() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).likes(anyInt(), anyBoolean(), anyInt(), anyInt());
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.onViewDestroyed();
        verify(callListUserLikes, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after a request to load likes that
     * failed.
     */
    @Test
    public void testOnViewDestroyedAfterLoadLikesRequestFailed() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(api).likes(anyInt(), anyBoolean(), anyInt(), anyInt());
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onFailure(callListUserLikes, new Throwable());
        presenter.onViewDestroyed();
        verify(callListUserLikes, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and the add like request is sending.
     */
    @Test
    public void testOnViewDestroyedWhenAddLikeRequestIsSending() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
        verify(api).addLike(anyInt());
        presenter.onViewDestroyed();
        verify(callAddLike).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after a request to add like that
     * succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterAddLikeRequestSucceeded() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
        verify(api).addLike(anyInt());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue()
                .onResponse(callAddLike, Response.success(new ServerResponse()));
        presenter.onViewDestroyed();
        verify(callAddLike, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after a request to add like that
     * failed.
     */
    @Test
    public void testOnViewDestroyedAfterAddLikeRequestFailed() {
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
        verify(api).addLike(anyInt());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onFailure(callAddLike, new Throwable());
        presenter.onViewDestroyed();
        verify(callAddLike, never()).cancel();
    }

    /**
     * Test the behavior when the view layout is destroyed and the remove like request is sending.
     */
    @Test
    public void testOnViewDestroyedWhenRemoveLikeRequestIsSending() {
        likes.add(userLike);
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(api).deleteLike(anyInt());
        presenter.onViewDestroyed();
        verify(callDeleteLike).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after a request to remove like that
     * succeeded.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testOnViewDestroyedAfterRemoveLikeRequestSucceeded() {
        likes.add(userLike);
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(api).deleteLike(anyInt());
        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        Void response = null;
        callbackDeleteLikeCaptor.getValue().onResponse(callDeleteLike, Response.success(response));
        presenter.onViewDestroyed();
        verify(callDeleteLike, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after a request to remove like that
     * failed.
     */
    @Test
    public void testOnViewDestroyedAfterRemoveLikeRequestFailed() {
        likes.add(userLike);
        this.initArgs();
        presenter.onViewCreated(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(api).deleteLike(anyInt());
        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        callbackDeleteLikeCaptor.getValue().onFailure(callDeleteLike, new Throwable());
        presenter.onViewDestroyed();
        verify(callDeleteLike, never()).cancel();
    }
}
