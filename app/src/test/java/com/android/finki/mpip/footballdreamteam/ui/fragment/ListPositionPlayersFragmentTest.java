package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListPositionPlayersAdapter;
import com.android.finki.mpip.footballdreamteam.ui.component.ListPositionPlayersView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

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
import org.robolectric.shadows.ShadowListView;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 20.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class ListPositionPlayersFragmentTest {

    @Mock
    private ListPositionPlayersViewPresenter presenter;

    @Mock
    private ListPositionPlayersViewComponent component;

    @Mock
    private static BaseFragment.Listener bfListener;

    @Mock
    private static ListPositionPlayersFragment.Listener lpfListener;

    private ListPositionPlayersFragment fragment;
    private final PositionUtils.POSITION_PLACE place = PositionUtils.POSITION_PLACE.ATTACKERS;
    private final int[] playersToExclude = {1, 521, 21, 10, 4, 45};
    private final List<Player> players = Arrays.asList(new Player(), new Player(), new Player());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockDependencies();
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setListPositionPlayersViewComponent(component);
        application.createAuthComponent();
    }

    /**
     * Mock the dependencies for the fragment.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ListPositionPlayersFragment fragment =
                        (ListPositionPlayersFragment) invocation.getArguments()[0];
                fragment.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(ListPositionPlayersFragment.class));
    }

    /**
     * Test the behavior on newInstance called with null position place.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOnNullPositionPlace() {
        ListPositionPlayersFragment.newInstance(null, playersToExclude);
    }

    /**
     * Test the behavior on newInstance called with null players to exclude.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullPlayersToExclude() {
        ListPositionPlayersFragment.newInstance(place, null);
    }

    /**
     * Test that fragment is successfully created.
     */
    @Test
    public void testFragmentIsCreated() {
        final int startX = 10;
        final int startY = 123;
        fragment = ListPositionPlayersFragment
                .newInstance(place, playersToExclude, startX, startY);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertSame(place, args.getSerializable(ListPositionPlayersView.PLACE_KEY));
        assertSame(playersToExclude,
                args.getSerializable(ListPositionPlayersFragment.EXCLUDE_LAYERS_KEY));
        assertEquals(startX, args.getFloat(ListPositionPlayersView.START_X_KEY, -1), 0.1);
        assertEquals(startY, args.getFloat(ListPositionPlayersView.START_Y_KEY, -1), 0.1);
        verify(bfListener).onFragmentActive();
        verify(bfListener, never()).changeTitle(anyString());
        verify(presenter).onViewCreated(args);
        verify(presenter).onViewLayoutCreated();
        assertNotNull(fragment.getView());
        ListView listView = (ListView) fragment.getView()
                .findViewById(R.id.positionPlayersLayout_listView);
        assertNotNull(listView);
        assertTrue(listView.getAdapter() instanceof ListPositionPlayersAdapter);
        assertEquals(0, listView.getAdapter().getCount());
    }

    /**
     * Test that fragment is successfully created when the activity doesn't implements
     * BaseFragment.Listener.
     */
    @Test
    public void testFragmentIsCreatedWhenActivityDoesNotImplementsListener() {
        fragment = ListPositionPlayersFragment.newInstance(place, playersToExclude);
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertSame(place, args.getSerializable(ListPositionPlayersView.PLACE_KEY));
        assertSame(playersToExclude,
                args.getSerializable(ListPositionPlayersFragment.EXCLUDE_LAYERS_KEY));
        assertEquals(0, args.getFloat(ListPositionPlayersView.START_X_KEY, -1), 0.1);
        assertEquals(0, args.getFloat(ListPositionPlayersView.START_Y_KEY, -1), 0.1);
        verify(bfListener, never()).onFragmentActive();
        verify(bfListener, never()).changeTitle(anyString());
        verify(presenter).onViewCreated(args);
        verify(presenter).onViewLayoutCreated();
        assertNotNull(fragment.getView());
        ListView listView = (ListView) fragment.getView()
                .findViewById(R.id.positionPlayersLayout_listView);
        assertNotNull(listView);
        assertTrue(listView.getAdapter() instanceof ListPositionPlayersAdapter);
        assertEquals(0, listView.getAdapter().getCount());
    }

    /**
     * Test the behavior when onPlayersLoaded is called.
     */
    @Test
    public void testOnPlayersLoaded() {
        fragment = ListPositionPlayersFragment.newInstance(place, playersToExclude);
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        View view = fragment.getView();
        assertNotNull(view);
        TextView txtHeader = (TextView) view.findViewById(R.id.positionPlayersLayout_headerText);
        assertNotNull(txtHeader);
        ListView listView = (ListView) view.findViewById(R.id.positionPlayersLayout_listView);
        assertNotNull(listView);
        assertTrue(listView.getAdapter() instanceof ListPositionPlayersAdapter);
        ListPositionPlayersAdapter adapter = (ListPositionPlayersAdapter) listView.getAdapter();
        txtHeader.setText(null);
        assertEquals(0, adapter.getCount());
        fragment.onPlayersLoaded(players, place.name());
        assertEquals(place.name(), txtHeader.getText());
        assertEquals(players.size(), adapter.getCount());
    }

    /**
     * Test the behavior when item from the list view is selected.
     */
    @Test
    public void testOnListItemSelected() {
        final int index = players.size() - 1;
        fragment = ListPositionPlayersFragment.newInstance(place, playersToExclude);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        fragment.onPlayersLoaded(players, place.name());
        assertNotNull(fragment.getView());
        ListView listView = (ListView) fragment.getView()
                .findViewById(R.id.positionPlayersLayout_listView);
        ShadowListView shadow = shadowOf(listView);
        shadow.performItemClick(index);
        verify(lpfListener).onPlayerSelected(players.get(index));
    }

    /**
     * Mock activity class for the fragment.
     */
    public static class MockActivity extends AppCompatActivity implements BaseFragment.Listener,
            ListPositionPlayersFragment.Listener {

        @Override
        public void onFragmentActive() {
            bfListener.onFragmentActive();
        }

        @Override
        public void changeTitle(String title) {
            bfListener.changeTitle(title);
        }

        @Override
        public void onPlayerSelected(Player player) {
            lpfListener.onPlayerSelected(player);
        }
    }
}
