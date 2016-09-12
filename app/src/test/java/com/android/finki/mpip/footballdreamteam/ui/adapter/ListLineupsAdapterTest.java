package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowBaseAdapter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 13.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ListLineupsAdapterTest {

    private ListLineupsAdapter adapter;
    private ShadowBaseAdapter shadow;
    private Context context;

    private final int year = 2016, month = 8, day = 13, hour = 19, minute = 7, second = 20;
    private Calendar calendar = new GregorianCalendar(year, month, day, hour, minute, second);
    private String sDate = DateUtils.dayNameFormat(calendar.getTime());

    private final int NUMBER_OF_LINEUPS = 3;
    private final int i1 = 0;
    private Lineup lineup1 = new Lineup(1, calendar.getTime(), calendar.getTime(),
            0, 0, new User(1, "User 1"));
    private final int i2 = 1;
    private Lineup lineup2 = new Lineup(2, calendar.getTime(), calendar.getTime(),
            0, 0, new User(2, "User 2"));
    private final int i3 = 2;
    private Lineup lineup3 = new Lineup(3, calendar.getTime(), calendar.getTime(),
            0, 0, new User(1, "User 1"));

    private Lineup unExistingLineup = new Lineup(4, 125);

    @Before
    public void setup() {
        context = RuntimeEnvironment.application.getBaseContext();
        adapter = new ListLineupsAdapter(context);
        shadow = shadowOf(adapter);
        adapter.add(lineup1);
        adapter.add(lineup2);
        adapter.add(lineup3);
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
        Lineup lineup = adapter.getItem(i1);
        assertNotNull(lineup);
        assertSame(lineup1, lineup);
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
     * Test the onUpdateSuccess method will the merge the current list with the given one.
     */
    @Test
    public void testUpdate() {
        int lineup1Id = 10;
        int lineup2Id = 20;
        int lineup3Id = 30;
        List<Lineup> lineups = Arrays.asList(new Lineup(lineup1Id, 0),
                new Lineup(lineup2Id, 0), new Lineup(lineup3Id, 0));
        adapter.update(lineups);
        assertEquals(NUMBER_OF_LINEUPS + lineups.size(), adapter.getCount());
        assertSame(lineups.get(0), adapter.getItem(NUMBER_OF_LINEUPS));
        assertSame(lineups.get(1), adapter.getItem(NUMBER_OF_LINEUPS + 1));
        assertSame(lineups.get(2), adapter.getItem(NUMBER_OF_LINEUPS + 2));
        assertTrue(shadow.wasNotifyDataSetChangedCalled());
    }

    /**
     * Test the getView method works.
     */
    @Test
    public void testGetView() {
        View view = adapter.getView(i1, null, null);
        assertNotNull(view);
        TextView txtUser = (TextView) view.findViewById(R.id.lineupsItem_user);
        assertNotNull(txtUser);
        assertEquals("by " + lineup1.getUser().getName(), txtUser.getText());
        TextView txtUpdatedAt = (TextView) view.findViewById(R.id.lineupsItem_updatedAt_text);
        assertNotNull(txtUpdatedAt);
        assertEquals(sDate, txtUpdatedAt.getText());
    }

    /**
     * Test the geView will recycle the view when is not null.
     */
    @Test
    public void testGetViewOnRecycleView() {
        View recycleView = View.inflate(context, R.layout.lineups_list_item, null);
        ListLineupsAdapter.ViewHolder holder = new ListLineupsAdapter.ViewHolder(recycleView);
        recycleView.setTag(holder);
        View view = adapter.getView(i1, recycleView, null);
        assertSame(recycleView, view);
    }
}
