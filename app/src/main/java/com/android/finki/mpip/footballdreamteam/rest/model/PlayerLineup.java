package com.android.finki.mpip.footballdreamteam.rest.model;

import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 29.07.2016.
 */
public class PlayerLineup implements Serializable {

    /**
     * The id of the lineup
     */
    @SerializedName("id")
    private int id;

    @SerializedName("pivot")
    private LineupPlayer pivot;

    @SerializedName("user")
    private User user;

    public PlayerLineup() {
    }

    public PlayerLineup(int id, LineupPlayer pivot, User user) {
        this.id = id;
        this.pivot = pivot;
        this.user = user;
    }

    public PlayerLineup(int id, LineupPlayer pivot) {
        this(id, pivot, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LineupPlayer getPivot() {
        return pivot;
    }

    public void setPivot(LineupPlayer pivot) {
        this.pivot = pivot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}