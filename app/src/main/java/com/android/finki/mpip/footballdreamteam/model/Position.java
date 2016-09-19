package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 27.07.2016.
 */
public class Position extends IdModel<Integer> implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        return o instanceof Position && this.id == ((Position) o).getId();
    }

    /**
     * Checks if its same with the given model.
     *
     * @param model model to be checked
     * @return whatever the models are same
     */
    @Override
    public boolean same(BaseModel model) {
        if (!(model instanceof Position)) {
            return false;
        }
        Position position = (Position) model;
        return this.id == position.getId() &&
                super.equalsFields(this.name, position.getName());
    }
}
