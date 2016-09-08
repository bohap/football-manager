package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Borce on 15.08.2016.
 */
public class LineupsDetailsViewPagerAdapter extends FragmentPagerAdapter {

    private static Logger logger = LoggerFactory.getLogger(LineupsDetailsViewPagerAdapter.class);
    private Context context;
    private Lineup lineup;
    private List<Fragment> fragments;

    public LineupsDetailsViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        fragments = new ArrayList<>();
    }

    /**
     * Set the lineup.
     *
     * @param lineup Lineup
     */
    public void setLineup(Lineup lineup) {
        if (lineup == null) {
            String message = "lineup can;t be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.lineup = lineup;
        fragments.add(LikeFragment.newInstance(lineup));
        fragments.add(CommentsFragment.newInstance(lineup.getId()));
    }

    /**
     * Get the fragment at the given position.
     *
     * @param position fragment position in the list
     * @return fragment at the given position
     */
    @Override
    public Fragment getItem(int position) {
        if (position > fragments.size()) {
            String message = String.format("index out of bounds, position %d, size %d",
                    position, fragments.size());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (position < 0) {
            String message = String.format("invalid position %d", position);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return fragments.get(position);
    }

    /**
     * Get the number of fragments.
     *
     * @return number of fragments
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * Get the tab view for the fragment at the given position.
     *
     * @param position fragment position in the list
     * @return tab view for the fragment at the given position
     */
    @SuppressLint("InflateParams")
    public View getTabViewAt(int position) {
        if (lineup == null) {
            String message = "lineup is not yet set";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.lineup_details_tab_view, null, false);
        TextView text = (TextView) view.findViewById(R.id.lineupTab_text);
        TextView count = (TextView) view.findViewById(R.id.lineupTab_count);
        if (position == 0) {
            text.setText(R.string.lineupDetailsActivity_likesTab_text);
            count.setText(String.format("%d", lineup.getLikesCount()));
        } else if (position == 1) {
            text.setText(R.string.lineupDetailsActivity_commentsTab_text);
            count.setText(String.format("%d", lineup.getCommentsCount()));
        } else {
            String message = String.format("index out of bound, position %d, size %d",
                            position, fragments.size());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return view;
    }
}
