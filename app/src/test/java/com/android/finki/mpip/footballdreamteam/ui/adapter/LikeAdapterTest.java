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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    private final Calendar calendar = new GregorianCalendar(2016, 7, 25, 22, 22, 12);
    private Date date = calendar.getTime();
    private String sDate = DateUtils.dayNameFormat(date);
    private final int NUMBER_OF_ITEMS = 4;
    private List<UserLike> likes;

    @Before
    public void setup() {
        likes = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            String name = String.format("User %d", i + 1);
            likes.add(new UserLike((i + 1), name, new LineupLike(i + 1, i + 1, date)));
        }
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
        assertEquals(sDate, createdAt.getText());
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
        LikesAdapter.ViewHolder holder = adapter.new ViewHolder(recycledView);
        recycledView.setTag(holder);
        View view = adapter.getView(1, recycledView, null);
        assertSame(recycledView, view);
    }

    /**
     * Test that add method add a new item in the adapter.
     */
    @Test
    public void testAdd() {
        int id = NUMBER_OF_ITEMS + 1;
        UserLike userLike = new UserLike(id, "User 101", new LineupLike(id, id + 2, date));
        adapter.add(userLike);
        assertEquals(NUMBER_OF_ITEMS + 1, adapter.getCount());
        assertSame(userLike, adapter.getItem(0));
        assertTrue(shadowOf(adapter).wasNotifyDataSetChangedCalled());
    }

    /**
     * Test that remove method remove a item from the adapter.
     */
    @Test
    public void testRemove() {
        List<UserLike> likes = new ArrayList<>(this.likes);
        final int position = 1;
        UserLike userLike = likes.get(position);
        adapter.remove(userLike);
        assertEquals(NUMBER_OF_ITEMS - 1, adapter.getCount());
        assertSame(likes.get(position + 1), adapter.getItem(position));
        assertTrue(shadowOf(adapter).wasNotifyDataSetChangedCalled());
    }

    /**
     * Test update method will sync the list of likes with the given list.
     */
    @Test
    public void testUpdate() {
        int id = this.likes.get(NUMBER_OF_ITEMS - 1).getId();
        final UserLike like1 = new UserLike(id + 1, "User", new LineupLike(id + 1, id + 1, date));
        id++;
        final UserLike like2 = new UserLike(id + 1, "User", new LineupLike(id + 1, id + 1, date));
        List<UserLike> likes = Arrays.asList(like1, this.likes.get(3), like2);
        adapter.update(likes);
        assertEquals(NUMBER_OF_ITEMS + 2, adapter.getCount());
        assertSame(likes.get(0), adapter.getItem(NUMBER_OF_ITEMS));
        assertSame(likes.get(2), adapter.getItem(NUMBER_OF_ITEMS + 1));
    }
}
