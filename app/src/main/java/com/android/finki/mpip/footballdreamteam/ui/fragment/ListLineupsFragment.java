package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListLineupsViewModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListLineupsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.component.ListLineupsView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListLineupsViewPresenter;

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
public class ListLineupsFragment extends Fragment implements ListLineupsView {

    public static final String TAG = "LIST_LINEUPS_FRAGMENT";
    private ListLineupsViewPresenter presenter;
    private Unbinder unbinder;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindString(R.string.listLineupsLayout_spinner_text)
    String spinnerLineupText;

    @BindView(R.id.error_loading)
    RelativeLayout errorLoadingLayout;

    @BindView(R.id.listLineupsLayout_content)
    RelativeLayout content;

    @BindView(R.id.listLineupsLayout_listVIew)
    ListView listView;

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
        super.onCreate(savedInstanceState);
        if (! (this.getActivity() instanceof ListLineupsAdapter.Listener)) {
            throw new IllegalArgumentException(
                    "activity must implement ListLineupsAdapter.Listener");
        }
        ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new ListLineupsViewModule(this)).inject(this);
        presenter.loadLineups();
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
        View view = inflater.inflate(R.layout.list_lineups_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.onViewCreated();
        View footer = inflater.inflate(R.layout.lineups_footer, null);
        listViewFooterHolder = new LineupsListViewFooterHolder(footer);
        listView.addFooterView(footer);
        adapter = new ListLineupsAdapter(this.getActivity(),
                (ListLineupsAdapter.Listener)this.getActivity());
        listView.setAdapter(adapter);
        adapter.update(presenter.getLineups());
        return view;
    }

    /**
     * Called before the fragment view is destroyer.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Show the spinner for when the lineups data is loading.
     */
    @Override
    public void showLoading() {
        txtSpinner.setText(spinnerLineupText);
        spinner.setVisibility(View.VISIBLE);
        errorLoadingLayout.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    /**
     * Called when loading hte lineups is successful.
     *
     * @param lineups list of Lineups
     */
    @Override
    public void showLoadingSuccess(List<Lineup> lineups) {
        content.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        errorLoadingLayout.setVisibility(View.GONE);
        assert listViewFooterHolder.lineupsListViewSpinner != null;
        listViewFooterHolder.lineupsListViewSpinner.setVisibility(View.GONE);
        adapter.update(lineups);
    }

    /**
     * Called when loading the lineup failed.
     */
    public void showLoadingFailed() {
        errorLoadingLayout.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    /**
     * Handle click on the button to load lineups again.
     */
    @OnClick(R.id.error_loading_btn_tryAgain)
    void reload() {
        presenter.loadLineups();
    }

    /**
     * Refresh the lineups data.
     */
    public void refresh() {
        presenter.refresh();
    }

    /**
     * Class holder for the lineups list view footer.
     */
    class LineupsListViewFooterHolder {

        /**
         * Is marked as nullable because the view is injected from the ButterKnife after is has been
         * added to the list view.
         */
        @Nullable
        @BindView(R.id.lineupsListVIew_spinnerLoadMore)
        ProgressBar lineupsListViewSpinner;

        LineupsListViewFooterHolder(View view) {
            ButterKnife.bind(this, view);
        }

        /**
         * Load more lineups when the button load more is clicked.
         */
        @OnClick(R.id.lineupsListView_btnLoadMore)
        void loadMore() {
            assert lineupsListViewSpinner != null;
            lineupsListViewSpinner.setVisibility(View.VISIBLE);
            presenter.loadMoreLineups();
        }
    }
}
