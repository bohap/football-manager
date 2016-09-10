package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.ListLineupsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ListLineupsViewPresenter extends BasePresenter {

    private static final Logger logger = LoggerFactory.getLogger(ListLineupsViewPresenter.class);
    private ListLineupsView view;
    private LineupApi api;
    private Call<List<Lineup>> call;
    private boolean requestSending = false;
    private boolean viewLayoutCreated = false;
    static final int LINEUPS_LIMIT = 20;
    private int lineupCounter = 0;
    private Set<Lineup> lineups = new LinkedHashSet<>();

    public ListLineupsViewPresenter(ListLineupsView view, LineupApi api) {
        this.view = view;
        this.api = api;
    }

    /**
     * Called when the view is created.
     */
    public void onViewCreated() {
        this.loadLineups();
    }

    /**
     * Called when the view layout is created.
     */
    public void onViewLayoutCreated() {
        viewLayoutCreated = true;
        if (requestSending) {
            view.showLoading();
        }
    }

    /**
     * Called when the view layout is destroyed.
     */
    public void onViewLayoutDestroyed() {
        this.viewLayoutCreated = false;
    }

    /**
     * Called when the view is destroyed.
     */
    public void onViewDestroyed() {
        if (call != null) {
            call.cancel();
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
        if (!requestSending) {
            logger.info("loading lineups data");
            requestSending = true;
            if (viewLayoutCreated) {
                view.showLoading();
            }
            this.loadData(LINEUPS_LIMIT, lineupCounter * LINEUPS_LIMIT);
        }
    }

    /**
     * Load more lineup from the server.
     */
    public void loadMoreLineups() {
        if (!requestSending) {
            logger.info("loading more lineups data");
            requestSending = true;
            this.loadData(LINEUPS_LIMIT, lineupCounter * LINEUPS_LIMIT);
        }
    }

    /**
     * Refresh the lineup data from the server.
     */
    public void refresh() {
        if (!requestSending) {
            requestSending = true;
            logger.info("refreshing lineups data");
            if (viewLayoutCreated) {
                view.showLoading();
            }
            final int limit = lineupCounter * LINEUPS_LIMIT;
            this.loadData(limit, 0);
        }
    }

    /**
     * Send a call to load lineup data from the server.
     */
    private void loadData(int limit, int offset) {
        call = this.api.index(null, true, limit, offset);
        call.enqueue(new Callback<List<Lineup>>() {
            @Override
            public void onResponse(Call<List<Lineup>> call, Response<List<Lineup>> response) {
                loadDataSuccess(response);
            }

            @Override
            public void onFailure(Call<List<Lineup>> call, Throwable t) {
                loadDataFailed(call, t);
            }
        });
    }

    /**
     * Called when loading the lineup data is successful.
     *
     * @param response server response
     */
    private void loadDataSuccess(Response<List<Lineup>> response) {
        logger.info("loading lineups data success");
        requestSending = false;
        this.call = null;
        lineupCounter++;
        lineups.addAll(response.body());
        if (viewLayoutCreated) {
            view.showLoadingSuccess(response.body());
        }
    }

    /**
     * Called when loading the lineups data failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void loadDataFailed(Call<List<Lineup>> call, Throwable t) {
        logger.info("loading lineups request failed");
        requestSending = false;
        if (call.isCanceled()) {
            logger.info("loading lineups request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showLoadingFailed();
                onRequestFailed(view, t);
            }
        }
        this.call = null;
    }
}
