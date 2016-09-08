package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupDetailsActivityComponent;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.adapter.LineupsDetailsViewPagerAdapter;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupDetailsActivityPresenter;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 15.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class LineupDetailsActivityTest {

    @Mock
    private LineupDetailsActivityPresenter presenter;

    @Mock
    private LineupDetailsActivityComponent component;

    @Mock
    private LineupsDetailsViewPagerAdapter pagerAdapter;

    @Mock
    private LikeFragment likeFragment;

    @Mock
    private CommentsFragment commentFragment;

    private final int VIEW_PAGER_PAGES = 2;

    private ActivityController<LineupDetailsActivity> controller;
    private LineupDetailsActivity activity;

    private RelativeLayout spinner;
    private RelativeLayout errorContent;
    private LinearLayout mainContent;
    private ViewPager pager;

    private final int year = 2016, month = 8, day = 15, hour = 14, minute = 13, second = 25;
    private Calendar calendar = new GregorianCalendar(year, month, day, hour, minute, second);
    private String sDate = null;
    private Lineup lineup = null;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initMocks();
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setLineupDetailsActivityComponent(component);
        this.mockDependencies();
        controller = Robolectric.buildActivity(LineupDetailsActivity.class);
        activity = controller.create().start().resume().visible().get();

        spinner = (RelativeLayout) activity.findViewById(R.id.spinner);
        assertNotNull(spinner);
        errorContent = (RelativeLayout) activity.findViewById(R.id.error_loading);
        assertNotNull(errorContent);
        mainContent = (LinearLayout) activity.findViewById(R.id.lineupDetailsLayout_mainContent);
        assertNotNull(mainContent);
        pager = (ViewPager) activity.findViewById(R.id.lineupDetailsActivity_viewPager);
        assertNotNull(pager);

        Date createdAt = calendar.getTime();
        calendar.add(Calendar.MINUTE, 10);
        Date updatedAt = calendar.getTime();
        sDate = DateUtils.format(updatedAt);
        lineup = new Lineup(1, createdAt, updatedAt, 100, 124, new User(1, "Simple User"));
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Init the mocks to return specific values on method calls.
     */
    private void initMocks() {
        when(pagerAdapter.getCount()).thenReturn(VIEW_PAGER_PAGES);
        when(pagerAdapter.getItem(0)).thenReturn(likeFragment);
        when(pagerAdapter.getItem(1)).thenReturn(commentFragment);
    }

    /**
     * Mock the dependencies for the activity.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LineupDetailsActivity activity = (LineupDetailsActivity)
                        invocation.getArguments()[0];
                activity.presenter = presenter;
                activity.pagerAdapter = pagerAdapter;
                return null;
            }
        }).when(component).inject(any(LineupDetailsActivity.class));
    }

    /**
     * Test that the activity is successfully created.
     */
    @Test
    public void testActivityIsCreated() {
        String title = activity.getString(R.string.lineupDetailsActivity_title);
        assertNotNull(activity.getSupportActionBar());
        assertEquals(title, activity.getSupportActionBar().getTitle());
        assertNotNull(activity.presenter);
        verify(presenter).loadLineupData(activity.getIntent().getExtras());
    }

    /**
     * Test the behavior on activity when showLoading method is called.
     */
    @Test
    public void testShowLoading() {
        errorContent.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.VISIBLE);
        activity.showLoading();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(View.GONE, errorContent.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
    }

    /**
     * Test the behavior on the activity when showLoadingFailed method is called.
     */
    @Test
    public void testErrorLoading() {
        spinner.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.VISIBLE);
        activity.errorLoading();
        assertEquals(View.VISIBLE, errorContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
    }

    /**
     * Test the behavior on the activity when showLoadingSuccess method is called.
     */
    @Test
    public void testSuccessLoading() {
        spinner.setVisibility(View.VISIBLE);
        errorContent.setVisibility(View.VISIBLE);

        activity.successLoading(lineup);
        verify(pagerAdapter).setLineup(lineup);

        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, errorContent.getVisibility());

        TextView txtUser = (TextView) activity.findViewById(R.id.lineupDetailsLayout_user);
        assertNotNull(txtUser);
        assertEquals(String.format("by %s", lineup.getUser().getName()), txtUser.getText());
        assertNotNull(pager.getAdapter());
        assertTrue(pager.getAdapter() instanceof LineupsDetailsViewPagerAdapter);
        TabLayout tabLayout = (TabLayout) activity
                .findViewById(R.id.lineupDetailsActivity_tabLayout);
        assertNotNull(tabLayout);
        assertEquals(VIEW_PAGER_PAGES, tabLayout.getTabCount());
        verify(pagerAdapter, times(2)).getTabViewAt(anyInt());
    }

    /**
     * Test the behavior on the activity when back button is pressed while likes tab is active.
     */
    @Test
    public void testOnBackPressedWhenLikesTabIsActive() {
        activity.onBackPressed();
        assertTrue(shadowOf(activity).isFinishing());
    }

    /**
     * Test the behavior on the activity when the back button is pressed while the c
     * comments tab is active.
     */
    @Test
    public void testOnBackPressedWhenCommentsTabIsActive() {
        activity.setupViewPager(lineup);
        int currentItem = pager.getCurrentItem() + 1;
        pager.setCurrentItem(currentItem);

        activity.onBackPressed();
        assertFalse(shadowOf(activity).isFinishing());
        assertEquals(currentItem - 1, pager.getCurrentItem());
    }

    /**
     * Test the behavior on the activity when btn "Try Again" is clicked.
     */
    @Test
    public void testBtnTryAgainClick() {
        Button btn = (Button) activity.findViewById(R.id.error_loading_btn_tryAgain);
        assertNotNull(btn);
        btn.performClick();
        /**
         * We verify that the presenter is called two time because the presenter
         * is called once in the onCreate method and a error should occurred for the button
         * "Try Again" to be showed.
         */
        verify(presenter, times(2)).loadLineupData(activity.getIntent().getExtras());
    }

    /**
     * Test that when button "View Players" is clicked a new activity will be started.
     */
    @Test
    public void testBtnShowPlayersClick() {
        when(presenter.getLineup()).thenReturn(lineup);
        Button button = (Button) activity.findViewById(R.id.lineupDetailsLayout_btnShowPlayers);
        assertNotNull(button);
        button.performClick();
        Intent expectedIntent = new Intent(activity, LineupPlayersActivity.class);
        expectedIntent.putExtra(LineupPlayersActivity.LINEUP_BUNDLE_KEY, lineup);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(expectedIntent.filterEquals(actualIntent));
    }
}
