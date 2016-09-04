package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.finki.mpip.footballdreamteam.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 15.08.2016.
 */
public class CommentFragment extends Fragment {

    private static Logger logger = LoggerFactory.getLogger(CommentFragment.class);
    private static final String LINEUP_ID_KEY = "lineup_id";
    private int lineupId;

    /**
     * Create a new instance of the fragment.
     *
     * @param lineupId id of the lineup
     * @return new instance of the fragment
     */
    public static CommentFragment newInstance(int lineupId) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(LINEUP_ID_KEY, lineupId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is ready to be created.
     *
     * @param savedInstanceState saved state for when the fragment is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.lineupId = this.getArguments().getInt(LINEUP_ID_KEY, -1);
        if (this.lineupId < 1) {
            String message = "lineup id is required for this fragment";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        //CALL THE PRESENTER
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_layout, container, false);
        //set the view logic
        return view;
    }
}
