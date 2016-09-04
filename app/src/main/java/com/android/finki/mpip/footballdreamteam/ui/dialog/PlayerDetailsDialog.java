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
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsDialogModule;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsDialogPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 20.08.2016.
 */
public class PlayerDetailsDialog extends DialogFragment {

    private static Logger logger = LoggerFactory.getLogger(PlayerDetailsDialog.class);
    public static final String TAG = "PLAYER_DETAILS_DIALOG";
    private static final String BUNDLE_PLAYER_ID_KEY = "player_id";
    private static final String BUNDLE_EDITABLE_KEY = "editable";

    private PlayerDetailsDialogPresenter presenter;

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
            String message = String.format("invalid player id, %d", playerId);
            logger.error(message);
            throw new IllegalArgumentException(message);
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
    public void setPresenter(PlayerDetailsDialogPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Get the key to the bundle where the player id is saved.
     *
     * @return bundle key
     */
    public static String getBundlePlayerIdKey() {
        return BUNDLE_PLAYER_ID_KEY;
    }

    /**
     * Get the key to the bundle where the editable value is saved,
     *
     * @return bundle key
     */
    public static String getBundleEditableKey() {
        return BUNDLE_EDITABLE_KEY;
    }

    /**
     * Called when the dialog is ready to be created.
     *
     * @param savedInstanceState saved state from when the dialog is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new PlayerDetailsDialogModule(this)).inject(this);
        this.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        presenter.onDialogCreated(this.getArguments());
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
        View view = inflater.inflate(R.layout.player_details_layout, container, false);
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        presenter.onViewCreated();
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
    public void bindPlayer(String name, String team, String age,
                           String position, boolean editable) {
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
