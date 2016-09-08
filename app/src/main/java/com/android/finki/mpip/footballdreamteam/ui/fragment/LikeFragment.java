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
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LikeViewModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;
import com.android.finki.mpip.footballdreamteam.ui.adapter.LikesAdapter;
import com.android.finki.mpip.footballdreamteam.ui.component.LikeView;
import com.android.finki.mpip.footballdreamteam.ui.listener.ActivityTitleSetterListener;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LikeViewPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Borce on 15.08.2016.
 */
public class LikeFragment extends BaseFragment implements LikeView {

    private LikeViewPresenter presenter;

    @BindString(R.string.likesFragment_title)
    String title;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindString(R.string.likesFragment_spinnerLoadingText)
    String spinnerText;

    @BindView(R.id.error_loading)
    RelativeLayout errorLoadingLayout;

    @BindView(R.id.likeLayout_mainContent)
    RelativeLayout mainContent;

    @BindView(R.id.likeLayout_listView)
    ListView likesListView;

    private LikesAdapter adapter;

    @BindView(R.id.likeLayout_btnAddLike)
    RelativeLayout btnAddLike;

    @BindView(R.id.spinnerLikeAdding)
    ProgressBar spinnerLikeAdding;

    @BindString(R.string.likesFragment_likeAddingFailed_text)
    String likeAddingFailedText;

    @BindView(R.id.likeLayout_btnRemoveLike)
    RelativeLayout btnRemoveLike;

    @BindView(R.id.spinnerLikeRemoving)
    ProgressBar spinnerLikeRemoving;

    @BindString(R.string.likesFragment_likeRemovingFailed_text)
    String likeRemovingFailedText;

    private Unbinder unbinder;

    /**
     * Create a new instance of the fragment.
     *
     * @param lineup instance of the lineup for which the likes will be showed
     * @return new instance of the Fragment
     */
    public static LikeFragment newInstance(Lineup lineup) {
        if (lineup == null) {
            throw new IllegalArgumentException("lineup can't be null");
        }
        LikeFragment fragment = new LikeFragment();
        Bundle args = new Bundle();
        args.putSerializable(LINEUP_KEY, lineup);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Set the presenter for the fragment.
     *
     * @param presenter fragment presenter
     */
    @Inject
    public void setPresenter(LikeViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the fragment is ready to be created.
     *
     * @param savedInstanceState saved state for when the fragment is recreated.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) this.getActivity().getApplication()).getUserComponent()
                .plus(new LikeViewModule(this)).inject(this);
        presenter.loadLikes(this.getArguments());
    }

    /**
     * Celled when the fragment view is ready to be created.
     *
     * @param inflater           system Layout inflater
     * @param container          fragment root container
     * @param savedInstanceState save state for when the fragment is recreated
     * @return fragment view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.like_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.onViewCreated();
        if (this.getActivity() instanceof ActivityTitleSetterListener) {
            ((ActivityTitleSetterListener) this.getActivity()).setTitle(title);
        }
        return view;
    }

    /**
     * Called when the view is ready to be destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Called when the likes has started loading from the server.
     */
    @Override
    public void showLoading() {
        if (this.isVisible()) {
            txtSpinner.setText(spinnerText);
            spinner.setVisibility(View.VISIBLE);
            errorLoadingLayout.setVisibility(View.GONE);
            mainContent.setVisibility(View.GONE);
        }
    }

    /**
     * Called when the likes data is successfully loaded.
     *
     * @param likes List of Lineup likes
     */
    public void showLoadingSuccess(List<UserLike> likes) {
        if (this.isVisible()) {
            mainContent.setVisibility(View.VISIBLE);
            errorLoadingLayout.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            adapter = new LikesAdapter(this.getActivity(), likes);
            likesListView.setAdapter(adapter);
        }
    }

    /**
     * Called when a error occurred while loading the likes data.
     */
    public void showLoadingFailed() {
        if (this.isVisible()) {
            errorLoadingLayout.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            mainContent.setVisibility(View.GONE);
        }
    }

    /**
     * Handle click on the button "Try Again".
     */
    @OnClick(R.id.error_loading_btn_tryAgain)
    void reload() {
        presenter.loadLikes();
    }

    /**
     * Show the addLike like button.
     */
    @Override
    public void showAddLikeButton() {
        if (this.isVisible()) {
            btnAddLike.setVisibility(View.VISIBLE);
            btnRemoveLike.setVisibility(View.GONE);
        }
    }

    /**
     * Show the removeLike like button.
     */
    @Override
    public void showRemoveLikeButton() {
        if (this.isVisible()) {
            btnRemoveLike.setVisibility(View.VISIBLE);
            btnAddLike.setVisibility(View.GONE);
        }
    }

    /**
     * Handle click on the button "Add Like".
     */
    @OnClick(R.id.likeLayout_btnAddLike)
    void onBtnAddLikeClick() {
        presenter.addLike();
    }

    /**
     * Called when a request has been send to addLike a like to the lineup.
     */
    @Override
    public void showLikeAdding() {
        spinnerLikeAdding.setVisibility(View.VISIBLE);
    }

    /**
     * Called when adding the like is successful.
     */
    @Override
    public void showLikeAddingSuccess(UserLike like) {
        if (this.isVisible()) {
            spinnerLikeAdding.setVisibility(View.GONE);
            this.showRemoveLikeButton();
            adapter.addLike(like);
        }
    }

    /**
     * Called when adding the like failed
     */
    @Override
    public void showLikeAddingFailed() {
        if (this.isVisible()) {
            spinnerLikeAdding.setVisibility(View.GONE);
            Toast.makeText(this.getActivity(), likeAddingFailedText, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Handle click on the button "Remove Like".
     */
    @OnClick(R.id.likeLayout_btnRemoveLike)
    void onBtnRemoveLikeClick() {
        presenter.removeLike();
    }

    /**
     * Called when a request has been send to removeLike the lineup like.
     */
    @Override
    public void showLikeRemoving() {
        spinnerLikeRemoving.setVisibility(View.VISIBLE);
    }

    /**
     * Called when removing the like is successful.
     */
    @Override
    public void showLikeRemovingSuccess(UserLike userLike) {
        if (this.isVisible()) {
            spinnerLikeRemoving.setVisibility(View.GONE);
            this.showAddLikeButton();
            adapter.removeLike(userLike);
        }
    }

    /**
     * Called when removing the like failed.
     */
    @Override
    public void showLikeRemovingFailed() {
        if (this.isVisible()) {
            spinnerLikeRemoving.setVisibility(View.GONE);
            Toast.makeText(this.getActivity(), likeRemovingFailedText, Toast.LENGTH_LONG).show();
        }
    }
}
