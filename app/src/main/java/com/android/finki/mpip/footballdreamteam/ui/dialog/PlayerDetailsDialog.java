package com.android.finki.mpip.footballdreamteam.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsViewModule;
import com.android.finki.mpip.footballdreamteam.ui.component.PlayerDetailsView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsViewPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 20.08.2016.
 */
public class PlayerDetailsDialog extends DialogFragment implements PlayerDetailsView {

    private static Logger logger = LoggerFactory.getLogger(PlayerDetailsDialog.class);
    public static final String TAG = "PLAYER_DETAILS_DIALOG";

    private PlayerDetailsViewPresenter presenter;

    @BindView(R.id.playerDetailsLayout_name)
    TextView txtName;

    @BindView(R.id.playerDetailsLayout_team)
    TextView txtTeam;

    @BindView(R.id.playerDetailsLayout_age)
    TextView txtAge;

    @BindView(R.id.playerDetailsLayout_position)
    TextView txtPosition;

    @BindView(R.id.playerDetailsLayout_btnRemove)
    Button btnRemove;

    /**
     * Create a new instance of the dialog.
     *
     * @param playerId player id
     * @param editable whatever the user can removeLike the player from
     * @return new instance of the dialog
     */
    public static PlayerDetailsDialog newInstance(int playerId, boolean editable) {
        if (playerId < 0) {
            throw new IllegalArgumentException(String
                    .format("invalid player id, %d", playerId));
        }
        PlayerDetailsDialog dialog = new PlayerDetailsDialog();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_PLAYER_ID_KEY, playerId);
        args.putBoolean(BUNDLE_EDITABLE_KEY, editable);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * Set the dialog presenter.
     *
     * @param presenter instance of the dialog presenter
     */
    @Inject
    public void setPresenter(PlayerDetailsViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the dialog is ready to be created.
     *
     * @param savedInstanceState saved state from when the dialog is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new PlayerDetailsViewModule(this)).inject(this);
        this.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        presenter.onViewCreated(this.getArguments());
    }

    /**
     * Called when the view is ready to be created.
     *
     * @param inflater           system Layout inflater
     * @param container          dialog viewGroup
     * @param savedInstanceState saved state from when the dialog is recreated
     * @return dialog view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("onCreateView");
        View view = inflater.inflate(R.layout.player_details_layout, container, false);
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        presenter.onViewLayoutCreated();
        return view;
    }

    /**
     * Bind the dialog view with the player.
     *
     * @param name     player name
     * @param team     name of the player team
     * @param age      player age
     * @param position name of the player position
     * @param editable whatever the user can removeLike the player
     */
    @Override
    public void bindPlayer(String name, String team, String age,
                           String position, boolean editable) {
        logger.info("bindPlayer");
        txtName.setText(name);
        txtTeam.setText(team);
        txtAge.setText(age);
        txtPosition.setText(position);
        if (editable) {
            btnRemove.setVisibility(View.VISIBLE);
        } else {
            btnRemove.setVisibility(View.GONE);
        }
    }

    /**
     * Handle click on hte button to removeLike the player.
     */
    @OnClick(R.id.playerDetailsLayout_btnRemove)
    void removePlayer() {
        logger.info("btn 'Remove Player' clicked");
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).removePlayer();
        }
        super.dismiss();
    }

    /**
     * Listener that dialog activity need to implement if they want to listen when
     * the player is removed.
     */
    public interface Listener {
        void removePlayer();
    }
}
