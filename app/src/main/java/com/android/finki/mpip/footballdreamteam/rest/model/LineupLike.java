package com.android.finki.mpip.footballdreamteam.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Borce on 28.07.2016.
 */
public class LineupLike {

    /**
     * The id of the lineup
     */
    @SerializedName("id")
    private int id;

    @SerializedName("pivot")
    private com.android.finki.mpip.footballdreamteam.model.LineupLike pivot;

    public LineupLike() {
    }

    public LineupLike(int id, com.android.finki.mpip.footballdreamteam.model.LineupLike pivot) {
        this.id = id;
        this.pivot = pivot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public com.android.finki.mpip.footballdreamteam.model.LineupLike getPivot() {
        return pivot;
    }

    public void setPivot(com.android.finki.mpip.footballdreamteam.model.LineupLike pivot) {
        this.pivot = pivot;
    }
}
