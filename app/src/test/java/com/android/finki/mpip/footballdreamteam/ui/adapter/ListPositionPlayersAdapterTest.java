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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
public class ListPositionPlayersAdapterTest {

    @Mock
    private PositionUtils utils;

    private ListPositionPlayersAdapter adapter;
    private int NUMBER_OF_PLAYERS = 20;
    private List<Player> players;
    private Calendar calendar = new GregorianCalendar(1991, 3, 11);
    private Date date = calendar.getTime();
    private final String positionShortName = "SP";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = RuntimeEnvironment.application.getBaseContext();
        players = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            Team team = new Team(i + 1, String.format("Team %d", i + 1));
            Position position = new Position(i + 1, String.format("Positions %d", i + 1));
            String name = String.format("Players %d", i + 1);
            players.add(new Player(i + 1, team, position, name, null, date));
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
        int index = 7;
        Player player = adapter.getItem(index);
        assertSame(players.get(index), player);
    }

    /**
     * Test that update method will merge the given list of items with the current one.
     */
    @Test
    public void testUpdate() {
        int id = players.get(players.size() - 1).getId();
        Player player1 = new Player(id, new Team(id, "Team"),
                new Position(id, "Position"), "Player", null, null);
        id++;
        Player player2 = new Player(id, new Team(id, "Team"),
                new Position(id, "Position"), "Player", null, null);
        List<Player> players = Arrays.asList(player1, player2);
        adapter.update(players);
        assertEquals(NUMBER_OF_PLAYERS + 2, adapter.getCount());
        assertSame(player1, adapter.getItem(NUMBER_OF_PLAYERS));
        assertSame(player2, adapter.getItem(NUMBER_OF_PLAYERS + 1));
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
        ListPositionPlayersAdapter.ViewHolder holder = adapter.new ViewHolder(recycledView);
        recycledView.setTag(holder);
        View view = adapter.getView(0, recycledView, null);
        assertSame(recycledView, view);
    }
}
