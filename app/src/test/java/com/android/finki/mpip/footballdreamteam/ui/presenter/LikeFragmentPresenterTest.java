package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.LikeDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.exception.LikeException;
import com.android.finki.mpip.footballdreamteam.exception.LineupException;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 26.08.2016.
 */
public class LikeFragmentPresenterTest {

    @Mock
    private LikeFragment fragment;

    @Mock
    private LineupApi api;

    @Mock
    private LineupDBService lineupDBService;

    @Mock
    private LikeDBService likeDBService;

    @Mock
    private Bundle args;

    @Mock
    private Call<List<UserLike>> callListUserLikes;

    @Captor
    private ArgumentCaptor<Callback<List<UserLike>>> callbackLikesCaptor;

    @Mock
    private Call<ServerResponse> callAddLike;

    @Captor
    private ArgumentCaptor<Callback<ServerResponse>> callbackAddLikeCaptor;

//    @Mock
//    private Call<Void> callDeleteLike;
//
//    @Captor
//    private ArgumentCaptor<Callback<Void>> callbackDeleteLikeCaptor;

    private LikeViewPresenter presenter;
    private final User user = new User(214, "User 214");
    private final Lineup lineup = new Lineup(1, 1);
    private List<UserLike> likes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new LikeViewPresenter(fragment, api, user, lineupDBService, likeDBService);
        when(api.likes(anyInt(), anyBoolean(), anyInt(), anyInt()))
                .thenReturn(callListUserLikes);
        when(api.addLike(anyInt())).thenReturn(callAddLike);
//        when(api.deleteLike(anyInt())).thenReturn(callDeleteLike);
        likes = new ArrayList<>();
        likes.add(new UserLike(1, "User 1", null));
        likes.add(new UserLike(2, "User 2", null));
        likes.add(new UserLike(3, "User 3", null));
    }

    /**
     * Test the behavior on loadLikes called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadLikesOnNullBundle() {
        presenter.loadLikes(null);
    }

    /**
     * Test the behavior when loadLikes is called with invalid bundle data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadLikesOnInvalidBundleData() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(new User());
        presenter.loadLikes(args);
    }

    /**
     * Test the behavior when loadLikes is called without bundle and the lineup is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadLikesWhenLineupNotSet() {
        presenter.loadLikes();
    }

    /**
     * Test the behavior when loadData is called and the view layout is not yet created.
     */
    @Test
    public void testLoadDataWhenViewLayoutIsNotYetCreated() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(api).likes(lineup.getId(), true, null, null);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        verify(fragment, never()).showLoading();
    }

    /**
     * Test the behavior when loadData is called and the view layout is created.
     */
    @Test
    public void testLoadDataWhenViewLayoutIsCreated() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.onViewCreated();
        presenter.loadLikes(args);
        verify(api).likes(lineup.getId(), true, null, null);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        verify(fragment).showLoading();
    }

    /**
     * Test the behavior when loading the likes is successful, the user has liked the lineup,
     * lineup data is not stored in the local database and error occurred while saving the lineup
     */
    @Test
    public void testLoadLikesSuccessWhenLikeExistsAndErrorOccurredWhileSavingTheLineup() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(false);
        doThrow(new LineupException("")).when(lineupDBService).store(any(Lineup.class));
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService).store(lineup);
        verify(lineupDBService).close();
        verify(likeDBService, never()).open();
        verify(likeDBService, never()).exists(user.getId(), lineup.getId());
        verify(likeDBService, never()).close();
        verify(fragment).showLoadingSuccess(likes);
        verify(fragment).showRemoveLikeButton();
    }

    /**
     * Test the behavior when loading the likes is successful, user has liked the lineup, lineup is
     * not saved on the local database, saving the lineup is successful.
     */
    @Test
    public void testLoadLikesSuccessWhenLikeExistsAndLineupSavedSuccessfully() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(false);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService).store(any(Lineup.class));
        verify(lineupDBService).close();
        verify(likeDBService).open();
        verify(likeDBService).exists(user.getId(), lineup.getId());
        verify(likeDBService).close();
        verify(fragment).showLoadingSuccess(likes);
        verify(fragment).showRemoveLikeButton();
    }

    /**
     * Test the behavior when loading the likes is successful, the user has liked the lineup and
     * lineup data is already saved.
     */
    @Test
    public void testLoadLikesSuccessWhenLikeExistsAndLineupIsAlreadySaved() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService, never()).store(any(Lineup.class));
        verify(lineupDBService).close();
        verify(likeDBService).open();
        verify(likeDBService).exists(user.getId(), lineup.getId());
        verify(likeDBService).close();
        verify(fragment).showLoadingSuccess(likes);
        verify(fragment).showRemoveLikeButton();
    }

    /**
     * Test the behavior when loading the likes is successful, the user has liked the lineup,
     * the liked is not saved in the local database and saving the like failed.
     */
    @Test
    public void testLoadLikesSuccessWhenLikeExistsAndLikeSavingFailed() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(likeDBService.exists(anyInt(), anyInt())).thenReturn(false);
        doThrow(new LikeException("")).when(likeDBService).store(any(LineupLike.class));
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));

        verify(likeDBService).open();
        verify(likeDBService).exists(user.getId(), lineup.getId());
        verify(likeDBService).store(any(LineupLike.class));
        verify(likeDBService).close();
        verify(fragment).showLoadingSuccess(likes);
        verify(fragment).showRemoveLikeButton();
    }

    /**
     * Test the behavior when loading the likes is successful, the user has liked the lineup,
     * the like is saved in the local database and saving the like is successful.
     */
    @Test
    public void testLoadLikesSuccessWhenLikeExistsAndSavingTheLikeIsSuccessful() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(likeDBService.exists(anyInt(), anyInt())).thenReturn(false);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));

        verify(likeDBService).open();
        verify(likeDBService).exists(user.getId(), lineup.getId());
        verify(likeDBService).store(any(LineupLike.class));
        verify(likeDBService).close();
        verify(fragment).showLoadingSuccess(likes);
        verify(fragment).showRemoveLikeButton();
    }

    /**
     * Test the behavior when loading the likes is successful, the user has liked the lineup, and
     * the like is already saved in the local database.
     */
    @Test
    public void testLoadLikesSuccessWhenLikeExistsAndLikeIsAlreadySaved() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(likeDBService.exists(anyInt(), anyInt())).thenReturn(true);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));

        verify(likeDBService).open();
        verify(likeDBService).exists(user.getId(), lineup.getId());
        verify(likeDBService, never()).store(any(LineupLike.class));
        verify(likeDBService).close();
        verify(fragment).showLoadingSuccess(likes);
        verify(fragment).showRemoveLikeButton();
    }

    /**
     * Test the behavior when loading the likes is successful and the user didn't
     * liked the lineup.
     */
    @Test
    public void testLoadLikesSuccessWhenUserDidNotLikedTheLineup() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        verify(fragment).showLoadingSuccess(likes);
        verify(fragment).showAddLikeButton();
        verify(likeDBService, never()).exists(anyInt(), anyInt());
    }

    /**
     * Test the behavior when loading the likes failed.
     */
    @Test
    public void testLoadingLikesFailed() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onFailure(callListUserLikes, new Throwable());
        verify(fragment).showLoadingFailed();
    }

    /**
     * Test the behavior when loadComments is called and a request to load the lineups is
     * not sending at the moment.
     */
    @Test
    public void testOnViewCreatedWhenLoadRequestIsNotSending() {
        presenter.onViewCreated();
        verify(fragment, never()).showLoading();
    }

    /**
     * Test the behavior when loadComments is called and a request to load the lineups is
     * sending at the moment.
     */
    @Test
    public void testOnViewCreatedWhenLoadRequestIsSending() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        presenter.onViewCreated();
        verify(fragment).showLoading();
    }

    /**
     * Test the behavior when addLike is called and the lineup is not yet set.
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
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
    }

    /**
     * Test the behavior when addLike is called and the user hasn't liked the lineup.
     */
    @Test
    public void testAddLike() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.addLike();
        verify(api).addLike(lineup.getId());
        verify(fragment).showLikeAdding();
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
    }

    /**
     * Test the behavior when adding the like is successful, the lineup is not saved in the local
     * database and storing the lineup failed.
     */
    @Test
    public void testAddLikeSuccessWhenLineupStoringFailed() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(false);
        doThrow(LineupException.class).when(lineupDBService).store(any(Lineup.class));
        presenter.loadLikes(args);
        presenter.addLike();
        verify(api).addLike(lineup.getId());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike, Response.success(new ServerResponse()));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService).store(lineup);
        verify(lineupDBService).close();
        verify(likeDBService, never()).open();
        verify(likeDBService, never()).exists(anyInt(), anyInt());
        verify(likeDBService, never()).close();
        verify(fragment).showLikeAddingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when adding the like is successful, the lineup is not saved in the local
     * database and storing the lineup is successful.
     */
    @Test
    public void testAddLikeSuccessWhenLineupStoringIsSuccessful() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(false);
        presenter.loadLikes(args);
        presenter.addLike();
        verify(api).addLike(lineup.getId());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike, Response.success(new ServerResponse()));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService).store(lineup);
        verify(lineupDBService).close();
        verify(likeDBService).open();
        verify(likeDBService).exists(anyInt(), anyInt());
        verify(likeDBService).close();
        verify(fragment).showLikeAddingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when adding the like is successful, and the lineup is already
     * saved in the database.
     */
    @Test
    public void testAddLikeSuccessWhenLineupAlreadySaved() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        presenter.loadLikes(args);
        presenter.addLike();
        verify(api).addLike(lineup.getId());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike, Response.success(new ServerResponse()));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService, never()).store(lineup);
        verify(lineupDBService).close();
        verify(likeDBService).open();
        verify(likeDBService).exists(anyInt(), anyInt());
        verify(likeDBService).close();
        verify(fragment).showLikeAddingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when adding the like is successful, the like is not saved in the local
     * database and saving the like failed.
     */
    @Test
    public void testAddLikeSuccessWhenLikeSavingFailed() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(likeDBService.exists(anyInt(), anyInt())).thenReturn(false);
        doThrow(new LikeException("")).when(likeDBService).store(any(LineupLike.class));
        presenter.loadLikes(args);
        presenter.addLike();
        verify(api).addLike(lineup.getId());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike, Response.success(new ServerResponse()));

        verify(likeDBService).open();
        verify(likeDBService).exists(anyInt(), anyInt());
        verify(likeDBService).store(any(LineupLike.class));
        verify(likeDBService).close();
        verify(fragment).showLikeAddingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when adding the like is successful, the like is not saved in the local
     * database and saving the like is successful.
     */
    @Test
    public void testAddLikeSuccessWhenLikeSavingIsSuccessful() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(likeDBService.exists(anyInt(), anyInt())).thenReturn(false);
        presenter.loadLikes(args);
        presenter.addLike();
        verify(api).addLike(lineup.getId());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike, Response.success(new ServerResponse()));

        verify(likeDBService).open();
        verify(likeDBService).exists(anyInt(), anyInt());
        verify(likeDBService).store(any(LineupLike.class));
        verify(likeDBService).close();
        verify(fragment).showLikeAddingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when adding the like is successful and the like is already saved.
     */
    @Test
    public void testAddLikeSuccessWhenLikeIsAlreadySaved() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(likeDBService.exists(anyInt(), anyInt())).thenReturn(true);
        presenter.loadLikes(args);
        presenter.addLike();
        verify(api).addLike(lineup.getId());
        verify(callAddLike).enqueue(callbackAddLikeCaptor.capture());
        callbackAddLikeCaptor.getValue().onResponse(callAddLike, Response.success(new ServerResponse()));

        verify(likeDBService).open();
        verify(likeDBService).exists(anyInt(), anyInt());
        verify(likeDBService, never()).store(any(LineupLike.class));
        verify(likeDBService).close();
        verify(fragment).showLikeAddingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when removeLike is called and the user didn't liked the lineup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveLikeWhenLineupIsNotYetSet() {
        presenter.removeLike();
    }

    /**
     * Test the behavior when remove like is called and the user didn't liked the lineup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveLikeWhenTheUserDidNotLikedTheLineup() {
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
    }

    /**
     * Test the behavior when removeLike is called and the user has liked the lineup.
     */
    @Test
    public void testRemoveLike() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
        verify(fragment).showLikeRemoving();
//        verify(api).deleteLike(lineup.getId());
//        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
    }

    /**
     * Test the behavior when removing the like is successful and deleting the like from
     * the database failed.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testLikeRemovingSuccessWhenLikeDeletingFailed() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        doThrow(new LikeException("")).when(likeDBService).delete(anyInt(), anyInt());
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
//        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        Void result = null;
//        callbackDeleteLikeCaptor.getValue().onResponse(callDeleteLike, Response.success(result));

        verify(likeDBService, times(2)).open();
        verify(likeDBService).delete(user.getId(), lineup.getId());
        verify(likeDBService, times(2)).close();
        verify(fragment).showLikeRemovingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when removing the like is successful and deleting the like from
     * the database is successful.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testLikeRemovingSuccessfulWhenLikeDeletingIsSuccessful() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
//        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
        Void result = null;
//        callbackDeleteLikeCaptor.getValue().onResponse(callDeleteLike, Response.success(result));

        verify(likeDBService, times(2)).open();
        verify(likeDBService).delete(user.getId(), lineup.getId());
        verify(likeDBService, times(2)).close();
        verify(fragment).showLikeRemovingSuccess(any(UserLike.class));
    }

    /**
     * Test the behavior when removing the like failed.
     */
    @Test
    public void testLikeRemovingFailed() {
        likes.add(new UserLike(user.getId(), user.getName(),
                new LineupLike(user, lineup, new Date())));
        when(args.getSerializable(LikeFragment.LINEUP_KEY)).thenReturn(lineup);
        presenter.loadLikes(args);
        verify(callListUserLikes).enqueue(callbackLikesCaptor.capture());
        callbackLikesCaptor.getValue().onResponse(callListUserLikes, Response.success(likes));
        presenter.removeLike();
//        verify(callDeleteLike).enqueue(callbackDeleteLikeCaptor.capture());
//        callbackDeleteLikeCaptor.getValue().onFailure(callDeleteLike, new Throwable());
        verify(fragment).showLikeRemovingFailed();
    }
}
