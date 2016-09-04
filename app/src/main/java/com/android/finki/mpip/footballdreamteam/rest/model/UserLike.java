package com.android.finki.mpip.footballdreamteam.rest.model;

import android.support.annotation.NonNull;

import com.android.finki.mpip.footballdreamteam.model.LineupLike;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 29.07.2016.
 */
public class UserLike implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("pivot")
    private LineupLike pivot;

    public UserLike() {
    }

    public UserLike(int id, String name, LineupLike pivot) {
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

    public LineupLike getPivot() {
        return pivot;
    }

    public void setPivot(LineupLike pivot) {
        this.pivot = pivot;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserLike)) {
            return false;
        }
        UserLike userLike = (UserLike)o;
        return this.getId() == userLike.getId();
    }
}
