package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by Borce on 25.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class LikeFragmentTest {

    @Mock
    private LikeViewComponent component;

    @Mock
    private LikeViewPresenter presenter;

    @Mock
    private static BaseFragment.Listener baseFragmentListener;

    private MockApplication application;
    private LikeFragment fragment;
    private RelativeLayout spinner;
    private TextView txtSpinner;
    private RelativeLayout error;
    private RelativeLayout content;
    private ListView listView;
    private RelativeLayout btnAddLike;
    private RelativeLayout btnRemoveLike;
    private ProgressBar spinnerAddLike;
    private ProgressBar spinnerRemoveLike;
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
        application = (MockApplication) RuntimeEnvironment.application;
        application.setLikeViewComponent(component);
        application.createAuthComponent();
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
     * Start a new instance of the LikeFragment.
     *
     * @param aClass fragment activity class, if null is passed default class will be used
     */
    private <T extends AppCompatActivity> void startFragment(Class<T> aClass) {
        fragment = LikeFragment.newInstance(lineup);
        if (aClass != null) {
            SupportFragmentTestUtil.startFragment(fragment, aClass);
        } else {
            SupportFragmentTestUtil.startFragment(fragment);
        }
    }

    /**
     * Test that the fragment is successfully created.
     */
    @Test
    public void testFragmentIsCrated() {
        this.startFragment(MockActivity.class);
        String title = application.getString(R.string.likesFragment_title);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertSame(lineup, args.getSerializable(LikeFragment.LINEUP_KEY));
        verify(baseFragmentListener).onFragmentActive();
        verify(presenter).onViewCreated(args);
        assertNotNull(fragment.getView());
        verify(baseFragmentListener).changeTitle(title);
        verify(presenter).onViewLayoutCreated();
        listView = (ListView) fragment.getView().findViewById(R.id.likeLayout_listView);
        assertNotNull(listView);
        Adapter adapter = listView.getAdapter();
        assertTrue(adapter instanceof LikesAdapter);
    }

    /**
     * Test the behavior when the fragment activity don't implements BaseFragment
     * Listener interface.
     */
    @Test
    public void testFragmentIsCreatedWhenActivityDoesNotImplementTheListener() {
        this.startFragment(null);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertSame(lineup, args.getSerializable(LikeFragment.LINEUP_KEY));
        assertNotNull(fragment.getView());
        verify(presenter).onViewCreated(args);
        verify(presenter).onViewLayoutCreated();
        verify(baseFragmentListener, never()).onFragmentActive();
        verify(baseFragmentListener, never()).changeTitle(anyString());
    }

    /**
     * Get the fragment views children.
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
        content = (RelativeLayout) view.findViewById(R.id.likeLayout_mainContent);
        assertNotNull(content);
        listView = (ListView) fragment.getView().findViewById(R.id.likeLayout_listView);
        assertNotNull(listView);
        btnAddLike = (RelativeLayout) view.findViewById(R.id.likeLayout_btnAddLike);
        assertNotNull(btnAddLike);
        btnRemoveLike = (RelativeLayout) view.findViewById(R.id.likeLayout_btnRemoveLike);
        assertNotNull(btnRemoveLike);
        spinnerAddLike = (ProgressBar) view.findViewById(R.id.spinnerLikeAdding);
        assertNotNull(spinnerAddLike);
        spinnerRemoveLike = (ProgressBar) view.findViewById(R.id.spinnerLikeRemoving);
        assertNotNull(spinnerRemoveLike);
    }

    /**
     * Test the behavior when showLoading is called.
     */
    @Test
    public void testShowLoading() {
        this.startFragment(null);
        this.getViews();
        String text = application.getString(R.string.likesFragment_spinnerLoadingText);
        spinner.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        txtSpinner.setText(null);
        fragment.showLoading();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(text, txtSpinner.getText());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
    }

    /**
     * Test the behavior when showLoadingSuccess is called.
     */
    @Test
    public void testShowLoadingSuccess() {
        this.startFragment(null);
        this.getViews();
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        fragment.showLoadingSuccess(likes);
        assertEquals(View.VISIBLE, content.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        View view = fragment.getView();
        assertNotNull(view);
        Adapter adapter = listView.getAdapter();
        assertTrue(adapter instanceof LikesAdapter);
        assertEquals(likes.size(), adapter.getCount());
    }

    /**
     * Test the behavior when showLoadingFailed is called.
     */
    @Test
    public void testShowLoadingFailed() {
        this.startFragment(null);
        this.getViews();
        error.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        fragment.showLoadingFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
    }

    /**
     * Test the behavior when button "Try Again" is clicked.
     */
    @Test
    public void testBtnTryAgainClick() {
        this.startFragment(null);
        assertNotNull(fragment.getView());
        Button btn = (Button) fragment.getView().findViewById(R.id.error_btnTryAgain);
        assertNotNull(btn);
        assertTrue(btn.performClick());
        verify(presenter).loadLikes();
    }

    /**
     * Test the behavior when showAddLikeButton is called.
     */
    @Test
    public void testShowAddLikeButton() {
        this.startFragment(null);
        this.getViews();
        btnAddLike.setVisibility(View.GONE);
        btnRemoveLike.setVisibility(View.VISIBLE);
        fragment.showAddLikeButton();
        assertEquals(View.VISIBLE, btnAddLike.getVisibility());
        assertEquals(View.GONE, btnRemoveLike.getVisibility());
    }

    /**
     * Test the behavior when showRemoveLikeButton is called.
     */
    @Test
    public void testShowRemoveLikeButton() {
        this.startFragment(null);
        this.getViews();
        btnRemoveLike.setVisibility(View.GONE);
        btnAddLike.setVisibility(View.VISIBLE);
        fragment.showRemoveLikeButton();
        assertEquals(View.VISIBLE, btnRemoveLike.getVisibility());
        assertEquals(View.GONE, btnAddLike.getVisibility());
    }

    /**
     * Test the behavior when button "Add Like" is clicked.
     */
    @Test
    public void testBtnAddLikeClick() {
        this.startFragment(null);
        this.getViews();
        assertTrue(btnAddLike.performClick());
        verify(presenter).addLike();
    }

    /**
     * Test the behavior when showLikeAdding is called.
     */
    @Test
    public void testShowLikeAdding() {
        this.startFragment(null);
        this.getViews();
        spinnerAddLike.setVisibility(View.GONE);
        fragment.showLikeAdding();
        assertEquals(View.VISIBLE, spinnerAddLike.getVisibility());
    }

    /**
     * Test the behavior when showLikeAddingSuccess is called.
     */
    @Test
    public void testShowLikeAddingSuccess() {
        this.startFragment(null);
        this.getViews();
        spinnerAddLike.setVisibility(View.VISIBLE);
        btnAddLike.setVisibility(View.VISIBLE);
        btnRemoveLike.setVisibility(View.GONE);
        fragment.showLoadingSuccess(likes);
        fragment.showLikeAddingSuccess(new UserLike());
        assertEquals(View.GONE, spinnerAddLike.getVisibility());
        assertEquals(View.VISIBLE, btnRemoveLike.getVisibility());
        assertEquals(View.GONE, btnAddLike.getVisibility());
        assertEquals(NUMBER_OF_LIKES + 1, listView.getAdapter().getCount());
    }

    /**
     * Test the behavior when showLikeAddingFailed is called.
     */
    @Test
    public void testShowLikeAddingFailed() {
        this.startFragment(null);
        this.getViews();
        spinnerAddLike.setVisibility(View.VISIBLE);
        fragment.showLikeAddingFailed();
        assertEquals(View.GONE, spinnerAddLike.getVisibility());
        String text = application.getString(R.string.likesFragment_likeAddingFailed_text);
        assertEquals(text, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test the behavior when button "Remove Like" is clicked.
     */
    @Test
    public void testBtnRemoveLikeClick() {
        this.startFragment(null);
        this.getViews();
        assertTrue(btnRemoveLike.performClick());
        verify(presenter).removeLike();
    }

    /**
     * Test the behavior when showLikeRemoving is called.
     */
    @Test
    public void testShowLikeRemoving() {
        this.startFragment(null);
        this.getViews();
        spinnerRemoveLike.setVisibility(View.GONE);
        fragment.showLikeRemoving();
        assertEquals(View.VISIBLE, spinnerRemoveLike.getVisibility());
    }

    /**
     * Test the behavior when showLikeRemovingSuccess is called.
     */
    @Test
    public void testShowLikeRemovingSuccess() {
        this.startFragment(null);
        this.getViews();
        spinnerRemoveLike.setVisibility(View.VISIBLE);
        btnRemoveLike.setVisibility(View.VISIBLE);
        btnAddLike.setVisibility(View.VISIBLE);
        fragment.showLoadingSuccess(likes);
        fragment.showLikeRemovingSuccess(likes.get(1));
        assertEquals(View.GONE, spinnerRemoveLike.getVisibility());
        assertEquals(View.GONE, btnRemoveLike.getVisibility());
        assertEquals(View.VISIBLE, btnAddLike.getVisibility());
        assertEquals(NUMBER_OF_LIKES - 1, listView.getAdapter().getCount());
    }

    /**
     * Test the behavior when showLikeRemovingFailed is called.
     */
    @Test
    public void testShowLikeRemovingFailed() {
        this.startFragment(null);
        this.getViews();
        spinnerRemoveLike.setVisibility(View.VISIBLE);
        fragment.showLikeRemovingFailed();
        assertNotNull(fragment.getView());
        assertEquals(View.GONE, spinnerRemoveLike.getVisibility());
        String text = application.getString(R.string.likesFragment_likeRemovingFailed_text);
        assertEquals(text, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Mock activity class for the fragment.
     */
    public static class MockActivity extends AppCompatActivity
            implements BaseFragment.Listener {

        @Override
        public void onFragmentActive() {
            baseFragmentListener.onFragmentActive();
        }

        @Override
        public void changeTitle(String title) {
            baseFragmentListener.changeTitle(title);
        }
    }
}