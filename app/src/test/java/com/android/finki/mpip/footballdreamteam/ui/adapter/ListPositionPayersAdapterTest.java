package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 19.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ListPositionPayersAdapterTest {

    @Mock
    private PositionUtils utils;

    private ListPositionPlayersAdapter adapter;
    private int NUMBER_OF_PLAYERS = 11;
    private List<Player> players;
    private final int year = 1991, month = 3, day = 11;
    private Calendar calendar = new GregorianCalendar(year, month, day);
    private final String positionShortName = "Position";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = RuntimeEnvironment.application.getBaseContext();
        players = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players.add(new Player(i + 1, new Team(i, "Team " + i), new Position(),
                    "Player " + (i + 1), null, calendar.getTime(), 0));
        }
        adapter = new ListPositionPlayersAdapter(context, players, utils);
    }

    /**
     * Test getCount method works.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_PLAYERS, adapter.getCount());
    }

    /**
     * Test the behavior on getItem called with un existing position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetItemOnUnExistingPosition() {
        adapter.getItem(NUMBER_OF_PLAYERS + 1);
    }

    /**
     * Test that getItem method works.
     */
    @Test
    public void testGetItem() {
        int position = 7;
        Player player = adapter.getItem(position);
        assertSame(players.get(position), player);
    }

    /**
     * Test the getView returns the correctly mapped view.
     */
    @Test
    public void testGetView() {
        when(utils.getShortName(any(Position.class))).thenReturn(positionShortName);
        int position = 2;
        View view = adapter.getView(position, null, null);
        assertNotNull(view);
        TextView txtName = (TextView) view.findViewById(R.id.positionPlayerLayout_txtName);
        assertNotNull(txtName);
        assertEquals(players.get(position).getName(), txtName.getText());
        TextView txtAge = (TextView) view.findViewById(R.id.positionPlayerLayout_txtAge);
        assertNotNull(txtAge);
        assertEquals(String.valueOf(DateUtils.getYearDiff(calendar.getTime())), txtAge.getText());
        TextView txtTeam = (TextView) view.findViewById(R.id.positionPlayerLayout_txtTeam);
        assertNotNull(txtTeam);
        assertEquals(players.get(position).getTeam().getName(), txtTeam.getText());
        TextView txtPosition = (TextView) view.findViewById(R.id.positionPlayerLayout_txtPosition);
        assertNotNull(txtPosition);
        assertEquals(positionShortName, txtPosition.getText());
    }

    /**
     * Test that getVIew return the correctly mapped view when the player team is null.
     */
    @Test
    public void testGetViewOnNullPlayerTeam() {
        when(utils.getShortName(any(Position.class))).thenReturn(positionShortName);
        int position = 7;
        players.get(position).setTeam(null);
        View view = adapter.getView(position, null, null);
        assertNotNull(view);
        TextView txtTeam = (TextView) view.findViewById(R.id.positionPlayerLayout_txtTeam);
        assertNotNull(txtTeam);
        assertEquals("", txtTeam.getText());
    }

    /**
     * Test that getVIew return the correctly mapped view when the player position is null.
     */
    @Test
    public void testGetViewOnNullPlayerPosition() {
        when(utils.getShortName(any(Position.class))).thenReturn(positionShortName);
        int position = 1;
        players.get(position).setPosition(null);
        View view = adapter.getView(position, null, null);
        assertNotNull(view);
        TextView txtPosition = (TextView) view.findViewById(R.id.positionPlayerLayout_txtPosition);
        assertNotNull(txtPosition);
        assertEquals("", txtPosition.getText());
    }

    /**
     * Test that getVIew return the correctly mapped view when the player position can't be found..
     */
    @Test
    public void testGetViewOnUnExistingPlayerPosition() {
        when(utils.getShortName(any(Position.class))).thenReturn(null);
        int position = 1;
        View view = adapter.getView(position, null, null);
        assertNotNull(view);
        TextView txtPosition = (TextView) view.findViewById(R.id.positionPlayerLayout_txtPosition);
        assertNotNull(txtPosition);
        assertEquals("", txtPosition.getText());
    }

    /**
     * Test that getView not creates a new view when the passed view is not null.
     */
    @Test
    public void testGetViewOnRecycledView() {
        View recycledView = View.inflate(RuntimeEnvironment.application,
                R.layout.position_players_list_item, null);
        ListPositionPlayersAdapter.ViewHolder holder =
                new ListPositionPlayersAdapter.ViewHolder(recycledView);
        recycledView.setTag(holder);
        View view = adapter.getView(0, recycledView, null);
        assertSame(recycledView, view);
    }
}
