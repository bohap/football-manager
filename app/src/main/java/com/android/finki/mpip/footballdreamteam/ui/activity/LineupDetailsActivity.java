package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupDetailsActivityModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.adapter.LineupsDetailsViewPagerAdapter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupDetailsActivityPresenter;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 13.08.2016.
 */
public class LineupDetailsActivity extends BaseActivity {

    public static final String LINEUP_ID_BUNDLE_KEY = "lineup_id";

    @Inject
    LineupDetailsActivityPresenter presenter;

    @Inject
    LineupsDetailsViewPagerAdapter pagerAdapter;

    @BindString(R.string.lineupDetailsActivity_title)
    String title;

    @BindString(R.string.lineupDetailsActivity_spinnerLoadingLineup_text)
    String spinnerText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindView(R.id.error_loading)
    RelativeLayout errorLoadingLayout;

    @BindView(R.id.lineupDetailsLayout_mainContent)
    LinearLayout mainContent;

    @BindView(R.id.lineupDetailsLayout_user)
    TextView txtLineupUser;

    @BindView(R.id.lineupDetailsActivity_tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.lineupDetailsActivity_viewPager)
    ViewPager viewPager;

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved instance state when the activity is recreated.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lineup_details_layout);
        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getUserComponent()
                .plus(new LineupDetailsActivityModule(this)).inject(this);

        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        presenter.loadLineupData(this.getIntent().getExtras());
    }

    /**
     * Called when the lineup data has been started loading from the server.
     */
    public void showLoading() {
        txtSpinner.setText(spinnerText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(mainContent, false);
    }

    /**
     * Called when loading lineup data is successful.
     *
     * @param lineup loaded Lineup
     */
    public void successLoading(Lineup lineup) {
        txtLineupUser.setText(String.format("by %s", lineup.getUser().getName()));
        this.setupViewPager(lineup);

        super.toggleVisibility(mainContent, true);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(spinner, false);
    }

    /**
     * Called when a error occurred while loading lineup data.
     */
    public void errorLoading() {
        super.toggleVisibility(errorLoadingLayout, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(mainContent, false);
    }

    /**
     * Setup the view pager.
     *
     * @param lineup loaded Lineup
     */
    void setupViewPager(Lineup lineup) {
        pagerAdapter.setLineup(lineup);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabViewAt(i));
            }
        }
    }

    /**
     * Handle click on the btn "Try Again".
     */
    @OnClick(R.id.error_loading_btn_tryAgain)
    void reload() {
        presenter.loadLineupData(this.getIntent().getExtras());
    }

    /**
     * Show the LineupPlayerActivity when "View Players" button is clicked.
     */
    @OnClick(R.id.lineupDetailsLayout_btnShowPlayers)
    void showLineupPlayersActivity() {
        Intent intent = new Intent(this, LineupPlayersActivity.class);
        intent.putExtra(LineupPlayersActivity.LINEUP_BUNDLE_KEY, presenter.getLineup());
        this.startActivity(intent);
    }

    /**
     * Handle pressing the back button.
     */
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            super.onBackPressed();
        }
    }
}