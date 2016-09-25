package com.android.finki.mpip.footballdreamteam.ui.dialog;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsViewComponent;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsViewPresenter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialogTest.TestActivity.REMOVE_PLAYER_TOAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;

/**
 * Created by Borce on 20.08.2016.
 */
@Ignore
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class PlayerDetailsDialogTest {

    private final int playerId = 1;
    private final String name = "Player";
    private final String team = "Team";
    private final String age = "30";
    private final String position = "Position";
    @Mock
    private PlayerDetailsViewComponent component;
    @Mock
    private PlayerDetailsViewPresenter presenter;
    private PlayerDetailsDialog dialog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockDependencies();
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setPlayerDetailsDialogComponent(component);
    }

    /**
     * Mock the dependencies for the dialog.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                PlayerDetailsDialog dialog = (PlayerDetailsDialog) invocation.getArguments()[0];
                dialog.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(PlayerDetailsDialog.class));
    }

    /**
     * Test the behavior on newInstance called with invalid player id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOnInvalidPlayerId() {
        PlayerDetailsDialog.newInstance(-1, true);
    }

    /**
     * Test that dialog is successfully created.
     */
    @Test
    public void testDialogIsCreated() {
        dialog = PlayerDetailsDialog.newInstance(playerId, true);
        SupportFragmentTestUtil.startFragment(dialog);
        Bundle args = dialog.getArguments();
        assertNotNull(args);
//        assertEquals(playerId, args.getInt(PlayerDetailsDialog.getBundlePlayerIdKey()));
//        assertEquals(true, args.getBoolean(PlayerDetailsDialog.getBundleEditableKey()));
        assertNotNull(dialog.getView());
    }

    /**
     * Test that when bindPlayer is called, dialog view is correctly bind with the passed data.
     */
    @Test
    public void testBindPlayerWIthEditableTrue() {
        dialog = PlayerDetailsDialog.newInstance(playerId, true);
        SupportFragmentTestUtil.startFragment(dialog);
        dialog.bindPlayer(name, team, age, position, true);
        View view = dialog.getView();
        assertNotNull(view);
        TextView txtName = (TextView) view.findViewById(R.id.playerDetailsLayout_name);
        assertNotNull(txtName);
        assertEquals(name, txtName.getText());
        TextView txtTeam = (TextView) view.findViewById(R.id.playerDetailsLayout_team);
        assertNotNull(txtTeam);
        assertEquals(team, txtTeam.getText());
        TextView txtAge = (TextView) view.findViewById(R.id.playerDetailsLayout_age);
        assertNotNull(txtAge);
        assertEquals(age, txtAge.getText());
        TextView txtPosition = (TextView) view.findViewById(R.id.playerDetailsLayout_position);
        assertNotNull(txtPosition);
        assertEquals(position, txtPosition.getText());
        Button button = (Button) view.findViewById(R.id.playerDetailsLayout_btnRemove);
        assertNotNull(button);
        assertEquals(View.VISIBLE, button.getVisibility());
    }

    /**
     * Test that when bindPlayer is called with editable false, the
     * remove button will not be showed.
     */
    @Test
    public void testBindPlayerWithEditableFalse() {
        dialog = PlayerDetailsDialog.newInstance(playerId, false);
        SupportFragmentTestUtil.startFragment(dialog);
        dialog.bindPlayer(name, team, age, position, false);
        View view = dialog.getView();
        assertNotNull(view);
        Button button = (Button) view.findViewById(R.id.playerDetailsLayout_btnRemove);
        assertNotNull(button);
        assertEquals(View.GONE, button.getVisibility());
    }

    /**
     * Test the behavior when btn remove is clicked.
     */
    @Test
    public void testBtnRemoveClick() {
        dialog = PlayerDetailsDialog.newInstance(playerId, true);
        SupportFragmentTestUtil.startFragment(dialog, TestActivity.class);
        dialog.bindPlayer(name, team, age, position, true);
        assertNotNull(dialog.getView());
        Button btn = (Button) dialog.getView().findViewById(R.id.playerDetailsLayout_btnRemove);
        btn.performClick();
        assertEquals(REMOVE_PLAYER_TOAST, ShadowToast.getTextOfLatestToast());
        assertFalse(dialog.isVisible());
    }

    public static class TestActivity extends AppCompatActivity
            implements PlayerDetailsDialog.Listener {

        static final String REMOVE_PLAYER_TOAST = "Remove Player";

        @Override
        public void removePlayer() {
            Toast.makeText(this, REMOVE_PLAYER_TOAST, Toast.LENGTH_SHORT).show();
        }
    }
}
