package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.UserRepository;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.UniqueFieldConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.UserException;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 01.08.2016.
 */
public class UserDBServiceUnitTest {

    @Mock
    private UserRepository repository;

    private UserDBService service;

    private final int userId = 1;
    private User user = new User(userId, "User", "user@email.com", "pass", new Date(), new Date());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new UserDBService(repository);
    }

    /**
     * Test that exists method returns true on existing id.
     */
    @Test
    public void testExists() {
        when(repository.get(userId)).thenReturn(user);
        assertTrue(service.exists(userId));
    }

    /**
     * Test that exists method will return false when the user id don't exists.
     */
    @Test
    public void testNotExists() {
        when(repository.get(userId)).thenReturn(null);
        assertFalse(service.exists(userId));
    }

    /**
     * Test the behavior on exists method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsOnNull() {
        service.exists(null);
    }

    /**
     * Test the behavior on store method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNull() {
        service.store(null);
    }

    /**
     * Test the behavior on store method called with invalid id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnInvalidId() {
        service.update(new User(0, "", "", null, null, null));
    }

    /**
     * Test the behavior on store method called with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNullName() {
        service.store(new User(1, null, "", null, null, null));
    }

    /**
     * Test the behavior on store method called with null email.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNullEmail() {
        service.store(new User(1, "", null, null, null, null));
    }

    /**
     * Test the behavior on store called with existing id.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnExistingId() {
        when(repository.get(userId)).thenReturn(user);
        service.store(user);
    }

    /**
     * Test the behavior on store method called with existing email.
     */
    @Test(expected = UniqueFieldConstraintException.class)
    public void testStoreOnExistingEmail() {
        String email = user.getEmail();
        when(repository.get(userId)).thenReturn(null);
        when(repository.getByEmail(email)).thenReturn(user);
        service.store(user);
    }

    /**
     * Test the behavior on store method when inserting the record on the database failed.
     */
    @Test(expected = UserException.class)
    public void testFailedStore() {
        when(repository.get(userId)).thenReturn(null);
        when(repository.store(user)).thenReturn(false);
        service.store(user);
    }

    /**
     * Test the behavior on store method when inserting the record on the database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(repository.get(userId)).thenReturn(null);
        when(repository.store(user)).thenReturn(true);
        User stored = service.store(user);
        assertNotNull(stored);
        assertEquals(userId, stored.getId().intValue());
        assertEquals(user.getEmail(), stored.getEmail());
    }

    /**
     * Test the behavior on onUpdateSuccess method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNull() {
        service.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnInvalidId() {
        service.update(new User(0, "", "", null, null, null));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNullName() {
        service.update(new User(1, null, "", null, null, null));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with null email.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNullEmail() {
        service.update(new User(0, "", null, null, null, null));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnExistingId() {
        when(repository.get(userId)).thenReturn(null);
        service.update(user);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record on the database failed.
     */
    @Test(expected = UserException.class)
    public void testFailedUpdate() {
        when(repository.get(userId)).thenReturn(user);
        when(repository.update(user)).thenReturn(false);
        service.update(user);
    }

    /**
     * Test the behavior on store method when inserting the record on the database is successful.
     */
    @Test
    public void testSuccessUpdate() {
        when(repository.get(userId)).thenReturn(user);
        when(repository.update(user)).thenReturn(true);
        User updated = service.update(this.user);
        assertNotNull(updated);
        assertEquals(userId, updated.getId().intValue());
    }

    /**
     * Test the behavior on delete method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnUnExistingId() {
        when(repository.get(userId)).thenReturn(null);
        service.delete(userId);
    }

    /**
     * Test the behavior on delete method when deleting the record on the database failed.
     */
    @Test(expected = UserException.class)
    public void testFailedDelete() {
        when(repository.get(userId)).thenReturn(user);
        when(repository.delete(userId)).thenReturn(false);
        service.delete(userId);
    }

    /**
     * Test the behavior on delete method when deleting the record on the database is successful.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(userId)).thenReturn(user);
        when(repository.delete(userId)).thenReturn(true);
        service.delete(userId);
    }

    /**
     * Test the behavior on delete method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnNull() {
        service.delete(null);
    }
}
