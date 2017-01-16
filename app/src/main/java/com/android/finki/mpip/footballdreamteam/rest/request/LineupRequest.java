package com.android.finki.mpip.footballdreamteam.rest.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 28.07.2016.
 */
public class LineupRequest implements Serializable {

    @SerializedName("players")
    private List<Content> players;

    public LineupRequest() {
    }

    public LineupRequest(List<Content> players) {
        this.players = players;
    }

    public List<Content> getPlayers() {
        return players;
    }

    public void setPlayers(List<Content> players) {
        this.players = players;
    }

    public static class Content implements Serializable {

        @SerializedName("player_id")
        private int playerId;

        @SerializedName("position_id")
        private int positionId;

        public Content() {
        }

        public Content(int payerId, int positionId) {
            this.playerId = payerId;
            this.positionId = positionId;
        }

        public int getPlayerId() {
            return playerId;
        }

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
        }
    }
}
