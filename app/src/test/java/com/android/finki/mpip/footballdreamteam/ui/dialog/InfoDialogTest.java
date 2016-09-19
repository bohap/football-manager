package com.android.finki.mpip.footballdreamteam.ui.dialog;

import android.app.Dialog;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.BuildConfig;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.android.finki.mpip.footballdreamteam.ui.dialog.InfoDialogTest.TestActivity.TOAST_TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 25.07.2016.
 */
@Ignore
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class InfoDialogTest {

    private String title = "Simple Title";
    private String message = "Simple message";
    private InfoDialog dialog;

    /**
     * Test that the dialog has been created successfully.
     */
    @Test
    public void testDialogShowed() {
        dialog = InfoDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog);
        Dialog alertDialog = dialog.getDialog();
        assertTrue(alertDialog instanceof AlertDialog);
        assertTrue(alertDialog.isShowing());
    }

    /**
     * Test that the click on the negative button will invoke hte callback on the activity.
     */
    @Test
    public void testBtnCancelClickCallCallbackOnActivity() {
        dialog = InfoDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog, TestActivity.class);
        AlertDialog alertDialog = (AlertDialog) dialog.getDialog();
        final int negativeButtonId = android.R.id.button2;
        View dialogBtn = alertDialog.findViewById(negativeButtonId);
        assertNotNull(dialogBtn);
        dialogBtn.performClick();
        assertFalse(alertDialog.isShowing());
        assertEquals(TOAST_TEXT, ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test the behavior when btn cancel is clicked and dialog activity
     * don't implement listener.
     */
    @Test
    public void testBtnCancelClickWhenActivityNotImplementTheListener() {
        dialog = InfoDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog);
        AlertDialog alertDialog = (AlertDialog) dialog.getDialog();
        final int negativeButtonId = android.R.id.button2;
        View dialogBtn = alertDialog.findViewById(negativeButtonId);
        assertNotNull(dialogBtn);
        dialogBtn.performClick();
        assertFalse(alertDialog.isShowing());
        assertNull(ShadowToast.getTextOfLatestToast());
    }

    /**
     * Test activity so that we can verify methods that calls the activity methods.
     */
    public static class TestActivity extends AppCompatActivity implements InfoDialog.Listener {

        static final String TOAST_TEXT = "TEST_INFO_DIALOG";

        @Override
        public void onDialogDone() {
            Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_SHORT).show();
        }
    }
}