package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Borce on 10.08.2016.
 */
public class ListLineupsAdapter extends BaseAdapter {

    private static Logger logger = LoggerFactory.getLogger(ListLineupsAdapter.class);

    private Context context;
    private ViewHolder holder;
    private List<Lineup> lineups;

    public ListLineupsAdapter(Context context) {
        this.context = context;
        lineups = new ArrayList<>();
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
            String message = String.format("can't get lineup, index out of bound, " +
                    "index-%d, list size-%d", i, lineups.size());
            logger.error(message);
            throw new IllegalArgumentException(message);
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
        if (lineup == null) {
            String message = "lineup can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.lineups.add(lineup);
        super.notifyDataSetChanged();
    }

    /**
     * Update the lineups list.
     *
     * @param lineups List of new lineups
     */
    public void update(List<Lineup> lineups) {
        this.lineups.addAll(lineups);
        super.notifyDataSetChanged();
    }

    /**
     * Bind a view to the element in the list.
     *
     * @param position element position
     * @param view element view
     * @param viewGroup element view group
     * @return binned view to the element
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lineups_list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Lineup lineup = lineups.get(position);
        if (lineup != null) {
            User user = lineup.getUser();
            if (user != null) {
                holder.txtUser.setText(String.format("by %s", user.getName()));
            }
            holder.txtUpdatedAt.setText(DateUtils.dayNameFormat(lineup.getUpdatedAt()));
        }
        return view;
    }

    /**
     * View holder for the list layout to improve view searching.
     */
    static class ViewHolder {

        @BindView(R.id.lineupsItem_user)
        TextView txtUser;

        @BindView(R.id.lineups_item_updated_at_text)
        TextView txtUpdatedAt;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
