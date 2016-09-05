package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.LineupDetailsActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 15.08.2016.
 */
public class LineupDetailsActivityPresenter implements Callback<Lineup> {

    private static Logger logger = LoggerFactory.getLogger(LineupDetailsActivityPresenter.class);

    private LineupDetailsActivity activity;
    private LineupApi api;

    private Lineup lineup;

    public LineupDetailsActivityPresenter(LineupDetailsActivity activity, LineupApi api) {
        this.activity = activity;
        this.api = api;
    }

    /**
     * Load the lineup data from the server.
     *
     * @param args view arguments
     */
    public void loadLineupData(Bundle args) {
        int lineupId = this.extractLineupId(args);
        activity.showLoading();
        logger.info("sending lineup request");
        api.get(lineupId).enqueue(this);
    }

    /**
     * Extract the lineup id from the given bundle.
     *
     * @param args view arguments
     */
    private int extractLineupId(Bundle args) {
        if (args == null) {
            String message = "bundle can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        int lineupId = args.getInt(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY, -1);
        if (lineupId < 1) {
            String message = "lineup id must be provided for lineup details view";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return lineupId;
    }

    /**
     * Called when loading the lineup data is successful.
     *
     * @param call retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<Lineup> call, Response<Lineup> response) {
        if (response.isSuccessful()) {
            logger.info("lineup request success");
            this.lineup = response.body();
            activity.successLoading(response.body());
        } else {
            logger.info("lineup request failed");
            activity.errorLoading();
        }
    }

    /**
     * Called when loading the lineup data failed.
     *
     * @param call retrofit call
     * @param t exception that has been thrown
     */
    @Override
    public void onFailure(Call<Lineup> call, Throwable t) {
        logger.info("lineup request failed");
        activity.errorLoading();
        if (t instanceof SocketTimeoutException) {
            activity.showConnectionTimeoutMessage();
        } else {
            activity.showServerErrorMessage();
        }
    }

    /**
     * Get the id of the loaded lineup.
     *
     * @return lineup id
     */
    public int getLineupId() {
        return lineup.getId();
    }

    /**
     * Get the loaded lineup.
     *
     * @return loaded lineup
     */
    public Lineup getLineup() {
        return this.lineup;
    }
}
