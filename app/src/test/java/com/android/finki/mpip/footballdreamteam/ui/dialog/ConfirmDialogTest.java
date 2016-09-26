package com.android.finki.mpip.footballdreamteam.ui.dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.android.finki.mpip.footballdreamteam.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by Borce on 21.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ConfirmDialogTest {

    @Mock
    private static ConfirmDialog.Listener listener;

    private String title = "Confirm Dialog";
    private String message = "Confirm Dialog Message";
    private ConfirmDialog dialog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

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
        Bundle args = dialog.getArguments();
        assertNotNull(args);
        assertEquals(title, args.getString(ConfirmDialog.TITLE_KEY));
        assertEquals(message, args.getString(ConfirmDialog.MESSAGE_KEY));
    }

    /**
     * Test the behavior when btn cancel is clicked.
     */
    @Test
    public void testBtnCancelClick() {
        dialog = ConfirmDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog, MockActivity.class);
        Dialog alertDialog = dialog.getDialog();
        Button btn = (Button) alertDialog.findViewById(android.R.id.button2);
        assertNotNull(btn);
        assertTrue(btn.performClick());
        assertFalse(alertDialog.isShowing());
        verify(listener, never()).onDialogConfirmed();
    }

    /**
     * Test the behavior when btn confirm is clicked and the activity implements the listener.
     */
    @Test
    public void testBtnConfirmClick() {
        dialog = ConfirmDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog, MockActivity.class);
        Dialog alertDialog = dialog.getDialog();
        Button btn = (Button) alertDialog.findViewById(android.R.id.button1);
        assertNotNull(btn);
        assertTrue(btn.performClick());
        assertFalse(alertDialog.isShowing());
        verify(listener).onDialogConfirmed();
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
        assertTrue(btn.performClick());
        assertFalse(alertDialog.isShowing());
        verify(listener, never()).onDialogConfirmed();
    }

    /**
     * Mock activity class for the dialog.
     */
    public static class MockActivity extends AppCompatActivity implements ConfirmDialog.Listener {

        @Override
        public void onDialogConfirmed() {
            listener.onDialogConfirmed();
        }
    }
}
