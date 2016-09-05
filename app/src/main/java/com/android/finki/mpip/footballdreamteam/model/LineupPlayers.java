package com.android.finki.mpip.footballdreamteam.model;

import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 13.08.2016.
 */
public class LineupPlayers implements Serializable {

    private List<Player> players;
    private List<Position> positions;
    private LineupUtils.FORMATION formation;
    private boolean editable;
    private Map<Integer, Player> mappedPlayers;

    public LineupPlayers() {
    }

    public LineupPlayers(List<Player> players, List<Position> positions,
                         LineupUtils.FORMATION formation, boolean editable) {
        this.players = players;
        this.positions = positions;
        this.formation = formation;
        this.editable = editable;
    }

    public LineupPlayers(List<Player> players, boolean editable) {
        this(players, null, null, editable);
    }

    public LineupPlayers(List<Player> players) {
        this(players, null, null, false);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public LineupUtils.FORMATION getFormation() {
        return formation;
    }

    public void setFormation(LineupUtils.FORMATION formation) {
        this.formation = formation;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Map<Integer, Player> getMappedPlayers() {
        return mappedPlayers;
    }

    public void setMappedPlayers(Map<Integer, Player> mappedPlayers) {
        this.mappedPlayers = mappedPlayers;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
