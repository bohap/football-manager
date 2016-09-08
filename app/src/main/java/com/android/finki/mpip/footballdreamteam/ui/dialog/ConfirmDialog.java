package com.android.finki.mpip.footballdreamteam.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Borce on 21.08.2016.
 */
public class ConfirmDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String TAG = "CONFIRM_DIALOG";
    private static final String TITLE_KEY = "title";
    private static final String MESSAGE_KEY = "message";
    private String title;
    private String message;

    /**
     * Create a new instance of the dialog.
     *
     * @param title   dialog title
     * @param message dialog message
     * @return new instance of the dialog
     */
    public static ConfirmDialog newInstance(String title, String message) {
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(MESSAGE_KEY, message);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * Called when the dialog is ready to be created.
     *
     * @param savedInstanceState saved state from when the dialog is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = this.getArguments().getString(TITLE_KEY, "");
        this.message = this.getArguments().getString(MESSAGE_KEY, "");
    }

    /**
     * Called when the dialog view is ready to be created.
     *
     * @param savedInstanceState save state from when the dialog is recreated
     * @return dialog view
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", this)
                .setNegativeButton("Cancel", this);
        return builder.create();
    }

    /**
     * Called when the dialog buttons are clicked.
     *
     * @param dialogInterface interface representing the dialog
     * @param which           the button that was clicked (BUTTON_NEGATIVE or BUTTON_POSITIVE)
     */
    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE && this.getActivity() instanceof Listener) {
            ((Listener) this.getActivity()).onDialogConfirm();
        }
    }

    /**
     * Interface that classes that use the dialog need to implement if they want to know when the
     * dialog is closed.
     */
    public interface Listener {

        void onDialogConfirm();
    }
}
