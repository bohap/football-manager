package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowBaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 13.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ListLineupsAdapterTest {

    @Mock
    private ListLineupsAdapter.Listener listener;

    private Context context;
    private ListLineupsAdapter adapter;
    private ShadowBaseAdapter shadow;

    private Calendar calendar = new GregorianCalendar(2016, 7, 13, 19, 7, 20);
    private Date date = calendar.getTime();
    private String sDate = DateUtils.dayNameFormat(calendar.getTime());
    private User authUser = new User(1, "Auth User");
    private User user1 = new User(2, "User 1");
    private User user2 = new User(3, "User 2");
    private User user3 = new User(4, "User 3");
    private final int NUMBER_OF_LINEUPS = 6;
    private Lineup authUserLineup1 = new Lineup(1, authUser, date, date);
    private Lineup lineup1 = new Lineup(2, user1, date, date);
    private Lineup lineup2 = new Lineup(3, user2, date, date);
    private Lineup authUserLineup2 = new Lineup(4, authUser, date, date);
    private Lineup lineup3 = new Lineup(5, user3, date, date);
    private Lineup lineup4 = new Lineup(6, user1, date, date);
    private Lineup unExistingLineup = new Lineup(7, user2, date, date);
    private List<Lineup> lineups = Arrays.asList(authUserLineup1, lineup1, lineup2,
            authUserLineup2, lineup3, lineup4);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        context = RuntimeEnvironment.application.getBaseContext();
        adapter = new ListLineupsAdapter(context, authUser, listener);
        shadow = shadowOf(adapter);
        adapter.add(authUserLineup1);
        adapter.add(lineup1);
        adapter.add(lineup2);
        adapter.add(authUserLineup2);
        adapter.add(lineup3);
        adapter.add(lineup4);
    }

    /**
     * Test getCount method works.
     */
    @Test
    public void testGetCount() {
        assertEquals(NUMBER_OF_LINEUPS, adapter.getCount());
    }

    /**
     * Test the getItem method works.
     */
    @Test
    public void testGetItem() {
        int index = 3;
        Lineup lineup = adapter.getItem(index);
        assertNotNull(lineup);
        assertSame(lineups.get(index), lineup);
    }

    /**
     * Test the behavior on getItem called with un existing position in the list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetItemOnUnExistingPosition() {
        adapter.getItem(NUMBER_OF_LINEUPS + 1);
    }

    /**
     * Test the addLike method works.
     */
    @Test
    public void testAdd() {
        adapter.add(unExistingLineup);
        assertEquals(NUMBER_OF_LINEUPS + 1, adapter.getCount());
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
    }

    /**
     * Test the behavior on addLike method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOnNull() {
        adapter.add(null);
    }

    /**
     * Test the update method will the merge the current list with the given one.
     */
    @Test
    public void testUpdate() {
        int id = unExistingLineup.getId();
        List<Lineup> expectedList = new ArrayList<>();
        List<Lineup> newItems = new ArrayList<>();
        calendar.add(Calendar.SECOND, 10);
        newItems.add(new Lineup(id++, user1, date, calendar.getTime()));
        calendar.add(Calendar.SECOND, 11);
        newItems.add(new Lineup(id++, user2, date, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 20);
        newItems.add(new Lineup(id++, user3, date, calendar.getTime()));
        calendar.add(Calendar.HOUR, 1);
        newItems.add(new Lineup(id, user2, date, calendar.getTime()));
        adapter.update(newItems);
        for (int i = newItems.size() - 1; i >= 0; i--) {
            expectedList.add(newItems.get(i));
        }
        expectedList.addAll(lineups);
        assertEquals(expectedList.size(), adapter.getCount());
        for (int i = 0; i < expectedList.size(); i++) {
            assertSame(expectedList.get(i), adapter.getItem(i));
        }
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
    }

    /**
     * Test the behavior on update method when the given list has already some of the lineups
     * in the adapter.
     */
    @Test
    public void testUpdateWithExistingLineup() {
        int id = unExistingLineup.getId();
        List<Lineup> expectedList = new ArrayList<>();
        List<Lineup> newItems = new ArrayList<>();
        calendar.add(Calendar.MINUTE, 10);
        newItems.add(new Lineup(id, user2, date, calendar.getTime()));
        newItems.add(lineup3);
        newItems.add(authUserLineup2);
        adapter.update(newItems);
        expectedList.add(newItems.get(0));
        expectedList.addAll(lineups);
        assertEquals(expectedList.size(), adapter.getCount());
        for (int i = 0; i < expectedList.size(); i++) {
            assertSame(expectedList.get(i), adapter.getItem(i));
        }
    }

    /**
     * Test the behavior when delete method is called.
     */
    @Test
    public void testDelete() {
        int index = lineups.indexOf(lineup1);
        adapter.delete(lineup1);
        assertEquals(NUMBER_OF_LINEUPS - 1, adapter.getCount());
        assertSame(lineups.get(index + 1), adapter.getItem(index));
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
    }

    /**
     * Test that the view at the given position in the adapter is correctly mapped.
     */
    @Test
    public void testGetView() {
        View view = adapter.getView(lineups.indexOf(lineup1), null, null);
        assertNotNull(view);
        RelativeLayout mainContent = (RelativeLayout)
                view.findViewById(R.id.lineupsItem_mainContent);
        assertNotNull(mainContent);
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        LinearLayout spinner = (LinearLayout) view.findViewById(R.id.lineupsItem_spinner);
        assertNotNull(spinner);
        assertEquals(View.GONE, spinner.getVisibility());
        TextView txtUser = (TextView) view.findViewById(R.id.lineupsItem_user);
        assertNotNull(txtUser);
        assertEquals("by " + lineup1.getUser().getName(), txtUser.getText());
        TextView txtUpdatedAt = (TextView) view.findViewById(R.id.lineupsItem_updatedAt_text);
        assertNotNull(txtUpdatedAt);
        assertEquals(sDate, txtUpdatedAt.getText());
        ButtonAwesome btnDelete = (ButtonAwesome) view.findViewById(R.id.lineupItem_btnDelete);
        assertNotNull(btnDelete);
        assertEquals(View.GONE, btnDelete.getVisibility());
    }

    /**
     * Test that the view at the given positions is correctly mapped when the lineup is created
     * by the authenticated user.
     */
    @Test
    public void testGetViewOnLineupItemCreatedByAuthUser() {
        View view = adapter.getView(lineups.indexOf(authUserLineup1), null, null);
        assertNotNull(view);
        ButtonAwesome btnDelete = (ButtonAwesome) view.findViewById(R.id.lineupItem_btnDelete);
        assertNotNull(btnDelete);
        assertEquals(View.VISIBLE, btnDelete.getVisibility());
    }

    /**
     * Test that the view is correctly mapped for a lineup with null user.
     */
    @Test
    public void testGetViewOnNullLineupUser() {
        lineup2.setUser(null);
        View view = adapter.getView(lineups.indexOf(lineup2), null, null);
        assertNotNull(view);
        TextView txtUser = (TextView) view.findViewById(R.id.lineupsItem_user);
        assertNotNull(txtUser);
        assertEquals("", txtUser.getText());
    }

    /**
     * Test that the view is correctly mapped for a lineup with null updated at.
     */
    @Test
    public void testGetViewOnNullUpdatedAt() {
        lineup3.setUpdatedAt(null);
        View view = adapter.getView(lineups.indexOf(lineup3), null, null);
        assertNotNull(view);
        TextView txtUpdatedAt = (TextView) view.findViewById(R.id.lineupsItem_updatedAt_text);
        assertNotNull(txtUpdatedAt);
        assertEquals("", txtUpdatedAt.getText());
    }

    /**
     * Test the geView will recycle the view when is not null.
     */
    @Test
    public void testGetViewOnRecycleView() {
        View recycleView = View.inflate(context, R.layout.lineups_list_item, null);
        ListLineupsAdapter.ViewHolder holder = adapter.new ViewHolder(recycleView);
        recycleView.setTag(holder);
        View view = adapter.getView(lineups.indexOf(lineup3), recycleView, null);
        assertSame(recycleView, view);
    }

    /**
     * Test the behavior when the lineup players content from the adapter is clicked.
     */
    @Test
    public void testLineupPlayersClick() {
        View view = adapter.getView(lineups.indexOf(lineup4), null, null);
        assertNotNull(view);
        RelativeLayout lineupPlayers = (RelativeLayout)
                view.findViewById(R.id.lineupsListItem_lineupPlayers);
        assertNotNull(lineupPlayers);
        assertTrue(lineupPlayers.performClick());
        verify(listener).onLineupPlayersSelected(lineup4);
    }

    /**
     * Test the behavior when the lineup likes content from the adapter is clicked.
     */
    @Test
    public void testLineupLikesClick() {
        View view = adapter.getView(lineups.indexOf(lineup3), null, null);
        assertNotNull(view);
        LinearLayout lineupLikes = (LinearLayout)
                view.findViewById(R.id.lineupsListItem_likes);
        assertNotNull(lineupLikes);
        assertTrue(lineupLikes.performClick());
        verify(listener).onLineupLikesSelected(lineup3);
    }

    /**
     * Test the behavior when the lineup comments content from the adapter is clicked.
     */
    @Test
    public void testLineupCommentsClick() {
        View view = adapter.getView(lineups.indexOf(lineup2), null, null);
        assertNotNull(view);
        LinearLayout lineupComments = (LinearLayout)
                view.findViewById(R.id.lineupsListItem_comments);
        assertNotNull(lineupComments);
        assertTrue(lineupComments.performClick());
        verify(listener).onLineupCommentsSelected(lineup2);
    }

    /**
     * Test the behavior when button 'Delete' is clicked for the item in the adapter.
     */
    @Test
    public void testBtnDeleteClick() {
        View view = adapter.getView(lineups.indexOf(authUserLineup1), null, null);
        assertNotNull(view);
        ButtonAwesome btnDelete = (ButtonAwesome) view.findViewById(R.id.lineupItem_btnDelete);
        assertNotNull(btnDelete);
        assertTrue(btnDelete.performClick());
        verify(listener).onBtnDeleteClick(authUserLineup1);
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                view = adapter.getView(lineups.indexOf(authUserLineup1), null, null);
            }
            RelativeLayout mainContent = (RelativeLayout)
                    view.findViewById(R.id.lineupsItem_mainContent);
            assertNotNull(mainContent);
            assertEquals(View.GONE, mainContent.getVisibility());
            LinearLayout spinner = (LinearLayout) view.findViewById(R.id.lineupsItem_spinner);
            assertNotNull(spinner);
            assertEquals(View.VISIBLE, spinner.getVisibility());
        }
    }

    /**
     * Test the behavior on the adapter when onDeleteFailed is called.
     */
    @Test
    public void testOnDeleteFailed() {
        View view = adapter.getView(lineups.indexOf(authUserLineup2), null, null);
        assertNotNull(view);
        ButtonAwesome btnDelete = (ButtonAwesome) view.findViewById(R.id.lineupItem_btnDelete);
        assertNotNull(btnDelete);
        assertTrue(btnDelete.performClick());
        adapter.onDeletingFailed(authUserLineup2);
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
        view = adapter.getView(lineups.indexOf(authUserLineup2), null, null);
        assertNotNull(view);
        RelativeLayout mainContent = (RelativeLayout)
                view.findViewById(R.id.lineupsItem_mainContent);
        assertNotNull(mainContent);
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        LinearLayout spinner = (LinearLayout) view.findViewById(R.id.lineupsItem_spinner);
        assertNotNull(spinner);
        assertEquals(View.GONE, spinner.getVisibility());
    }
}
