package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListPositionPlayersAdapter;
import com.android.finki.mpip.footballdreamteam.ui.component.ListPositionPlayersView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersViewPresenter;
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
public class ListPositionPlayersFragment extends BaseFragment implements ListPositionPlayersView {

    private Logger logger = LoggerFactory.getLogger(ListPositionPlayersFragment.class);
    private ListPositionPlayersViewPresenter presenter;
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
            throw new IllegalArgumentException("position place can't be null");
        }
        if (playersToExclude == null) {
            throw new IllegalArgumentException("players to exclude can't be null");
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
    public void setPresenter(ListPositionPlayersViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Inject the position utils.
     *
     * @param utils instance of PositionUtils
     */
    @Inject
    void setPositionUtils(PositionUtils utils) {
        this.utils = utils;
    }

    /**
     * Called when the fragment is ready to be created.
     *
     * @param savedInstanceState saved state for when the fragment is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new ListPositionPlayersViewModule(this)).inject(this);
        presenter.onViewCreated(this.getArguments());
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
        logger.info("onCreateView");
        View view = inflater.inflate(R.layout.position_players_layout, container, false);
        ButterKnife.bind(this, view);
        presenter.onViewLayoutCreated();
        return view;
    }

    /**
     * Create the adapter for the ListVIew.
     *
     * @param players position players
     */
    @Override
    public void setAdapter(List<Player> players) {
        logger.info("setAdapter");
        adapter = new ListPositionPlayersAdapter(this.getActivity(), players, utils);
        listView.setAdapter(adapter);
    }

    /**
     * Set the text of the text view position place.
     *
     * @param place position place
     */
    @Override
    public void setPositionPlace(String place) {
        logger.info("setPositionsPlace");
        txtPositionPlace.setText(place);
    }

    /**
     * Handle selecting the players in the list view.
     *
     * @param position player position in the list
     */
    @OnItemClick(R.id.positionPlayersLayout_listView)
    void onPlayerSelected(int position) {
        logger.info(String.format("onPlayerSelected, positions %d", position));
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
