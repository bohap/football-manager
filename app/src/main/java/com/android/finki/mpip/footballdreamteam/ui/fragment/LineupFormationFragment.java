package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationViewModule;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.helpers.SerializableList;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationViewPresenter;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils.FORMATION;
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
public class LineupFormationFragment extends BaseFragment
                                     implements LineupFormationView,
                                                View.OnClickListener {
    private static Logger logger = LoggerFactory.getLogger(LineupFormationFragment.class);
    public static final String TAG = "LineupFormationFragment";

    private LineupFormationViewPresenter presenter;

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
     * @param players  List of players in the lineup
     * @param editable whatever the lineup can be edited
     * @return new instance of the fragment
     */
    public static LineupFormationFragment newInstance(List<Player> players, boolean editable) {
        if (players == null) {
            throw new IllegalArgumentException("players can't be null");
        }
        if (players.size() != 11) {
            throw new IllegalArgumentException(
                    String.format("invalid player size, required 11, but got %s", players.size()));
        }
        LineupFormationFragment fragment = new LineupFormationFragment();
        Bundle args = new Bundle();
        args.putSerializable(LINEUP_PLAYERS_KEY, new SerializableList<>(players));
        args.putBoolean(LINEUP_EDITABLE_KEY, editable);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new instance of the fragment.
     *
     * @param formation lineup formation
     * @param players   list of players that are already in the lineup
     * @return LineupFormation fragment instance
     */
    public static LineupFormationFragment newInstance(FORMATION formation, List<Player> players) {
        if (formation == null) {
            throw new IllegalArgumentException("formation can't be null");
        }
        if (players == null) {
            throw new IllegalArgumentException("players can't be null");
        }
        LineupFormationFragment fragment = new LineupFormationFragment();
        Bundle args = new Bundle();
        args.putSerializable(FORMATION_KEY, formation);
        args.putSerializable(LIST_PLAYERS_KEY, new SerializableList<>(players));
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new instance of the fragment with a empty list of players.
     *
     * @param formation lineup formation
     * @return new instance of the fragment
     */
    public static LineupFormationFragment newInstance(FORMATION formation) {
        if (formation == null) {
            throw new IllegalArgumentException("formation can't be null");
        }
        return newInstance(formation, new ArrayList<Player>());
    }

    /**
     * Set the fragment presenter.
     *
     * @param presenter fragment presenter
     */
    @Inject
    public void setPresenter(LineupFormationViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the fragment is reay to be created.
     *
     * @param savedInstanceState saved state if the fragment is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        if (this.getActivity() instanceof BaseFragment.Listener) {
            ((BaseFragment.Listener) this.getActivity()).onFragmentActive();
        }
        ((MainApplication) this.getActivity().getApplication()).getAuthComponent()
                .plus(new LineupFormationViewModule(this)).inject(this);
        presenter.onViewCreated(this.getArguments());
    }

    /**
     * Called when the fragment view is ready to be created.
     *
     * @param inflater              the layout inflater
     * @param container             fragment ViewGroup
     * @param savedInstanceState    saved state for when the fragment is recreated
     * @return                      fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("onCreateView");
        View view;
        FORMATION formation = presenter.getFormation();
        if (formation == null) {
            throw new IllegalArgumentException("formation can't be null");
        }

        int layoutId;
        switch (formation) {
            case F_4_4_2:
                layoutId = R.layout.lineup_formation_4_4_2;
                break;
            case F_3_2_3_2:
                layoutId = R.layout.lineup_formation_3_2_3_2;
                break;
            case F_4_2_3_1:
                layoutId = R.layout.lineup_formation_4_2_3_1;
                break;
            case F_4_3_3:
                layoutId = R.layout.lineup_formation_4_3_3;
                break;
            default:
                throw new IllegalArgumentException("can't determine lineup formation");
        }

        view = inflater.inflate(layoutId, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.onViewLayoutCreated();
        return view;
    }

    /**
     * Called before the fragment view is destroyed.
     */
    @Override
    public void onDestroyView() {
        logger.info("onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
        presenter.onViewLayoutDestroyed();
    }

    /**
     * Bind the players data to tbe view.
     */
    @Override
    public void bindPlayers() {
        logger.info("bindPlayers");
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
        logger.info("player clicked");
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        presenter.onPlayerClick(view.getId(), location[0], location[1]);
    }

    /**
     * Show the ListPlayers fragment.
     *
     * @param place            position place on the field for which the players should be listed
     * @param playersToExclude array of players id to exclude from the list
     * @param startX           view x position
     * @param startY           view y position
     */
    @Override
    public void showListPositionPlayersView(PositionUtils.POSITION_PLACE place,
                                            int[] playersToExclude, int startX, int startY) {
        logger.info("showListPositionsPlayersView");
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity())
                    .showListPositionPlayersFragment(place, playersToExclude, startX, startY);
        }
    }

    /**
     * Show PlayerDetails fragment.
     *
     * @param id       player id
     * @param editable whatever the player is editable
     */
    @Override
    public void showPlayerDetailsView(int id, boolean editable) {
        logger.info("showPlayerDetailsView");
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
        logger.info("updatePlayerPosition");
        presenter.updateLineupPosition(player);
    }

    /**
     * Remove the player on the selected lineup position.
     */
    public void removeSelectedPlayer() {
        logger.info("onRemoveSuccess selected player");
        presenter.removeSelectedPlayer();
    }

    /**
     * Called when selecting a player from the ListPositionPlayer is canceled.
     */
    public void onPlayerSelectCanceled() {
        logger.info("onPlayerSelectCanceled");
        presenter.onPlayerSelectCanceled();
    }

    /**
     * Called when the formation is valid.
     */
    @Override
    public void showValidLineup() {
        logger.info("showValidLineup");
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).showValidLineup();
        }
    }

    /**
     * Called when the formation is invalid.
     */
    @Override
    public void showInvalidLineup() {
        logger.info("showInvalidLineup");
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).showInvalidLineup();
        }
    }

    /**
     * Get the formation for the lineup.
     *
     * @return lineup formation
     */
    public FORMATION getFormation() {
        logger.info("getFormation");
        return presenter.getFormation();
    }

    /**
     * Get all player with their position in the lineup.
     *
     * @return List of player in the lineup.
     */
    public List<LineupPlayer> getLineupPlayers() {
        logger.info("getLineupPlayers");
        return presenter.getLineupPlayers();
    }

    /**
     * Get all players in the lineup ordered by their position in the lienup.
     *
     * @return List of players
     */
    public List<Player> getPlayersOrdered() {
        logger.info("getPlayerOrdered");
        return presenter.getPlayersOrdered();
    }

    /**
     * Fragment listener used for communication with the activity.
     */
    public interface Listener {

        void showListPositionPlayersFragment(PositionUtils.POSITION_PLACE place,
                                             int[] playersToExclude, int startX, int startY);

        void showPlayerDetailsDialog(int id, boolean editable);

        void showValidLineup();

        void showInvalidLineup();
    }
}