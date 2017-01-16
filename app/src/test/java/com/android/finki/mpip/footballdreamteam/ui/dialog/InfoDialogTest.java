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
 * Created by Borce on 25.07.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class InfoDialogTest {

    @Mock
    private static InfoDialog.Listener listener;

    private String title = "Test Title";
    private String message = "Test message";
    private InfoDialog dialog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test that the dialog has been created successfully.
     */
    @Test
    public void testDialogIsCreated() {
        dialog = InfoDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog);
        Dialog alertDialog = dialog.getDialog();
        assertTrue(alertDialog instanceof AlertDialog);
        assertTrue(alertDialog.isShowing());
        Bundle args = dialog.getArguments();
        assertNotNull(args);
        assertEquals(title, args.getString(InfoDialog.TITLE_KEY));
        assertEquals(message, args.getString(InfoDialog.MESSAGE_KEY));
    }

    /**
     * Test that the click on the negative button will invoke the callback on the activity.
     */
    @Test
    public void testBtnCancelClick() {
        dialog = InfoDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog, MockActivity.class);
        AlertDialog alertDialog = (AlertDialog) dialog.getDialog();
        final int negativeButtonId = android.R.id.button2;
        Button btn = (Button) alertDialog.findViewById(negativeButtonId);
        assertNotNull(btn);
        assertTrue(btn.performClick());
        assertFalse(alertDialog.isShowing());
        verify(listener).onDialogDone();
    }

    /**
     * Test the behavior when btn cancel is clicked and dialog activity don't implements
     * InfoDialog.Listener.
     */
    @Test
    public void testBtnCancelClickWhenActivityDoesNotImplementsTheListener() {
        dialog = InfoDialog.newInstance(title, message);
        SupportFragmentTestUtil.startFragment(dialog);
        AlertDialog alertDialog = (AlertDialog) dialog.getDialog();
        final int negativeButtonId = android.R.id.button2;
        Button btn = (Button) alertDialog.findViewById(negativeButtonId);
        assertNotNull(btn);
        assertTrue(btn.performClick());
        assertFalse(alertDialog.isShowing());
        verify(listener, never()).onDialogDone();
    }

    /**
     * Mock activity class for the fragment.
     */
    public static class MockActivity extends AppCompatActivity implements InfoDialog.Listener {

        @Override
        public void onDialogDone() {
            listener.onDialogDone();
        }
    }
}