package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersFragmentModule;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListPositionPlayersAdapter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersFragmentPresenter;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Borce on 17.08.2016.
 */
public class ListPositionPlayersFragment extends Fragment {

    private static Logger logger = LoggerFactory.getLogger(ListPositionPlayersFragment.class);
    private static final String PLACE_KEY = "place";
    private static final String EXCLUDE_LAYERS_KEY = "exclude_players";

    private ListPositionPlayersFragmentPresenter presenter;
    private PositionUtils utils;

    @BindView(R.id.positionPlayersLayout_headerText)
    TextView txtPositionPlace;

    @BindView(R.id.positionPlayersLayout_listView)
    ListView listView;

    private ListPositionPlayersAdapter adapter;

    /**
     * Create a new instance of the fragment.
     *
     * @param place position place on the field for which the player will be listed
     * @return new instance of the fragment
     */
    public static ListPositionPlayersFragment newInstance(PositionUtils.POSITION_PLACE place,
                                                          int[] playersToExclude) {
        if (place == null) {
            String message = "position place can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (playersToExclude == null) {
            String message = "players to exclude can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        ListPositionPlayersFragment fragment = new ListPositionPlayersFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLACE_KEY, place);
        args.putIntArray(EXCLUDE_LAYERS_KEY, playersToExclude);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Set the fragment presenter.
     *
     * @param presenter fragment presenter
     */
    @Inject
    public void setPresenter(ListPositionPlayersFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Inject the position utils.
     *
     * @param utils instance of PositionUtilss
     */
    @Inject
    void setPositionUtils(PositionUtils utils) {
        this.utils = utils;
    }

    /**
     * Get the bundle key for the formation place data.
     *
     * @return bundle key
     */
    public static String getPlaceKey() {
        return PLACE_KEY;
    }

    /**
     * Get the bundle key for the players to exclude data.
     *
     * @return bundle key
     */
    public static String getExcludeLayersKey() {
        return EXCLUDE_LAYERS_KEY;
    }

    /**
     * Called when the fragment is ready to be created.
     *
     * @param savedInstanceState saved state for when the fragment is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new ListPositionPlayersFragmentModule(this)).inject(this);
        presenter.onFragmentCreated(this.getArguments());
    }

    /**
     * Called when the fragment view is ready to be created.
     *
     * @param inflater           system Layout inflater
     * @param container          fragment root container
     * @param savedInstanceState saved state for when the fragment is recreated
     * @return fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.position_players_layout, container, false);
        ButterKnife.bind(this, view);
        presenter.onViewCreated();
        return view;
    }

    /**
     * Create the adapter for the ListVIew.
     *
     * @param players position players
     */
    public void setAdapter(List<Player> players) {
        adapter = new ListPositionPlayersAdapter(this.getActivity(), players, utils);
        listView.setAdapter(adapter);
    }

    /**
     * Set the text of the text view position place.
     *
     * @param place position place
     */
    public void setPositionPlace(String place) {
        txtPositionPlace.setText(place);
    }

    /**
     * Handle selecting the players in the list view.
     *
     * @param position player position in the list
     */
    @OnItemClick(R.id.positionPlayersLayout_listView)
    void onPlayerSelected(int position) {
        Player player = adapter.getItem(position);
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).onPlayerSelected(player);
        }
    }

    /**
     * Fragment listener that the activity that wants to know when a player
     * is selected will need implement.
     */
    public interface Listener {
        void onPlayerSelected(Player player);
    }
}
