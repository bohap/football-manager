package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.HomeViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListLineupsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.HomeViewPresenter;

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
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 09.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class HomeActivityTest {

    @Mock
    private HomeViewPresenter presenter;

    @Mock
    private HomeViewComponent component;

    private ActivityController<HomeActivity> controller;
    private HomeActivity activity;

    private RelativeLayout errorLoadingLayout;
    private RelativeLayout spinner;
    private TextView txtSpinner;
    private RelativeLayout lineupsListContent;
    private ListView lineupsListView;
    private ProgressBar lineupsLoadMoreSpinner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setHomeActivityComponent(component);
        this.mockDependencies();
        controller = Robolectric.buildActivity(HomeActivity.class);
        activity = controller.create().start().resume().visible().get();

        errorLoadingLayout = (RelativeLayout) activity.findViewById(R.id.error);
        assertNotNull(errorLoadingLayout);
        spinner = (RelativeLayout) activity.findViewById(R.id.spinner);
        assertNotNull(spinner);
        txtSpinner = (TextView) activity.findViewById(R.id.spinner_text);
        assertNotNull(txtSpinner);
        lineupsListContent = (RelativeLayout) activity.findViewById(R.id.homeLayout_mainContent);
        assertNotNull(lineupsListContent);
        lineupsListView = (ListView) activity.findViewById(R.id.listLineupsLayout_listVIew);
        assertNotNull(lineupsListView);
        lineupsLoadMoreSpinner = (ProgressBar) activity
                .findViewById(R.id.lineupsListVIew_spinnerLoadMore);
        assertNotNull(lineupsLoadMoreSpinner);
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Mock the dependencies for the activity.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                HomeActivity activity = (HomeActivity) invocation.getArguments()[0];
                activity.presenter = presenter;
                return null;
            }
        }).when(component).inject(any(HomeActivity.class));
    }

    /**
     * Test that the activity is successfully created.
     */
    @Test
    public void testActivityIsCreated() {
        String title = activity.getString(R.string.homeActivity_title);
        assertNotNull(activity.getSupportActionBar());
        assertEquals(title, activity.getSupportActionBar().getTitle());
        verify(presenter).loadData();
        assertTrue(lineupsListView.getAdapter() instanceof HeaderViewListAdapter);
        assertTrue(((HeaderViewListAdapter) lineupsListView.getAdapter()).getWrappedAdapter()
                instanceof ListLineupsAdapter);
        ShadowListView shadowListView = shadowOf(lineupsListView);
        List<View> footerViews = shadowListView.getFooterViews();
        assertEquals(1, footerViews.size());
        assertEquals(R.id.lineupsListView_btnLoadMore, footerViews.get(0).getId());
    }

    /**
     * Test the showInitialDataLoading method will show the spinner with initial data loading
     * message for loading initial data and hide all other content in the activity.
     */
    @Test
    public void testShowInitialDataLoading() {
        String text = activity.getString(R.string.homeActivity_spinnerInitialDataLoading_text);
        lineupsListContent.setVisibility(View.VISIBLE);
        errorLoadingLayout.setVisibility(View.VISIBLE);
        activity.showInitialDataLoading();
        assertEquals(View.VISIBLE, activity.spinner.getVisibility());
        assertEquals(text, txtSpinner.getText());
        assertEquals(View.GONE, errorLoadingLayout.getVisibility());
        assertEquals(View.GONE, lineupsListContent.getVisibility());
    }

    /**
     * Test that showInfoDialog method will start a new dialog AlertDialog.
     */
    @Test
    public void testShowInfoDialog() {
        activity.showInfoDialog();
        Dialog dialog = ShadowDialog.getLatestDialog();
        assertNotNull(dialog);
        assertTrue(dialog instanceof AlertDialog);
    }

    /**
     * Test the showLineupsLoading method will show the spinner with lineup loading message
     * and hide all other content in the activity.
     */
    @Test
    public void testShowLineupsLoading() {
        String text = activity.getString(R.string.listLineupsLayout_spinner_text);
        errorLoadingLayout.setVisibility(View.VISIBLE);
        lineupsListContent.setVisibility(View.VISIBLE);
        activity.showLineupsLoading();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(text, txtSpinner.getText());
        assertEquals(View.GONE, errorLoadingLayout.getVisibility());
        assertEquals(View.GONE, lineupsListContent.getVisibility());
        assertEquals(View.GONE, lineupsLoadMoreSpinner.getVisibility());
    }

    /**
     * Test the showErrLoading method will show the error content and hide all other content
     * in the activity.
     */
    @Test
    public void testShowErrorLoading() {
        spinner.setVisibility(View.VISIBLE);
        lineupsListContent.setVisibility(View.VISIBLE);
        activity.showErrorLoading();
        assertEquals(View.VISIBLE, errorLoadingLayout.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, lineupsListContent.getVisibility());
    }

    /**
     * Test the behavior on the activity when loading the lineups is successful.
     */
    @Test
    public void testSuccessLoadingLineups() {
        spinner.setVisibility(View.VISIBLE);
        errorLoadingLayout.setVisibility(View.VISIBLE);
        List<Lineup> lineups = Arrays.asList(new Lineup(1, 1),
                new Lineup(2, 2), new Lineup(3, 3));
        activity.successLoadingLineups(lineups);
        assertEquals(View.VISIBLE, lineupsListContent.getVisibility());
        assertEquals(View.GONE, errorLoadingLayout.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, lineupsLoadMoreSpinner.getVisibility());
        for (int i = 0; i < lineups.size(); i++) {
            assertSame(lineups.get(i), lineupsListView.getAdapter().getItem(i));
        }
    }

    /**
     * Test the behavior on the presenter when "Try Again" button in the error layout is clicked.
     */
    @Test
    public void testBtnTryAgainClick() {
        Button button = (Button) activity.findViewById(R.id.error_btnTryAgain);
        assertNotNull(button);
        button.performClick();
        /*
         * We addLike times two because the presenter is already called in the onCreate, and in the
         * first call a error should occurred for the error content and btn to be showed
         */
        verify(presenter, times(2)).loadData();
    }

    /**
     * Test the behavior on tbe activity when the btnLoadMore is clicked.
     */
    @Test
    public void testBtnLoadMoreClick() {
        lineupsListContent.setVisibility(View.VISIBLE);
        RelativeLayout btnLoadMore = (RelativeLayout) activity
                .findViewById(R.id.lineupsListView_btnLoadMore);
        assertNotNull(btnLoadMore);
        btnLoadMore.performClick();
        verify(presenter).loadMoreLineups();
        assertEquals(View.VISIBLE, lineupsLoadMoreSpinner.getVisibility());
        assertEquals(View.VISIBLE, lineupsListContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, errorLoadingLayout.getVisibility());
    }

    /**
     * Test tha behavior on the activity when a item from the lineups list is selected.
     */
    @Test
    public void testLineupsListItemSelected() {
        final int position = 1;
        List<Lineup> lineups = Arrays.asList(new Lineup(1, 1), new Lineup(1, 1));
        ListLineupsAdapter adapter = (ListLineupsAdapter) ((HeaderViewListAdapter)
                lineupsListView.getAdapter()).getWrappedAdapter();
        adapter.update(lineups);
        assertEquals(lineups.size(), adapter.getCount());
        ShadowListView shadowListView = shadowOf(lineupsListView);
        shadowListView.performItemClick(position);
        Intent expectedIntent = new Intent(activity, LineupDetailsActivity.class);
        expectedIntent.putExtra(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY,
                lineups.get(position).getId());
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }
}
