package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;
import com.android.finki.mpip.footballdreamteam.utility.ListUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Borce on 17.08.2016.
 */
public class ListPositionPlayersAdapter extends BaseAdapter {

    private static Logger logger = LoggerFactory.getLogger(ListPositionPlayersAdapter.class);
    private Context context;
    private List<Player> players;
    private PositionUtils utils;

    public ListPositionPlayersAdapter(Context context, List<Player> players, PositionUtils utils) {
        this.context = context;
        this.players = players;
        this.utils = utils;
    }

    /**
     * Get the number of items in the adapter.
     *
     * @return number of items in the adapter
     */
    @Override
    public int getCount() {
        return players.size();
    }

    /**
     * Get the item at the given position.
     *
     * @param position item position in the list
     * @return item at the given position
     */
    @Override
    public Player getItem(int position) {
        if (position > players.size() - 1) {
            String message = String.format("index out of bounds, position %d, list size %d",
                    position, players.size());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return players.get(position);
    }

    /**
     * Get the item id.
     *
     * @param id item id
     * @return item id
     */
    @Override
    public long getItemId(int id) {
        return id;
    }

    /**
     * Merge the given list with the current.
     *
     * @param players List of players that will be merged
     */
    public void update(List<Player> players) {
        this.players = ListUtils.concat(this.players, players);
    }

    /**
     * Get the view for the item at the given position.
     *
     * @param position item position
     * @param view item view
     * @param container item root container
     * @return item view
     */
    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.position_players_list_item, container, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.setPositions(position);
        return view;
    }

    /**
     * Class holder for the adapter items view.
     */
    public class ViewHolder {

        @BindView(R.id.positionPlayerLayout_txtName)
        TextView txtName;

        @BindView(R.id.positionPlayerLayout_txtAge)
        TextView txtAge;

        @BindView(R.id.positionPlayerLayout_txtTeam)
        TextView txtTeam;

        @BindView(R.id.positionPlayerLayout_txtPosition)
        TextView txtPosition;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        /**
         * Set the holder position in the adapter.
         *
         * @param position holder position
         */
        public void setPositions(int position) {
            Player player = players.get(position);
            txtName.setText(player.getName());
            txtAge.setText(String.valueOf(DateUtils.getYearDiff(player.getDateOfBirth())));
            if (player.getTeam() != null) {
                txtTeam.setText(player.getTeam().getName());
            }
            if (player.getPosition() != null) {
                txtPosition.setText(utils.getShortName(player.getPosition()));
            }
        }
    }
}
