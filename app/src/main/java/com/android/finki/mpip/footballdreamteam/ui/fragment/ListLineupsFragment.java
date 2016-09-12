package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListLineupsViewModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListLineupsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.component.ListLineupsView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListLineupsViewPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Borce on 05.09.2016.
 */
public class ListLineupsFragment extends BaseFragment implements ListLineupsView,
        ListLineupsAdapter.Listener {

    private Logger logger = LoggerFactory.getLogger(ListLineupsFragment.class);
    public static final String TAG = "LIST_LINEUPS_FRAGMENT";
    private ListLineupsViewPresenter presenter;
    private Unbinder unbinder;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindString(R.string.listLineupsLayout_spinner_text)
    String spinnerLineupText;

    @BindView(R.id.error)
    RelativeLayout error;

    @BindView(R.id.txtError)
    TextView txtError;

    @BindString(R.string.lineupPlayersActivity_loadingLineupFailed_text)
    String loadingLineupsFailedText;

    @BindView(R.id.listLineupsLayout_content)
    RelativeLayout content;

    @BindView(R.id.listLineupsLayout_listVIew)
    ListView listView;

    @BindString(R.string.listLineupLayout_deletingSuccess_text)
    String deletingSuccessText;

    @BindString(R.string.listLineupLayout_deletingFailed_text)
    String deletingFailedText;

    private ListLineupsAdapter adapter;
    private LineupsListViewFooterHolder listViewFooterHolder;

    @Inject
    public void setPresenter(ListLineupsViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the fragment is ready to be created.
     *
     * @param savedInstanceState bundle state for when the fragment is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        if (this.getActivity() instanceof BaseFragment.Listener) {
            ((BaseFragment.Listener) this.getActivity()).onFragmentActive();
        }
        ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new ListLineupsViewModule(this)).inject(this);
        presenter.onViewCreated();
    }

    /**
     * Called when the fragment view is ready to be created.
     *
     * @param inflater           system layout inflater service
     * @param container          fragment container
     * @param savedInstanceState saved state for when the fragment is recreated
     * @return fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.info("onCreateView");
        View view = inflater.inflate(R.layout.list_lineups_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.onViewLayoutCreated();
        View footer = inflater.inflate(R.layout.lineups_footer, null);
        listView.addFooterView(footer);
        listViewFooterHolder = new LineupsListViewFooterHolder(footer);
        adapter = new ListLineupsAdapter(this.getActivity(), presenter.getUser(), this);
        listView.setAdapter(adapter);
        adapter.update(presenter.getLineups());
        return view;
    }

    /**
     * Called before the fragment view is destroyer.
     */
    @Override
    public void onDestroyView() {
        logger.info("onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
        presenter.onViewLayoutDestroyed();
        presenter.onViewDestroyed();
    }

    /**
     * Called before the fragment is destroyed.
     */
    @Override
    public void onDestroy() {
        logger.info("onDestroy");
        super.onDestroy();
        presenter.onViewDestroyed();
    }

    /**
     * Show the spinner for when the lineups data is loading.
     */
    @Override
    public void showLoading() {
        logger.info("showLoading");
        txtSpinner.setText(spinnerLineupText);
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    /**
     * Called when loading hte lineups is successful.
     *
     * @param lineups list of Lineups
     */
    @Override
    public void showLoadingSuccess(List<Lineup> lineups) {
        logger.info("showLoadingSuccess");
        content.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        assert listViewFooterHolder.lineupsListViewSpinner != null;
        listViewFooterHolder.lineupsListViewSpinner.setVisibility(View.GONE);
        adapter.update(lineups);
    }

    /**
     * Called when loading the lineup failed.
     */
    public void showLoadingFailed() {
        logger.info("showLoadingFailed");
        txtError.setText(loadingLineupsFailedText);
        error.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    /**
     * Handle click on the button to load lineups again.
     */
    @OnClick(R.id.error_btnTryAgain)
    void reload() {
        logger.info("btn 'Try Again' clicked");
        presenter.loadLineups();
    }

    /**
     * Refresh the lineups data.
     */
    public void refresh() {
        logger.info("refresh");
        presenter.refresh();
    }

    /**
     * Called when the lineup players from the list has been selected.
     *
     * @param lineup selected lineup in the list
     */
    @Override
    public void onLineupPlayersSelected(Lineup lineup) {
        logger.info("onLineupPlayersSelected");
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).showLineupPlayersView(lineup);
        }
    }

    /**
     * Called when lineup likes from the list has been selected.
     *
     * @param lineup selected lineup in the list
     */
    @Override
    public void onLineupLikesSelected(Lineup lineup) {
        logger.info("onLineupLikesSelected");
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).showLineupLikesView(lineup);
        }
    }

    /**
     * Called when lineup comments from the list has been selected.
     *
     * @param lineup selected lineup in the list.
     */
    @Override
    public void onLineupCommentsSelected(Lineup lineup) {
        logger.info("onLineupCommentsSelected");
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).showLineupCommentsView(lineup);
        }
    }

    /**
     * Called when there are not more lineups to be loaded.
     */
    @Override
    public void showNoMoreLineups() {
        logger.info("showNoMoreLineups");
        listViewFooterHolder.hideBtnLoadMore();
    }

    /**
     * Called when the Delete button has been click for the lineup.
     *
     * @param lineup selected lineup
     */
    @Override
    public void onBtnDeleteClick(Lineup lineup) {
        logger.info("onBtnDeleteClick");
        presenter.deleteLineup(lineup);
    }

    /**
     * Called when deleting the lineup is successful.
     *
     * @param lineup deleted lineup
     */
    @Override
    public void showLineupDeletingSuccess(Lineup lineup) {
        logger.info("showLineupDeletingSuccess");
        Toast.makeText(this.getActivity(), deletingSuccessText, Toast.LENGTH_SHORT).show();
        adapter.delete(lineup);
    }

    /**
     * Called when deleting the lineup failed.
     *
     * @param lineup failed deleted lineup
     */
    @Override
    public void showLineupDeletingFailed(Lineup lineup) {
        logger.info("showLineupDeletingFailed");
        Toast.makeText(this.getActivity(), deletingFailedText, Toast.LENGTH_SHORT).show();
        adapter.onDeletingFailed(lineup);
    }

    /**
     * Class holder for the lineups list view footer.
     */
    class LineupsListViewFooterHolder {

        /**
         * Is marked as nullable because the view is injected from the ButterKnife
         * after is has been added to the list view.
         */
        @BindView(R.id.lineupsListVIew_spinnerLoadMore)
        ProgressBar lineupsListViewSpinner;

        @BindView(R.id.lineupsListView_btnLoadMore)
        RelativeLayout btnLoadMore;

        LineupsListViewFooterHolder(View view) {
            ButterKnife.bind(this, view);
        }

        /**
         * Load more lineups when the button load more is clicked.
         */
        @OnClick(R.id.lineupsListView_btnLoadMore)
        void loadMore() {
            logger.info("btn 'Load More' clicked");
            lineupsListViewSpinner.setVisibility(View.VISIBLE);
            presenter.loadMoreLineups();
        }


        /**
         * Hide the load more button.
         */
        public void hideBtnLoadMore() {
            btnLoadMore.setVisibility(View.GONE);
        }
    }

    /**
     * Fragment listener user for communication with the activity.
     */
    public interface Listener {

        void showLineupPlayersView(Lineup lineup);

        void showLineupLikesView(Lineup lineup);

        void showLineupCommentsView(Lineup lineup);
    }
}
