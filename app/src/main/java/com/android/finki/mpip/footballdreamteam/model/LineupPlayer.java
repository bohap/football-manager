package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Borce on 29.07.2016.
 */
public class LineupPlayer extends BaseModel implements Serializable {

    @SerializedName("lineup_id")
    private int lineupId;

    @SerializedName("player_id")
    private int playerId;

    @SerializedName("position_id")
    private int positionId;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    private Lineup lineup;

    private Player player;

    private Position position;

    public LineupPlayer() {
    }

    public LineupPlayer(int lineupId, int playerId, int positionId, Date createdAt,
                        Date updatedAt, Lineup lineup, Player player, Position position) {
        this.lineupId = lineupId;
        this.playerId = playerId;
        this.positionId = positionId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lineup = lineup;
        this.player = player;
        this.position = position;
    }

    public LineupPlayer(Lineup lineup, Player player, Position position, Date createdAt,
                        Date updatedAt) {
        this(lineup.getId(), player.getId(), position.getId(), createdAt, updatedAt,
                lineup, player, position);
    }

    public LineupPlayer(Lineup lineup, Player player, Position position) {
        this(lineup, player, position, null, null);
    }

    public LineupPlayer(int lineupId, int playerId, int positionId) {
        this(lineupId, playerId, positionId, null, null, null, null, null);
    }

    public int getLineupId() {
        if (lineupId < 1 && lineup != null) {
            return lineup.getId();
        }
        return lineupId;
    }

    public void setLineupId(int lineupId) {
        this.lineupId = lineupId;
    }

    public int getPlayerId() {
        if (playerId < 1 && player != null) {
            return player.getId();
        }
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPositionId() {
        if (positionId < 1 && position != null) {
            return position.getId();
        }
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLineup(Lineup lineup) {
        this.lineup = lineup;
        if (lineup != null) {
            this.lineupId = lineup.getId();
        }
    }

    public Lineup getLineup() {
        return lineup;
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (player != null) {
            this.playerId = player.getId();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPosition(Position position) {
        this.position = position;
        if (position != null) {
            this.positionId = position.getId();
        }
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LineupPlayer)) {
            return false;
        }
        LineupPlayer lineupPlayer = (LineupPlayer) o;
        return this.lineupId == lineupPlayer.getLineupId() &&
                this.playerId == lineupPlayer.getPlayerId() &&
                this.positionId == lineupPlayer.getPositionId();
    }

    /**
     * Checks if its same with the given model.
     *
     * @param model model to be checked
     * @return whatever the models are same
     */
    @Override
    public boolean same(BaseModel model) {
        if (!(model instanceof LineupPlayer)) {
            return false;
        }
        LineupPlayer lineupPlayer = (LineupPlayer) model;
        return this.lineupId == lineupPlayer.getLineupId() &&
                this.playerId == lineupPlayer.getPlayerId() &&
                this.positionId == lineupPlayer.getPositionId() &&
                super.equalsFields(this.createdAt, lineupPlayer.getCreatedAt()) &&
                super.equalsFields(this.updatedAt, lineupPlayer.getUpdatedAt());
    }
}
