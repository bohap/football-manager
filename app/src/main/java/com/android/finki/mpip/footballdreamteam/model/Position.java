package com.android.finki.mpip.footballdreamteam.model;

import com.android.finki.mpip.footballdreamteam.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 27.07.2016.
 */
public class Position extends BaseModel<Integer> implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("players_count")
    private int playersCount;

    private List<Player> players;

    private List<Position> positions;

    public Position() {
    }

    public Position(int id, String name, int playersCount) {
        this.id = id;
        this.name = name;
        this.playersCount = playersCount;
    }

    public Position(int id, String name) {
        this(id, name, 0);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }
}
