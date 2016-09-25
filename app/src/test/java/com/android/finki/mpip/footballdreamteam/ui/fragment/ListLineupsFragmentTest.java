package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListLineupsViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListLineupsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListLineupsViewPresenter;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 23.09.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class ListLineupsFragmentTest {

    @Mock
    private ListLineupsViewComponent component;

    @Mock
    private ListLineupsViewPresenter presenter;

    @Mock
    private static BaseFragment.Listener bfListener;

    @Mock
    private static ListLineupsFragment.Listener lfListener;

    private MockApplication application;
    private ListLineupsFragment fragment;
    private RelativeLayout spinner;
    private TextView txtSpinner;
    private RelativeLayout error;
    private TextView txtError;
    private RelativeLayout content;
    private ListView listView;
    private RelativeLayout btnLoadMore;
    private ProgressBar spinnerLoadMore;
    private List<Lineup> lineups = Arrays.asList(new Lineup(1, 1),
            new Lineup(2, 2), new Lineup(3, 3));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockDependencies();
        application = (MockApplication) RuntimeEnvironment.application;
        application.setListLineupsViewComponent(component);
        application.createAuthComponent();
        fragment = new ListLineupsFragment();
    }

    /**
     * Mock the dependencies for the fragment,
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ListLineupsFragment fragment = (ListLineupsFragment) invocation.getArguments()[0];
                fragment.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(ListLineupsFragment.class));
    }

    /**
     * Test that the fragment is created correctly.
     */
    @Test
    public void testFragmentIsCreated() {
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        verify(bfListener).onFragmentActive();
        verify(presenter).onViewCreated();
        verify(presenter).onViewLayoutCreated();
        verify(bfListener, never()).changeTitle(anyString());
        assertNotNull(fragment.getView());
        ListView listView = (ListView)
                fragment.getView().findViewById(R.id.listLineupsLayout_listView);
        assertNotNull(listView);
        assertTrue(listView.getAdapter() instanceof HeaderViewListAdapter);
        assertTrue(((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter()
                instanceof ListLineupsAdapter);
    }

    /**
     * Test that the fragment is created correctly when the fragment activity don't implements
     * BaseFragment Listener interface.
     */
    @Test
    public void testFragmentIsCreatedWhenActivityDoestNotImplementBaseFragmentListener() {
        SupportFragmentTestUtil.startFragment(fragment);
        verify(bfListener, never()).onFragmentActive();
        verify(presenter).onViewCreated();
        verify(presenter).onViewLayoutCreated();
        verify(bfListener, never()).changeTitle(anyString());
        assertNotNull(fragment.getView());
        ListView listView = (ListView)
                fragment.getView().findViewById(R.id.listLineupsLayout_listView);
        assertNotNull(listView);
        assertTrue(listView.getAdapter() instanceof HeaderViewListAdapter);
        assertTrue(((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter()
                instanceof ListLineupsAdapter);
    }

    /**
     * Get the main view from the fragment.
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
        content = (RelativeLayout) view.findViewById(R.id.listLineupsLayout_content);
        assertNotNull(component);
        listView = (ListView) view.findViewById(R.id.listLineupsLayout_listView);
        btnLoadMore = (RelativeLayout) view.findViewById(R.id.lineupsListView_btnLoadMore);
        assertNotNull(btnLoadMore);
        spinnerLoadMore = (ProgressBar) view.findViewById(R.id.lineupsListView_spinnerLoadMore);
        assertNotNull(spinnerLoadMore);
    }

    /**
     * Test the behavior when showLoading is called.
     */
    @Test
    public void testShowLoading() {
        String text = application.getString(R.string.listLineupsLayout_spinnerLoadingLineups_text);
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        spinner.setVisibility(View.GONE);
        txtSpinner.setText(null);
        error.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        fragment.showLoading();
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
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        spinnerLoadMore.setVisibility(View.VISIBLE);
        fragment.showLoadingSuccess(lineups);
        assertEquals(View.VISIBLE, content.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, spinnerLoadMore.getVisibility());
        Adapter adapter = ((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter();
        assertEquals(lineups.size(), adapter.getCount());
    }

    /**
     * Test the behavior when showLoadingFailed is called.
     */
    @Test
    public void testShowLoadingFailed() {
        String text = application.getString(R.string.listLineupLayout_loadingFailed_test);
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        txtError.setText(null);
        spinnerLoadMore.setVisibility(View.VISIBLE);
        fragment.showLoadingFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(View.GONE, spinnerLoadMore.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when button 'Try Again' is clicked.
     */
    @Test
    public void onBtnTryAgainClick() {
        SupportFragmentTestUtil.startFragment(fragment);
        View view = fragment.getView();
        assertNotNull(view);
        Button btn = (Button) view.findViewById(R.id.error_btnTryAgain);
        assertNotNull(btn);
        assertTrue(btn.performClick());
        verify(presenter).loadLineups(true);
    }

    /**
     * Test the behavior when showRefreshing is called.
     */
    @Test
    public void testShowRefreshing() {
        String text =
                application.getString(R.string.listLineupsLayout_spinnerRefreshingLineups_text);
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        spinner.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
        txtSpinner.setText(null);
        fragment.showRefreshing();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtSpinner.getText());
    }

    /**
     * Test the behavior when showRefreshingFailed is called.
     */
    @Test
    public void testShowRefreshingFailed() {
        String text = application.getString(R.string.listLineupLayout_refreshingFailed_text);
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        txtError.setText(null);
        fragment.showRefreshingFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when onLineupPlayersSelected is called and the activity implements the
     * fragment listener.
     */
    @Test
    public void testOnLineupPlayersSelected() {
        final Lineup lineup = lineups.get(0);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        fragment.onLineupPlayersSelected(lineup);
        verify(lfListener).showLineupPlayersView(lineup);
    }

    /**
     * Test the behavior when onLineupPlayersSelected is called and the activity doesn't implements
     * the fragment listener.
     */
    @Test
    public void testOnLineupPlayersSelectedWhenActivityDoesNotImplmeentsListener() {
        final Lineup lineup = lineups.get(0);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.onLineupPlayersSelected(lineup);
        verify(lfListener, never()).showLineupPlayersView(any(Lineup.class));
    }

    /**
     * Test the behavior when onLineupPLikesSelected is called and the activity implements the
     * fragment listener.
     */
    @Test
    public void testOnLineupLikesSelected() {
        final Lineup lineup = lineups.get(0);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        fragment.onLineupLikesSelected(lineup);
        verify(lfListener).showLineupLikesView(lineup);
    }

    /**
     * Test the behavior when onLineupLikesSelected is called and the activity doesn't implements
     * the fragment listener.
     */
    @Test
    public void testOnLineupLikesSelectedWhenActivityDoesNotImplmeentsListener() {
        final Lineup lineup = lineups.get(0);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.onLineupLikesSelected(lineup);
        verify(lfListener, never()).showLineupLikesView(any(Lineup.class));
    }

    /**
     * Test the behavior when onLineupCommentsSelected is called and the activity implements the
     * fragment listener.
     */
    @Test
    public void testOnLineupCommentsSelected() {
        final Lineup lineup = lineups.get(0);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        fragment.onLineupCommentsSelected(lineup);
        verify(lfListener).showLineupCommentsView(lineup);
    }

    /**
     * Test the behavior when onLineupCommentsSelected is called and the activity doesn't implements
     * the fragment listener.
     */
    @Test
    public void testOnLineupCommentsSelectedWhenActivityDoesNotImplmeentsListener() {
        final Lineup lineup = lineups.get(0);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.onLineupCommentsSelected(lineup);
        verify(lfListener, never()).showLineupCommentsView(any(Lineup.class));
    }

    /**
     * Test the behavior when button 'Load More' is clicked.
     */
    @Test
    public void testBtnLoadMoreClick() {
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        spinnerLoadMore.setVisibility(View.GONE);
        assertTrue(btnLoadMore.performClick());
        assertEquals(View.VISIBLE, spinnerLoadMore.getVisibility());
        verify(presenter).loadLineups(false);
    }

    /**
     * Test the behavior when showNoMoreLineups is called.
     */
    @Test
    public void testShowNoMoreLineups() {
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        btnLoadMore.setVisibility(View.VISIBLE);
        fragment.showNoMoreLineups();
        assertEquals(View.GONE, btnLoadMore.getVisibility());
    }

    /**
     * Test the behavior when showLineupDeletingSuccess is called.
     */
    @Test
    public void testShowLineupDeletingSuccess() {
        String text = application.getString(R.string.listLineupLayout_deletingSuccess_text);
        List<Lineup> lineups = new ArrayList<>(this.lineups);
        when(presenter.getLineups()).thenReturn(lineups);
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        Adapter adapter = ((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter();
        assertEquals(this.lineups.size(), adapter.getCount());
        fragment.showLineupDeletingSuccess(lineups.get(1));
        assertEquals(text, ShadowToast.getTextOfLatestToast());
        assertEquals(this.lineups.size() - 1, adapter.getCount());
    }

    /**
     * Test the behavior when showLineupDeletingFailed is called.
     */
    @Test
    public void testShowLineupDeletingFailed() {
        String text = application.getString(R.string.listLineupLayout_deletingFailed_text);
        final int index = 1;
        when(presenter.getLineups()).thenReturn(lineups);
        SupportFragmentTestUtil.startFragment(fragment);
        this.getViews();
        ListLineupsAdapter adapter = (ListLineupsAdapter)
                ((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter();
        View view = adapter.getView(index, null, listView);
        assertNotNull(view);
        ButtonAwesome btnDelete = (ButtonAwesome) view.findViewById(R.id.lineupItem_btnDelete);
        assertNotNull(btnDelete);
        assertTrue(btnDelete.performClick());
        assertTrue(adapter.isDeleting(lineups.get(index)));
        fragment.showLineupDeletingFailed(lineups.get(index));
        assertEquals(text, ShadowToast.getTextOfLatestToast());
        assertFalse(adapter.isDeleting(lineups.get(index)));
    }

    /**
     * Mock activity class for the fragment.
     */
    public static class MockActivity extends AppCompatActivity implements
            BaseFragment.Listener, ListLineupsFragment.Listener {

        @Override
        public void onFragmentActive() {
            bfListener.onFragmentActive();
        }

        @Override
        public void changeTitle(String title) {
            bfListener.changeTitle(title);
        }

        @Override
        public void showLineupPlayersView(Lineup lineup) {
            lfListener.showLineupPlayersView(lineup);
        }

        @Override
        public void showLineupLikesView(Lineup lineup) {
            lfListener.showLineupLikesView(lineup);
        }

        @Override
        public void showLineupCommentsView(Lineup lineup) {
            lfListener.showLineupCommentsView(lineup);
        }
    }
}
