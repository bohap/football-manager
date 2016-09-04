package com.android.finki.mpip.footballdreamteam.rest.model;

import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 29.07.2016.
 */
public class LineupPosition implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("pivot")
    private LineupPlayer pivot;

    public LineupPosition() {
    }

    public LineupPosition(int id, String name, LineupPlayer pivot) {
        this.id = id;
        this.name = name;
        this.pivot = pivot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LineupPlayer getPivot() {
        return pivot;
    }

    public void setPivot(LineupPlayer pivot) {
        this.pivot = pivot;
    }
}
