package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationFragmentModule;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationFragmentPresenter;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Borce on 13.08.2016.
 */
public class LineupFormationFragment extends Fragment implements View.OnClickListener {

    private static Logger logger = LoggerFactory.getLogger(LineupFormationFragment.class);
    public static final String TAG = "LineupFormationFragment";
    public static final String LINEUP_PLAYERS_KEY = "LINEUP_PLAYERS";
    public static final String FORMATION_KEY = "LINEUP_FORMATION";
    public static final String LIST_PLAYERS_KEY = "LINEUP_LIST_PLAYERS";

    private LineupFormationFragmentPresenter presenter;

    @BindView(R.id.keeper)
    TextView keeper;

    @Nullable
    @BindView(R.id.leftBack)
    TextView leftBack;

    @Nullable
    @BindView(R.id.rightBack)
    TextView rightBack;

    @BindView(R.id.leftCentreBack)
    TextView leftCentreBack;

    @BindView(R.id.rightCentreBack)
    TextView rightCentreBack;

    @Nullable
    @BindView(R.id.centreCentreBack)
    TextView centreCentreBack;

    @BindView(R.id.leftCentreMidfield)
    TextView leftCentreMidfield;

    @BindView(R.id.rightCentreMidfield)
    TextView rightCentreMidfield;

    @Nullable
    @BindView(R.id.centreCentreMidfield)
    TextView centreCentreMidfield;

    @Nullable
    @BindView(R.id.leftWing)
    TextView leftWing;

    @Nullable
    @BindView(R.id.rightWing)
    TextView rightWing;

    @Nullable
    @BindView(R.id.attackingMidfield)
    TextView attackingMidfield;

    @Nullable
    @BindView(R.id.leftCentreForward)
    TextView leftCentreForward;

    @Nullable
    @BindView(R.id.rightCentreForward)
    TextView rightCentreForward;

    @Nullable
    @BindView(R.id.centreCentreForward)
    TextView centreCentreForward;

    private Unbinder unbinder;

    /**
     * Create a new instance of the fragment.
     *
     * @param lineupPlayers List of players
     * @return new instance of the fragment
     */
    public static LineupFormationFragment newInstance(LineupPlayers lineupPlayers) {
        if (lineupPlayers == null) {
            String message = "lineup players can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (lineupPlayers.getPlayers().size() != 11) {
            String message = String.format("invalid player size, required 11, but got %s",
                    lineupPlayers.getPlayers().size());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        LineupFormationFragment fragment = new LineupFormationFragment();
        Bundle args = new Bundle();
        args.putSerializable(LINEUP_PLAYERS_KEY, lineupPlayers);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new instance of the fragment.
     *
     * @param formation lineup formation
     * @param players list of players that are already in the lineup
     * @return LineupFormation fragment instance
     */
    public static LineupFormationFragment newInstance(LineupUtils.FORMATION formation,
                                                      List<Player> players) {
        if (formation == null) {
            String message = "formation can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (players == null) {
            String message = "players can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        LineupFormationFragment fragment = new LineupFormationFragment();
        Bundle args = new Bundle();
        args.putSerializable(FORMATION_KEY, formation);
        args.putSerializable(LIST_PLAYERS_KEY, new LineupPlayers(players));
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new instance of the fragment with a empty list of players.
     *
     * @param formation lineup formation
     * @return new instance of the fragment
     */
    public static LineupFormationFragment newInstance(LineupUtils.FORMATION formation) {
        return newInstance(formation, new ArrayList<Player>());
    }

    /**
     * Set the fragment presenter.
     *
     * @param presenter fragment presenter
     */
    @Inject
    public void setPresenter(LineupFormationFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the fragment is reay to be created.
     *
     * @param savedInstanceState saved state if the fragment is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new LineupFormationFragmentModule(this)).inject(this);
        presenter.onFragmentCreated(this.getArguments());
    }

    /**
     * Called when the fragment view is ready to be created.
     *
     * @param inflater           LayoutInflater
     * @param container          fragment ViewGroup
     * @param savedInstanceState saved state for when the fragment is recreated
     * @return fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        LineupUtils.FORMATION formation = presenter.getFormation();
        int layoutId = -1;
        if (formation == LineupUtils.FORMATION.F_4_4_2) {
            layoutId = R.layout.lineup_formation_4_4_2;
        } else if (formation == LineupUtils.FORMATION.F_3_2_3_2) {
            layoutId = R.layout.lineup_formation_3_2_3_2;
        } else if (formation == LineupUtils.FORMATION.F_4_2_3_1) {
            layoutId = R.layout.lineup_formation_4_2_3_1;
        } else if (formation == LineupUtils.FORMATION.F_4_3_3) {
            layoutId = R.layout.lineup_formation_4_3_3;
        }
        if (layoutId == -1) {
            throw new IllegalArgumentException("can't determine lineup formation");
        }

        view = inflater.inflate(layoutId, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.onViewCreated();
        return view;
    }

    /**
     * Called before the fragment view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Bind the players data to tbe view.
     */
    public void bindPlayers() {
        keeper.setText(presenter.getPlayerAt(R.id.keeper));
        keeper.setOnClickListener(this);
        leftCentreBack.setText(presenter.getPlayerAt(R.id.leftCentreBack));
        leftCentreBack.setOnClickListener(this);
        rightCentreBack.setText(presenter.getPlayerAt(R.id.rightCentreBack));
        rightCentreBack.setOnClickListener(this);
        if (leftBack != null) {
            leftBack.setText(presenter.getPlayerAt(R.id.leftBack));
            leftBack.setOnClickListener(this);
        }
        if (rightBack != null) {
            rightBack.setText(presenter.getPlayerAt(R.id.rightBack));
            rightBack.setOnClickListener(this);
        }
        if (centreCentreBack != null) {
            centreCentreBack.setText(presenter.getPlayerAt(R.id.centreCentreBack));
            centreCentreBack.setOnClickListener(this);
        }
        leftCentreMidfield.setText(presenter.getPlayerAt(R.id.leftCentreMidfield));
        leftCentreMidfield.setOnClickListener(this);
        rightCentreMidfield.setText(presenter.getPlayerAt(R.id.rightCentreMidfield));
        rightCentreMidfield.setOnClickListener(this);
        if (centreCentreMidfield != null) {
            centreCentreMidfield.setText(presenter.getPlayerAt(R.id.centreCentreMidfield));
            centreCentreMidfield.setOnClickListener(this);
        }
        if (leftWing != null) {
            leftWing.setText(presenter.getPlayerAt(R.id.leftWing));
            leftWing.setOnClickListener(this);
        }
        if (rightWing != null) {
            rightWing.setText(presenter.getPlayerAt(R.id.rightWing));
            rightWing.setOnClickListener(this);
        }
        if (attackingMidfield != null) {
            attackingMidfield.setText(presenter.getPlayerAt(R.id.attackingMidfield));
            attackingMidfield.setOnClickListener(this);
        }
        if (leftCentreForward != null) {
            leftCentreForward.setText(presenter.getPlayerAt(R.id.leftCentreForward));
            leftCentreForward.setOnClickListener(this);
        }
        if (rightCentreForward != null) {
            rightCentreForward.setText(presenter.getPlayerAt(R.id.rightCentreForward));
            rightCentreForward.setOnClickListener(this);
        }
        if (centreCentreForward != null) {
            centreCentreForward.setText(presenter.getPlayerAt(R.id.centreCentreForward));
            centreCentreForward.setOnClickListener(this);
        }
    }

    /**
     * Handle click on views containing the players.
     *
     * @param view View that was clicked
     */
    @Override
    public void onClick(View view) {
        presenter.onPlayerClick(view.getId());
    }

    /**
     * Show the ListPlayers fragment.
     *
     * @param place position place on the field for which the players should be listed
     * @param playersToExclude array of players id to exclude from the list
     */
    public void showListPositionPlayersFragment(PositionUtils.POSITION_PLACE place,
                                                int[] playersToExclude) {
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity())
                    .showListPositionPlayersFragment(place, playersToExclude);
        }
    }

    /**
     * Show PlayerDetails fragment.
     *
     * @param id player id
     * @param editable whatever the player is editable
     */
    public void showPlayerDetailsDialog(int id, boolean editable) {
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).showPlayerDetailsDialog(id, editable);
        }
    }

    /**
     * Update the lineup position with the new player.
     *
     * @param player new player on the the position
     */
    public void updateLineupPosition(Player player) {
        presenter.updateLineupPosition(player);
    }

    /**
     * Remove the player on the selected lineup position.
     */
    public void removeSelectedPlayer() {
        presenter.removeSelectedPlayer();
    }

    /**
     * Called when selecting a player from the ListPositionPlayer is canceled.
     */
    public void onPlayerSelectedCanceled() {
        presenter.onPlayerSelectCanceled();
    }

    /**
     * Called when the formation is valid.
     */
    public void lineupValid() {
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).onValidLineup();
        }
    }

    /**
     * Called when the formation is invalid.
     */
    public void lineupInvalid() {
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).onInvalidLineup();
        }
    }

    /**
     * Get the formation for the lineup.
     *
     * @return lineup formation
     */
    public LineupUtils.FORMATION getFormation() {
        return presenter.getFormation();
    }

    /**
     * Get all player with their position in the lineup.
     *
     * @return List of player in the lineup.
     */
    public List<LineupPlayer> getLineupPlayers() {
        return presenter.getLineupPlayers();
    }

    /**
     * Get all players in the lineup ordered by their position in the lienup.
     *
     * @return List of players
     */
    public List<Player> getPlayersOrdered() {
        return presenter.getPlayersOrdered();
    }

    /**
     * Fragment listener used for communication with the activity.
     */
    public interface Listener {

        void showListPositionPlayersFragment(PositionUtils.POSITION_PLACE place,
                                             int[] playersToExclude);

        void showPlayerDetailsDialog(int id, boolean editable);

        void onValidLineup();

        void onInvalidLineup();
    }
}