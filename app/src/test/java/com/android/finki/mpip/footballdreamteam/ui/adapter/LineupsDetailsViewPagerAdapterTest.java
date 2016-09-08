package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockActivity;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Borce on 15.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class LineupsDetailsViewPagerAdapterTest {


    private MockActivity activity;
    private LineupsDetailsViewPagerAdapter adapter;

    private final int NUMBER_OF_FRAGMENTS = 2;

    private Lineup lineup = new Lineup(1, new Date(), new Date(), 14, 152, new User(1, "User"));

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(MockActivity.class);
        adapter = new LineupsDetailsViewPagerAdapter(activity,
                activity.getSupportFragmentManager());
        adapter.setLineup(lineup);
    }

    /**
     * Test that getCount method retuen the number of fragment in the adapter.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_FRAGMENTS, adapter.getCount());
    }

    /**
     * Test that getItem will return return the fragment at the given position in the list.
     */
    @Test
    public void testGetItem() {
        Fragment fragment = adapter.getItem(0);
        assertNotNull(fragment);
    }

    /**
     * Test the behavior on the getItem method called with un existing position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetItemOnUnExistingPosition() {
        adapter.getItem(NUMBER_OF_FRAGMENTS + 1);
    }

    /**
     * Test that the fragments in the list are correctly set.
     */
    @Test
    public void testAdapterListCorrectlySet() {
        assertTrue(adapter.getItem(0) instanceof LikeFragment);
        assertTrue(adapter.getItem(1) instanceof CommentsFragment);
    }

    /**
     * Test that the view is correctly set when getTabViewAt is called with 0 position.
     */
    @Test
    public void testGetTabViewAtOnFirstPosition() {
        String likesText = activity.getString(R.string.lineupDetailsActivity_likesTab_text);
        View view = adapter.getTabViewAt(0);
        assertNotNull(view);
        TextView txtText = (TextView) view.findViewById(R.id.lineupTab_text);
        assertNotNull(txtText);
        assertEquals(likesText, txtText.getText());
        TextView txtCount = (TextView) view.findViewById(R.id.lineupTab_count);
        assertNotNull(txtCount);
        assertEquals(String.format("%d", lineup.getLikesCount()), txtCount.getText());
    }

    /**
     * Test that the view is correctly set when getTabViewAt is called with 1 position.
     */
    @Test
    public void testGetTabViewAtOnSecondPosition() {
        String commentText = activity.getString(R.string.lineupDetailsActivity_commentsTab_text);
        View view = adapter.getTabViewAt(1);
        assertNotNull(view);
        TextView txtText = (TextView) view.findViewById(R.id.lineupTab_text);
        assertNotNull(txtText);
        assertEquals(commentText, txtText.getText());
        TextView txtCount = (TextView) view.findViewById(R.id.lineupTab_count);
        assertNotNull(txtCount);
        assertEquals(String.format("%d", lineup.getCommentsCount()), txtCount.getText());
    }

    /**
     * Test the behavior on the getTabViewAt method called with un existing position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetTabViewAtOnUnExistingPosition() {
        adapter.getTabViewAt(NUMBER_OF_FRAGMENTS + 1);
    }
}