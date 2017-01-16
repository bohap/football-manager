package com.android.finki.mpip.footballdreamteam.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 25.07.2016.
 */
public class InfoDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private static final Logger logger = LoggerFactory.getLogger(InfoDialog.class);
    public static final String TAG = "INFO_DIALOG";
    public static String TITLE_KEY = "title";
    public static String MESSAGE_KEY = "message";
    private String title;
    private String message;

    /**
     * Get a new instance of the InfoDialog.
     *
     * @param message dialog message
     * @return new instance of the infoDialog on which arguments is set the given message
     */
    public static InfoDialog newInstance(String title, String message) {
        InfoDialog infoDialog = new InfoDialog();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(MESSAGE_KEY, message);;
        infoDialog.setArguments(args);
        return infoDialog;
    }

    /**
     * Called when the fragment is ready to be created.
     *
     * @param savedInstanceState saved state if the fragment is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        this.setStyle(DialogFragment.STYLE_NORMAL, 0);
        this.title = this.getArguments().getString(TITLE_KEY);
        this.message = this.getArguments().getString(MESSAGE_KEY);
    }

    /**
     * Called when the dialog is ready to be shown.
     *
     * @param savedInstanceState saved state if the fragment is recreated
     * @return the dialog that will be shown
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        logger.info("onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(this.title)
               .setMessage(this.message)
               .setNegativeButton("Close", this);
        return builder.create();
    }

    /**
     * Called when some of the dialog buttons are clicked.
     *
     * @param dialogInterface interface representation of the dialog
     * @param which           the button that was clicked
     *                        (AlertDialog. BUTTON_NEGATIVE or BUTTON_POSITIVE)
     */
    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        logger.info(String.format("onClick, which %d",which));
        if (this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).onDialogDone();
        }
    }

    /**
     * Interface that classes that use the dialog need to implement if they want to be notified
     * when the dialog is closed.
     */
    public interface Listener {
        void onDialogDone();
    }
}