package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 27.07.2016.
 */
public class Team extends IdModel<Integer> implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("short_name")
    private String shortName;

    @SerializedName("squad_market_value")
    private String squadMarketValue;

    @SerializedName("players_count")
    private int playersCount;

    private List<Player> players;

    public Team() {
    }

    public Team(int id, String name, String shortName,
                String squadMarketValue, int playersCount) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.squadMarketValue = squadMarketValue;
        this.playersCount = playersCount;
    }

    public Team(int id, String name, String shortName, String squadMarketValue) {
        this(id, name, shortName, squadMarketValue, 0);
    }

    public Team(int id, String name) {
        this(id, name, null, null);
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSquadMarketValue() {
        return squadMarketValue;
    }

    public void setSquadMarketValue(String squadMarketValue) {
        this.squadMarketValue = squadMarketValue;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Team && this.id == ((Team) o).getId();
    }

    /**
     * Check if its same with the given model.
     *
     * @param model model to be checked
     * @return whatever the model are same
     */
    @Override
    public boolean same(BaseModel model) {
        if (!(model instanceof Team)) {
            return false;
        }
        Team team = (Team) model;
        return this.id == team.getId() &&
                super.equalsFields(this.name, team.getName()) &&
                super.equalsFields(this.shortName, team.getShortName()) &&
                super.equalsFields(this.squadMarketValue, team.getSquadMarketValue());

    }
}
