package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.ListLineupsView;
import com.android.finki.mpip.footballdreamteam.utility.ArrayUtils;
import com.android.finki.mpip.footballdreamteam.utility.ListUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 05.09.2016.
 */
public class ListLineupsViewPresenter {

    private static final Logger logger = LoggerFactory.getLogger(ListLineupsViewPresenter.class);
    private ListLineupsView view;
    private LineupApi api;

    private boolean isLineupRequestSending = false;
    private boolean refreshingLineupDataFailed = false;
    private boolean viewCreated = false;
    static final int LINEUPS_LIMIT = 20;
    private int lineupCounter = 0;
    private Set<Lineup> lineups = new LinkedHashSet<>();

    public ListLineupsViewPresenter(ListLineupsView view, LineupApi api) {
        this.view = view;
        this.api = api;
    }

    /**
     * Called when the view has been fully created.
     */
    public void onViewCreated() {
        viewCreated = true;
        if (isLineupRequestSending) {
            view.showLoading();
        }
    }

    /**
     * Get all the loaded lineups.
     *
     * @return loaded lineups
     */
    public List<Lineup> getLineups() {
        return Arrays.asList(lineups.toArray(new Lineup[lineups.size()]));
    }

    /**
     * Load the lineups data from the server.
     */
    public void loadLineups() {
        logger.info("loading lineups data");
        isLineupRequestSending = true;
        if (viewCreated) {
            view.showLoading();
        }
        this.loadData(LINEUPS_LIMIT, lineupCounter * LINEUPS_LIMIT);
    }

    /**
     * Load more lineup from the server.
     */
    public void loadMoreLineups() {
        if (!isLineupRequestSending) {
            isLineupRequestSending = true;
            logger.info("loading more lineups data");
            this.loadData(LINEUPS_LIMIT, lineupCounter * LINEUPS_LIMIT);
        }
    }

    /**
     * Refresh the lineup data from the server.
     */
    public void refresh() {
        if (!isLineupRequestSending) {
            logger.info("refreshing lineups data");
            view.showLoading();
            final int limit = lineupCounter * LINEUPS_LIMIT;
            this.loadData(limit, 0);
        }
    }

    /**
     * Send a call to load lineup data from the server.
     */
    private void loadData(int limit, int offset) {
        Call<List<Lineup>> call = this.api.index(null, true,
                LINEUPS_LIMIT, lineupCounter * LINEUPS_LIMIT);
        call.enqueue(new Callback<List<Lineup>>() {
            @Override
            public void onResponse(Call<List<Lineup>> call, Response<List<Lineup>> response) {
                if (response.isSuccessful()) {
                    logger.info("loading lineups data success");
                    isLineupRequestSending = false;
                    lineupCounter++;
                    view.showLoadingSuccess(response.body());
                    lineups.addAll(response.body());
                } else {
                    view.showLoadingFailed();
                }
            }

            @Override
            public void onFailure(Call<List<Lineup>> call, Throwable t) {
                logger.info("loading lineups request failed");
                isLineupRequestSending = false;
                view.showLoadingFailed();
            }
        });
    }
}
