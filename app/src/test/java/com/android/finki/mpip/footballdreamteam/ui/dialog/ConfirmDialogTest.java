package com.android.finki.mpip.footballdreamteam.ui.dialog;

import android.app.Dialog;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.android.finki.mpip.footballdreamteam.ui.dialog.ConfirmDialogTest.TestActivity.ON_DIALOG_CONFIRM_TOAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 21.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ConfirmDialogTest {

    private String title = "Confirm Dialog";
    private String message = "Confirm Dialog Message";
    private ConfirmDialog dialog;

    /**
     * Test that dialog is successfully created.
     */
    @Test
    public void testDialogIsCreated() {
        dialog = ConfirmDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog);
        Dialog alertDialog = dialog.getDialog();
        assertTrue(alertDialog instanceof AlertDialog);
        assertTrue(alertDialog.isShowing());
    }

    /**
     * Test the behavior when btn cancel is clicked.
     */
    @Test
    public void testBtnCancelClick() {
        dialog = ConfirmDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog, TestActivity.class);
        Dialog alertDialog = dialog.getDialog();
        Button btn = (Button) alertDialog.findViewById(android.R.id.button2);
        assertNotNull(btn);
        btn.performClick();
        assertFalse(alertDialog.isShowing());
        assertNull(ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test the behavior when btn confirm is clicked and the activity implements the listener.
     */
    @Test
    public void testBtnConfirmClick() {
        dialog = ConfirmDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog, TestActivity.class);
        Dialog alertDialog = dialog.getDialog();
        Button btn = (Button) alertDialog.findViewById(android.R.id.button1);
        assertNotNull(btn);
        btn.performClick();
        assertFalse(alertDialog.isShowing());
        assertEquals(ON_DIALOG_CONFIRM_TOAST, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test the behavior when btn confirm is clicked and the activity
     * don't implement the listener.
     */
    @Test
    public void testBtnConfirmClickWhenActivityNotImplementsTheListener() {
        dialog = ConfirmDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog);
        Dialog alertDialog = dialog.getDialog();
        Button btn = (Button) alertDialog.findViewById(android.R.id.button1);
        assertNotNull(btn);
        btn.performClick();
        assertFalse(alertDialog.isShowing());
        assertNull(ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test activity so the methods from the dialog that calls the listener can be verified.
     */
    public static class TestActivity extends AppCompatActivity implements ConfirmDialog.Listener {

        static final String ON_DIALOG_CONFIRM_TOAST = "On Dialog Confirm";

        @Override
        public void onDialogConfirmed() {
            Toast.makeText(this, ON_DIALOG_CONFIRM_TOAST, Toast.LENGTH_SHORT).show();
        }
    }
}
