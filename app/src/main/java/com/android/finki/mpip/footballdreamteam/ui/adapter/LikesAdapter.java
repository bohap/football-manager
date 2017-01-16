package com.android.finki.mpip.footballdreamteam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;
import com.android.finki.mpip.footballdreamteam.utility.ListUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Borce on 25.08.2016.
 */
public class LikesAdapter extends BaseAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LikesAdapter.class);
    private Context context;
    private List<UserLike> likes;

    public LikesAdapter(Context context, List<UserLike> likes) {
        this.context = context;
        this.likes = likes;
    }

    /**
     * Get the number of items in the adapter.
     *
     * @return number of items in the adapter
     */
    @Override
    public int getCount() {
        return likes.size();
    }

    /**
     * Get the item in the adapter at the given position.
     *
     * @param position item position
     * @return item in the list at the given position
     */
    @Override
    public UserLike getItem(int position) {
        if (position > likes.size() - 1) {
            throw new IllegalArgumentException(String
                    .format("invalid position, i-%d, list size-%d", position, likes.size()));
        }
        return likes.get(position);
    }

    /**
     * Get the item id.
     *
     * @param id item id
     * @return item id
     */
    @Override
    public long getItemId(int id) {
        return 0;
    }

    /**
     * Get the view for the item at the givne position.
     *
     * @param i         item position
     * @param view      item view
     * @param viewGroup item parent container
     * @return item view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.likes_list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.setPositions(i);
        return view;
    }

    /**
     * Add a new like.
     *
     * @param userLike like to be added
     */
    public void add(UserLike userLike) {
        logger.info("add");
        likes.add(0, userLike);
        super.notifyDataSetChanged();
    }

    /**
     * Remove the like from the list.
     *
     * @param userLike like to be removed
     */
    public void remove(UserLike userLike) {
        logger.info("remove");
        likes.remove(userLike);
        super.notifyDataSetChanged();
    }

    /**
     * Merge the given list with the current.
     *
     * @param likes List of likes that will be merged
     */
    public void update(List<UserLike> likes) {
        this.likes = ListUtils.concat(this.likes, likes);
    }

    /**
     * Class holder for the adapter views.
     */
    public class ViewHolder {

        @BindView(R.id.likesListItem_user)
        TextView user;

        @BindView(R.id.likesListItem_cratedAt)
        TextView createdAt;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        /**
         * Set the positions for the view holder.
         *
         * @param position view holder position
         */
        public void setPositions(int position) {
            UserLike userLike = likes.get(position);
            user.setText(userLike.getName());
            if (userLike.getPivot() != null && userLike.getPivot().getCreatedAt() != null) {
                createdAt.setText(DateUtils.dayNameFormat(userLike.getPivot().getCreatedAt()));
            }
        }
    }
}
