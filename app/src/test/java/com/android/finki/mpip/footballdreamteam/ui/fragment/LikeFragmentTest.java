package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LikeViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;
import com.android.finki.mpip.footballdreamteam.ui.adapter.LikesAdapter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LikeViewPresenter;

import org.junit.Before;
import org.junit.Ignore;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

/**
 * Created by Borce on 25.08.2016.
 */
@Ignore
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class LikeFragmentTest {

    @Mock
    private LikeViewComponent component;

    @Mock
    private LikeViewPresenter presenter;

    private LikeFragment fragment;
    private RelativeLayout spinner;
    private TextView txtSpinner;
    private RelativeLayout failedRequestLayout;
    private RelativeLayout mainContent;
    private final Lineup lineup = new Lineup(1, 1);
    private final int NUMBER_OF_LIKES = 3;
    private List<UserLike> likes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockDependencies();
        likes = new ArrayList<>();
        likes.add(new UserLike(1, "User 1", null));
        likes.add(new UserLike(2, "User 2", null));
        likes.add(new UserLike(3, "User 3", null));
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setLikeViewComponent(component);
    }

    /**
     * Mock the dependencies for the fragment.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LikeFragment fragment = (LikeFragment) invocation.getArguments()[0];
                fragment.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(LikeFragment.class));
    }

    /**
     * Test the behavior when newInstance is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOnNullLineup() {
        LikeFragment.newInstance(null);
    }

    /**
     * Test that the fragment is successfully created.
     */
    @Test
    public void testFragmentIsCrated() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertSame(lineup, args.getSerializable(LikeFragment.LINEUP_KEY));
        assertNotNull(fragment.getView());
//        verify(presenter).loadLikes(args);
//        verify(presenter).onViewCreated();
    }

    /**
     * Get the fragment views children.
     */
    private void initViews() {
        View view = fragment.getView();
        assertNotNull(view);
        spinner = (RelativeLayout) view.findViewById(R.id.spinner);
        assertNotNull(spinner);
        txtSpinner = (TextView) view.findViewById(R.id.spinner_text);
        assertNotNull(txtSpinner);
        failedRequestLayout = (RelativeLayout) view.findViewById(R.id.error);
        assertNotNull(failedRequestLayout);
        mainContent = (RelativeLayout) view.findViewById(R.id.likeLayout_mainContent);
        assertNotNull(mainContent);
    }

    /**
     * Test the behavior when showLoading is called.
     */
    @Test
    public void testShowLoading() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        this.initViews();
        String text = RuntimeEnvironment.application
                .getString(R.string.likesFragment_spinnerLoadingText);
        failedRequestLayout.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.VISIBLE);
        fragment.showLoading();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(text, txtSpinner.getText());
        assertEquals(View.GONE, failedRequestLayout.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
    }

    /**
     * Test the behavior when showLoadingFailed is called.
     */
    @Test
    public void testShowLoadingFailed() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        this.initViews();
        spinner.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.VISIBLE);
        fragment.showLoadingFailed();
        assertEquals(View.VISIBLE, failedRequestLayout.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
    }

    /**
     * Test the behavior when button "Try Again" is clicked.
     */
    @Test
    public void testBtnTryAgainClick() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        assertNotNull(fragment.getView());
        Button btn = (Button) fragment.getView().findViewById(R.id.error_btnTryAgain);
        btn.performClick();
        verify(presenter).loadLikes();
    }

    /**
     * Test the behavior when showLoadingSuccess is called.
     */
    @Test
    public void testShowLoadingSuccess() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        this.initViews();
        spinner.setVisibility(View.VISIBLE);
        failedRequestLayout.setVisibility(View.VISIBLE);
        fragment.showLoadingSuccess(likes);
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, failedRequestLayout.getVisibility());
        assertNotNull(fragment.getView());
        ListView listView = (ListView) fragment.getView().findViewById(R.id.likeLayout_listView);
        Adapter adapter = listView.getAdapter();
        assertTrue(adapter instanceof LikesAdapter);
        assertEquals(likes.size(), adapter.getCount());
    }

    /**
     * Test the behavior when showAddLikeButton is called.
     */
    @Test
    public void testShowAddLikeButton() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.showAddLikeButton();
        assertNotNull(fragment.getView());
        RelativeLayout btnAddLike = (RelativeLayout)
                fragment.getView().findViewById(R.id.likeLayout_btnAddLike);
        assertNotNull(btnAddLike);
        assertEquals(View.VISIBLE, btnAddLike.getVisibility());
        RelativeLayout btnRemoveLike = (RelativeLayout)
                fragment.getView().findViewById(R.id.likeLayout_btnRemoveLike);
        assertNotNull(btnRemoveLike);
        assertEquals(View.GONE, btnRemoveLike.getVisibility());
    }

    /**
     * Test the behavior when showRemoveLikeButton is called.
     */
    @Test
    public void testShowRemoveLikeButton() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.showRemoveLikeButton();
        assertNotNull(fragment.getView());
        RelativeLayout btnRemoveLike = (RelativeLayout)
                fragment.getView().findViewById(R.id.likeLayout_btnRemoveLike);
        assertNotNull(btnRemoveLike);
        assertEquals(View.VISIBLE, btnRemoveLike.getVisibility());
        RelativeLayout btnAddLike = (RelativeLayout)
                fragment.getView().findViewById(R.id.likeLayout_btnAddLike);
        assertNotNull(btnAddLike);
        assertEquals(View.GONE, btnAddLike.getVisibility());
    }

    /**
     * Test the behavior when button "Add Like" is clicked.
     */
    @Test
    public void testBtnAddLikeClick() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        assertNotNull(fragment.getView());
        RelativeLayout btn = (RelativeLayout) fragment.getView().findViewById(R.id.likeLayout_btnAddLike);
        btn.performClick();
        verify(presenter).addLike();
    }

    /**
     * Test the behavior when showLikeAdding is called.
     */
    @Test
    public void testShowLikeAdding() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.showLikeAdding();
        assertNotNull(fragment.getView());
        assertEquals(View.VISIBLE, fragment.getView()
                .findViewById(R.id.spinnerLikeAdding).getVisibility());
    }

    /**
     * Test the behavior when showLikeAddingSuccess is called.
     */
    @Test
    public void testShowLikeAddingSuccess() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.showLoadingSuccess(likes);
        fragment.showLikeAddingSuccess(new UserLike());
        assertNotNull(fragment.getView());
        assertEquals(View.GONE, fragment.getView()
                .findViewById(R.id.spinnerLikeAdding).getVisibility());
        assertEquals(View.VISIBLE, fragment.getView()
                .findViewById(R.id.likeLayout_btnRemoveLike).getVisibility());
        assertEquals(View.GONE, fragment.getView()
                .findViewById(R.id.likeLayout_btnAddLike).getVisibility());
        ListView listView = (ListView) fragment.getView().findViewById(R.id.likeLayout_listView);
        assertNotNull(listView);
        assertEquals(NUMBER_OF_LIKES + 1, listView.getAdapter().getCount());
    }

    /**
     * Test the behavior when showLikeAddingFailed is called.
     */
    @Test
    public void testShowLikeAddingFailed() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.showLikeAddingFailed();
        assertNotNull(fragment.getView());
        assertEquals(View.GONE, fragment.getView()
                .findViewById(R.id.spinnerLikeAdding).getVisibility());
        String text = RuntimeEnvironment.application
                .getString(R.string.likesFragment_likeAddingFailed_text);
        assertEquals(text, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test the behavior when button "Remove Like" is clicked.
     */
    @Test
    public void testBtnRemoveLikeClick() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        assertNotNull(fragment.getView());
        RelativeLayout btn = (RelativeLayout) fragment.getView().findViewById(R.id.likeLayout_btnRemoveLike);
        btn.performClick();
        verify(presenter).removeLike();
    }

    /**
     * Test the behavior when showLikeRemoving is called.
     */
    @Test
    public void testShowLikeRemoving() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.showLikeRemoving();
        assertNotNull(fragment.getView());
        assertEquals(View.VISIBLE, fragment.getView()
                .findViewById(R.id.spinnerLikeRemoving).getVisibility());
    }

    /**
     * Test the behavior when showLikeRemovingSuccess is called.
     */
    @Test
    public void testShowLikeRemovingSuccess() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.showLoadingSuccess(likes);
        fragment.showLikeRemovingSuccess(likes.get(1));
        assertNotNull(fragment.getView());
        assertEquals(View.GONE, fragment.getView()
                .findViewById(R.id.spinnerLikeRemoving).getVisibility());
        assertEquals(View.GONE, fragment.getView()
                .findViewById(R.id.likeLayout_btnRemoveLike).getVisibility());
        assertEquals(View.VISIBLE, fragment.getView()
                .findViewById(R.id.likeLayout_btnAddLike).getVisibility());
        ListView listView = (ListView) fragment.getView().findViewById(R.id.likeLayout_listView);
        assertEquals(NUMBER_OF_LIKES - 1, listView.getAdapter().getCount());
    }

    /**
     * Test the behavior when showLikeRemovingFailed is called.
     */
    @Test
    public void testShowLikeRemovingFailed() {
        fragment = LikeFragment.newInstance(lineup);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.showLikeRemovingFailed();
        assertNotNull(fragment.getView());
        assertEquals(View.GONE, fragment.getView()
                .findViewById(R.id.spinnerLikeRemoving).getVisibility());
        String text = RuntimeEnvironment.application
                .getString(R.string.likesFragment_likeRemovingFailed_text);
        assertEquals(text, ShadowToast.getTextOfLatestToast());
    }
}