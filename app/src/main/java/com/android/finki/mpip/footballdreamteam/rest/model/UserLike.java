package com.android.finki.mpip.footballdreamteam.rest.model;

import android.support.annotation.NonNull;

import com.android.finki.mpip.footballdreamteam.model.BaseModel;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 29.07.2016.
 */
public class UserLike extends BaseModel implements Serializable {

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

    public UserLike(int id) {
        this(id, null, null);
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
        UserLike like = (UserLike)o;
        return this.id == like.getId();
    }

    /**
     * Checks if its same with the given model.
     *
     * @param model BaseModel with which will be checked
     * @return whatever the model are same
     */
    @Override
    public boolean same(BaseModel model) {
        if (!(model instanceof UserLike)) {
            return false;
        }
        UserLike like = (UserLike) model;
        return this.id == like.getId() &&
                super.equalsFields(name, like.getName()) &&
                super.sameModels(this.pivot, like.getPivot());
    }
}
