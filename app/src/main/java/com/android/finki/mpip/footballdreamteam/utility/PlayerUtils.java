package com.android.finki.mpip.footballdreamteam.utility;

import com.android.finki.mpip.footballdreamteam.model.Player;

/**
 * Created by Borce on 12.08.2016.
 */
public class PlayerUtils {

    /**
     * Get the player last name.
     *
     * @param name player name
     * @return player last name
     */
    public String getLastName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name can't be null");
        }
        StringBuilder lastName = new StringBuilder();
        String[] parts = name.split(" ");
        if (parts.length == 1) {
            return name;
        }
        for (int i = 1; i < parts.length; i++) {
            lastName.append(parts[i]);
            if (i < parts.length - 1) {
                lastName.append(" ");
            }
        }
        return lastName.toString();
    }
}
