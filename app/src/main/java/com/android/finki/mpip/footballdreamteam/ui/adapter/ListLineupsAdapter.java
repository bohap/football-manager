package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;
import com.android.finki.mpip.footballdreamteam.utility.ListUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 10.08.2016.
 */
public class ListLineupsAdapter extends BaseAdapter {

    private static Logger logger = LoggerFactory.getLogger(ListLineupsAdapter.class);
    private Context context;
    private Listener listener;
    private User user;
    private List<Lineup> lineups;
    private Map<Integer, Boolean> deleting;

    public ListLineupsAdapter(Context context, List<Lineup> lineups, User user,
                              Listener listener) {
        this.context = context;
        this.listener = listener;
        this.user = user;
        this.lineups = lineups;
        deleting = new HashMap<>();
        for (Lineup lineup : lineups) {
            deleting.put(lineup.getId(), false);
        }
    }

    /**
     * Get the number of items in the adapter.
     *
     * @return number of items in the adapter
     */
    @Override
    public int getCount() {
        return lineups.size();
    }

    /**
     * Get e element from the list adapter at the given position.
     *
     * @param i element position
     * @return element from the list or null if there is no element at that position
     */
    @Override
    public Lineup getItem(int i) {
        if (i > lineups.size() - 1) {
            throw new IllegalArgumentException(String
                    .format("can't get lineup, index out of bound, index-%d, list size-%d",
                            i, lineups.size()));
        }
        return lineups.get(i);
    }

    /**
     * Get the element id.
     *
     * @param id element id
     * @return element id
     */
    @Override
    public long getItemId(int id) {
        return id;
    }

    /**
     * Add a new lineup to the list.
     *
     * @param lineup Lineup to be added
     */
    public void add(Lineup lineup) {
        logger.info("add");
        if (lineup == null) {
            throw new IllegalArgumentException("lineup can't be null");
        }
        this.lineups.add(lineup);
        this.deleting.put(lineup.getId(), false);
        super.notifyDataSetChanged();
    }

    /**
     * Merge the given list with the current.
     *
     * @param lineups List of lineups that will be merged
     */
    public void update(List<Lineup> lineups) {
        logger.info("update");
        this.lineups = ListUtils.concat(this.lineups, lineups);
        Collections.sort(this.lineups);
        for (Lineup lineup : this.lineups) {
            if (this.deleting.get(lineup.getId()) == null) {
                this.deleting.put(lineup.getId(), false);
            }
        }
        super.notifyDataSetChanged();
    }

    /**
     * Delete the lineup from the adapter.
     *
     * @param lineup lineup to be deleted
     */
    public void delete(Lineup lineup) {
        lineups.remove(lineup);
        this.deleting.remove(lineup.getId());
        super.notifyDataSetChanged();
    }

    /**
     * Called when deleting the lineup failed.
     *
     * @param lineup lineup that was deleting
     */
    public void onDeletingFailed(Lineup lineup) {
        logger.info(String.format("onDeletingFailed, id - %d", lineup.getId()));
        this.deleting.put(lineup.getId(), false);
        super.notifyDataSetChanged();
    }

    /**
     * Checks if the given lineup is deleting in the adapter.
     *
     * @param lineup lineup to be checked
     */
    public boolean isDeleting(Lineup lineup) {
        if (lineup == null) {
            throw new IllegalArgumentException("lineup can't be null");
        }
        Boolean deleting = this.deleting.get(lineup.getId());
        if (deleting == null) {
            throw new IllegalArgumentException("lineup not in the adapter");
        }
        return deleting;
    }

    /**
     * Bind a view to the element in the list.
     *
     * @param position  element position
     * @param view      element view
     * @param viewGroup element view group
     * @return binned view to the element
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lineups_list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.setPosition(position);
        return view;
    }

    /**
     * View holder for the list layout to improve view searching.
     */
    public class ViewHolder {

        private int position;
        private Lineup lineup;

        @BindView(R.id.lineupsItem_mainContent)
        RelativeLayout content;

        @BindView(R.id.lineupsItem_spinner)
        LinearLayout spinner;

        @BindView(R.id.lineupsItem_user)
        TextView txtUser;

        @BindView(R.id.lineupsItem_updatedAt_text)
        TextView txtUpdatedAt;

        @BindView(R.id.lineupItem_btnDelete)
        ButtonAwesome btnDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        /**
         * Set the holder position in the adapter.
         *
         * @param position holder position
         */
        public void setPosition(int position) {
            this.position = position;
            lineup = lineups.get(position);
            User lineupUser = lineup.getUser();
            if (lineupUser != null) {
                txtUser.setText(String.format("by %s", lineupUser.getName()));
            }
            txtUpdatedAt.setText(DateUtils.dayNameFormat(lineup.getUpdatedAt()));
            int userId = lineup.getUserId();
            if (userId < 1 && lineup.getUser() != null) {
                userId = lineup.getUser().getId();
            }
            boolean canEdit = lineupUser != null && userId == user.getId();
            if (canEdit) {
                btnDelete.setVisibility(View.VISIBLE);
            } else {
                btnDelete.setVisibility(View.GONE);
            }
            boolean requestSending = deleting.get(lineup.getId());
            spinner.setVisibility(requestSending ? View.VISIBLE : View.GONE);
            content.setVisibility(requestSending ? View.GONE : View.VISIBLE);
        }

        /**
         * Handle click on the main content.
         */
        @OnClick(R.id.lineupsListItem_lineupPlayers)
        void onLineupPlayersClick() {
            logger.info(String.format("onLineupPlayersClick, position %d", position));
            listener.onLineupPlayersSelected(lineup);
        }

        /**
         * Handle click on the likes content.
         */
        @OnClick(R.id.lineupsListItem_likes)
        void onLikesClick() {
            logger.info(String.format("onLikesClick, position %d", position));
            listener.onLineupLikesSelected(lineup);
        }

        /**
         * Handle click on the comments content.
         */
        @OnClick(R.id.lineupsListItem_comments)
        void onCommentsClick() {
            logger.info(String.format("onCommentsClick, position %d", position));
            listener.onLineupCommentsSelected(lineup);
        }

        /**
         * Handle click on the 'Delete' button.
         */
        @OnClick(R.id.lineupItem_btnDelete)
        void onBtnDeleteClick() {
            logger.info(String.format("btn 'Delete' clicked, position %d", position));
            deleting.put(lineup.getId(), true);
            content.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            listener.onBtnDeleteClick(lineup);
        }
    }

    /**
     * Listener used for communication with the view using the listener.
     */
    public interface Listener {

        void onLineupPlayersSelected(Lineup lineup);

        void onLineupLikesSelected(Lineup lineup);

        void onLineupCommentsSelected(Lineup lineup);

        void onBtnDeleteClick(Lineup lineup);
    }
}
