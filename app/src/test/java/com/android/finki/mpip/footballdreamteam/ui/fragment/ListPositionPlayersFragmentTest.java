package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListPositionPlayersAdapter;
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
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Arrays;
import java.util.List;

import static com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragmentTest.TestActivity.ON_PLAYER_SELECTED_TOAST;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
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

    private ListPositionPlayersFragment fragment;
    private final PositionUtils.POSITION_PLACE place = PositionUtils.POSITION_PLACE.ATTACKERS;
    private final int[] playersToExclude = {1, 521 ,21, 10, 4, 45};
    private final List<Player> players = Arrays.asList(new Player(), new Player(), new Player());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockDependencies();
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setListPositionPlayersFragmentComponent(component);
    }

    /**
     * Mock the dependencies for the fragment.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ListPositionPlayersFragment fragment = (ListPositionPlayersFragment)
                        invocation.getArguments()[0];
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
        fragment = ListPositionPlayersFragment.newInstance(place, playersToExclude);
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertSame(place, args.getSerializable(ListPositionPlayersFragment.getPlaceKey()));
        assertSame(playersToExclude, args
                .getSerializable(ListPositionPlayersFragment.getExcludeLayersKey()));
        verify(presenter).onViewCreated(args);
        assertNotNull(fragment.getView());
        verify(presenter).onViewCreated();
    }

    /**
     * Test that setPositionPlace correctly sets the view text.
     */
    @Test
    public void testSetPositionPlace() {
        fragment = ListPositionPlayersFragment.newInstance(place, playersToExclude);
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        assertNotNull(fragment.getView());
        String text = "ListPositionPlayers";
        fragment.setPositionPlace(text);
        TextView txt = (TextView) fragment.getView()
                .findViewById(R.id.positionPlayersLayout_headerText);
        assertNotNull(txt);
        assertEquals(text, txt.getText());
    }

    /**
     * Test that setAdapter correctly sets the adapter for the list view.
     */
    @Test
    public void testSetAdapter() {
        fragment = ListPositionPlayersFragment.newInstance(place, playersToExclude);
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        assertNotNull(fragment.getView());
        fragment.setAdapter(players);
        ListView listView = (ListView) fragment.getView()
                .findViewById(R.id.positionPlayersLayout_listView);
        assertNotNull(listView);
        assertNotNull(listView.getAdapter());
        assertTrue(listView.getAdapter() instanceof ListPositionPlayersAdapter);
    }

    /**
     * Test the behavior when item from the list view is selected.
     */
    @Test
    public void testOnListItemSelected() {
        fragment = ListPositionPlayersFragment.newInstance(place, playersToExclude);
        SupportFragmentTestUtil.startVisibleFragment(fragment, TestActivity.class, 0);
        fragment.setAdapter(players);
        assertNotNull(fragment.getView());
        ListView listView = (ListView) fragment.getView()
                .findViewById(R.id.positionPlayersLayout_listView);
        ShadowListView shadow = shadowOf(listView);
        shadow.performItemClick(players.size() - 1);
        assertEquals(ON_PLAYER_SELECTED_TOAST, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test activity so we can test the methods from the fragment that calls a
     * method from the activity.
     */
    public static class TestActivity extends AppCompatActivity implements
                                            ListPositionPlayersFragment.Listener {
        static final String ON_PLAYER_SELECTED_TOAST = "On player Selected";

        @Override
        public void onPlayerSelected(Player player) {
            Toast.makeText(this, ON_PLAYER_SELECTED_TOAST, Toast.LENGTH_SHORT).show();
        }
    }
}
