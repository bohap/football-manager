package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.ListLineupsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
    private User user;
    private Call<List<Lineup>> call;
    private boolean requestSending = false;
    private boolean viewLayoutCreated = false;
    static final int LINEUPS_LIMIT = 20;
    private int lineupCounter = 0;
    private List<Lineup> lineups = new ArrayList<>();
    private Queue<DeleteCall> deleteCalls = new ArrayDeque<>();

    public ListLineupsViewPresenter(ListLineupsView view, LineupApi api, User user) {
        this.view = view;
        this.api = api;
        this.user = user;
    }

    /**
     * Called when the view is created.
     */
    public void onViewCreated() {
        logger.info("onViewCreated");
        this.loadLineups(true);
    }

    /**
     * Called when the view layout is created.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        viewLayoutCreated = true;
        if (requestSending) {
            view.showLoading();
        }
    }

    /**
     * Get all the loaded lineups.
     *
     * @return loaded lineups
     */
    public List<Lineup> getLineups() {
        return lineups;
    }

    /**
     * Get the authenticated user.
     *
     * @return authenticated
     */
    public User getUser() {
        return user;
    }

    /**
     * Load the lineups data from the server.
     *
     * @param callView whatever the view method should be called
     */
    public void loadLineups(boolean callView) {
        if (!requestSending) {
            logger.info("loading lineups data");
            requestSending = true;
            if (viewLayoutCreated && callView) {
                view.showLoading();
            }
            call = this.api.index(null, true, LINEUPS_LIMIT, lineupCounter * LINEUPS_LIMIT);
            call.enqueue(new Callback<List<Lineup>>() {
                @Override
                public void onResponse(Call<List<Lineup>> call, Response<List<Lineup>> response) {
                    onLineupsLoadingSuccess(response);
                }

                @Override
                public void onFailure(Call<List<Lineup>> call, Throwable t) {
                    onLineupsLoadingFailed(call, t);
                }
            });
        }
    }

    /**
     * Called when loading the lineup data is successful.
     *
     * @param response server response
     */
    private void onLineupsLoadingSuccess(Response<List<Lineup>> response) {
        logger.info("loading lineups data success");
        requestSending = false;
        this.call = null;
        lineupCounter++;
        List<Lineup> body = response.body();
        lineups.addAll(body);
        if (viewLayoutCreated) {
            view.showLoadingSuccess(body);
            if (body.size() < LINEUPS_LIMIT) {
                view.showNoMoreLineups();
            }
        }
    }

    /**
     * Called when loading the lineups data failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void onLineupsLoadingFailed(Call<List<Lineup>> call, Throwable t) {
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

    /**
     * Refresh the lineup data from the server.
     */
    public void refresh() {
        if (!requestSending) {
            requestSending = true;
            logger.info("refreshing lineups data");
            if (viewLayoutCreated) {
                view.showRefreshing();
            }
            final int limit = (lineupCounter + 1) * LINEUPS_LIMIT;
            call = api.index(false, true, limit, 0);
            call.enqueue(new Callback<List<Lineup>>() {
                @Override
                public void onResponse(Call<List<Lineup>> call, Response<List<Lineup>> response) {
                    onRefreshSuccess(response);
                }

                @Override
                public void onFailure(Call<List<Lineup>> call, Throwable t) {
                    onRefreshFailed(call, t);
                }
            });
        }
    }

    /**
     * Called when refreshing the lineup data is successful.
     *
     * @param response server response
     */
    private void onRefreshSuccess(Response<List<Lineup>> response) {
        logger.info("refresh lineups success");
        requestSending = false;
        call = null;
        List<Lineup> lineups = response.body();
        this.lineups = lineups;
        if (viewLayoutCreated) {
            view.showLoadingSuccess(lineups);
        }
    }

    /**
     * Called when refreshing the lineup data failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void onRefreshFailed(Call<List<Lineup>> call, Throwable t) {
        logger.info("onRefreshFailed");
        requestSending = false;
        if (call.isCanceled()) {
            logger.info("refresh lineups canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showRefreshingFailed();
                super.onRequestFailed(view, t);
            }
        }
        this.call = null;
    }

    /**
     * Execute the next call from the queue of delete calls.
     */
    private void executeCall() {
        DeleteCall call = deleteCalls.peek();
        if (call != null && !call.isSending()) {
            call.execute();
        }
    }

    /**
     * Send a request for deleting the lineup.
     *
     * @param lineup lineup to be deleted
     */
    public void deleteLineup(Lineup lineup) {
        deleteCalls.add(new DeleteCall(lineup));
        this.executeCall();
    }

    /**
     * Called when deleting the lineup is successful.
     *
     * @param lineup deleted lineup
     */
    public void onLineupDeletingSuccess(Lineup lineup) {
        logger.info(String.format("delete lineup success, lineup id %d", lineup.getId()));
        if (viewLayoutCreated) {
            view.showLineupDeletingSuccess(lineup);
        }
        deleteCalls.poll();
        this.executeCall();
    }

    /**
     * Called when deleting the lineup failed.
     *
     * @param lineup failed deleted lineup
     * @param call   retrofit call
     * @param t      exception that has been thrown
     */
    public void onLineupDeletingFailed(Lineup lineup, Call<Void> call, Throwable t) {
        logger.info(String.format("delete lineup failed, lineup %d", lineup.getId()));
        if (call.isCanceled()) {
            logger.info(String.format("delete lineup canceled, lineup %d", lineup.getId()));
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showLineupDeletingFailed(lineup);
                super.onRequestFailed(view, t);
            }
        }
        deleteCalls.poll();
        this.executeCall();
    }

    /**
     * Called when the view layout is destroyed.
     */
    public void onViewLayoutDestroyed() {
        logger.info("onViewLayoutDestroyed");
        this.viewLayoutCreated = false;
    }

    /**
     * Called when the view is destroyed.
     */
    public void onViewDestroyed() {
        logger.info("onViewDestroyed");
        if (call != null) {
            call.cancel();
        }
        DeleteCall deleteCall = deleteCalls.peek();
        if (deleteCall != null) {
            deleteCall.cancel();
        }
    }

    /**
     * Wrapper class for deleting lineup call.
     */
    public class DeleteCall implements Callback<Void> {

        private Lineup lineup;
        private Call<Void> call;

        public DeleteCall(Lineup lineup) {
            this.lineup = lineup;
        }

        /**
         * Send a request for deleting the lineup.
         */
        public void execute() {
            logger.info(String.format("delete lineup request, lineup id %d", lineup.getId()));
            call = api.delete(lineup.getId());
            call.enqueue(this);
        }

        /**
         * Called when deleting the lineup is successful.
         *
         * @param call     retrofit call
         * @param response server response
         */
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            onLineupDeletingSuccess(lineup);
        }

        /**
         * Called when deleting the lineup failed.
         *
         * @param call retrofit call
         * @param t    exception that has been thrown
         */
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            onLineupDeletingFailed(lineup, call, t);
        }

        /**
         * Cancel the request.
         */
        public void cancel() {
            if (call != null) {
                call.cancel();
            }
        }

        /**
         * Checks whatever a request is sending.
         *
         * @return whatever the request is sending
         */
        public boolean isSending() {
            return call != null;
        }
    }
}
