package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 25.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class LikeAdapterTest {

    private LikesAdapter adapter;
    private final int year = 2016, month = 8, day = 25, hour = 22, minute = 22, second = 12;
    private final Calendar calendar = new GregorianCalendar(year, month, day, hour, minute, second);
    private final int NUMBER_OF_ITEMS = 4;
    private List<UserLike> likes;

    @Before
    public void setup() {
        likes = new ArrayList<>();
        likes.add(new UserLike(1, "User 1", new LineupLike(1, 1, calendar.getTime())));
        likes.add(new UserLike(2, "User 2", new LineupLike(2, 1, calendar.getTime())));
        likes.add(new UserLike(3, "User 3", new LineupLike(3, 2, calendar.getTime())));
        likes.add(new UserLike(4, "User 4", new LineupLike(4, 2, calendar.getTime())));
        adapter = new LikesAdapter(RuntimeEnvironment.application.getBaseContext(), likes);
    }

    /**
     * Test that getCount method returns the number of items in the adapter.
     */
    @Test
    public void testGetCount() {
        assertEquals(NUMBER_OF_ITEMS, adapter.getCount());
    }

    /**
     * Test the behavior on getItem called with un existing position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetItemOnUnExistingPosition() {
        adapter.getItem(likes.size());
    }

    /**
     * Test that getItem return the item at the ginve position in the adapter.
     */
    @Test
    public void testGetItem() {
        final int position = 2;
        UserLike item = adapter.getItem(position);
        assertSame(likes.get(position), item);
    }

    /**
     * Test the getView correctly bind the adapter item to a layout.
     */
    @Test
    public void testGetView() {
        final int position = 1;
        View view = adapter.getView(position, null, null);
        assertNotNull(view);
        TextView user = (TextView) view.findViewById(R.id.likesListItem_user);
        assertNotNull(user);
        assertEquals(likes.get(position).getName(), user.getText());
        TextView createdAt = (TextView) view.findViewById(R.id.likesListItem_cratedAt);
        assertNotNull(createdAt);
        assertEquals(DateUtils.dayNameFormat(likes.get(position).getPivot().getCreatedAt()),
                createdAt.getText());
    }

    /**
     * Get the behavior when getView is called with null like pivot.
     */
    @Test
    public void testGetViewOnNullLikePivot() {
        final int position = 1;
        UserLike like = likes.get(position);
        like.setPivot(null);
        View view = adapter.getView(position, null, null);
        assertNotNull(view);
        TextView user = (TextView) view.findViewById(R.id.likesListItem_user);
        assertNotNull(user);
        assertEquals(likes.get(position).getName(), user.getText());
        TextView createdAt = (TextView) view.findViewById(R.id.likesListItem_cratedAt);
        assertNotNull(createdAt);
        assertEquals("", createdAt.getText());
    }

    /**
     * Test the behavior when getView is called with null like crated at.
     */
    @Test
    public void testGetViewOnNullLikeCreatedAt() {
        final int position = 1;
        UserLike like = likes.get(position);
        like.getPivot().setCreatedAt(null);
        View view = adapter.getView(position, null, null);
        assertNotNull(view);
        TextView user = (TextView) view.findViewById(R.id.likesListItem_user);
        assertNotNull(user);
        assertEquals(likes.get(position).getName(), user.getText());
        TextView createdAt = (TextView) view.findViewById(R.id.likesListItem_cratedAt);
        assertNotNull(createdAt);
        assertEquals("", createdAt.getText());
    }

    /**
     * Test that when getView is called with view different that null, a new
     * view will not be crated.
     */
    @Test
    public void testGetViewOnRecycledView() {
        View recycledView = View.inflate(RuntimeEnvironment.application.getBaseContext(),
                R.layout.likes_list_item, null);
        LikesAdapter.ViewHolder holder = new LikesAdapter.ViewHolder(recycledView);
        recycledView.setTag(holder);
        View view = adapter.getView(1, recycledView, null);
        assertSame(recycledView, view);
    }

    /**
     * Test that addLike method add a new item in the adapter.
     */
    @Test
    public void testAddLike() {
        UserLike userLike = new UserLike(5, "User 5", new LineupLike(5, 2, calendar.getTime()));
        adapter.addLike(userLike);
        assertEquals(NUMBER_OF_ITEMS + 1, adapter.getCount());
        assertSame(userLike, adapter.getItem(0));
        assertTrue(shadowOf(adapter).wasNotifyDataSetChangedCalled());
    }

    /**
     * Test that removeLike method onRemoveSuccess a item from the adapter.
     */
    @Test
    public void testRemoveLike() {
        final int position = 2;
        UserLike userLike = likes.get(position);
        adapter.removeLike(userLike);
        assertEquals(NUMBER_OF_ITEMS - 1, adapter.getCount());
        assertTrue(shadowOf(adapter).wasNotifyDataSetChangedCalled());
    }
}
