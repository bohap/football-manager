package com.android.finki.mpip.footballdreamteam.rest.response;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 28.07.2016.
 */
public class LineupResponse extends ServerResponse implements Serializable {

    @SerializedName("lineup")
    private Lineup lineup;

    public LineupResponse() {
    }

    public LineupResponse(int code, String status, String message, Lineup lineup) {
        super(code, status, message);
        this.lineup = lineup;
    }

    public LineupResponse(Lineup lineup) {
        this.lineup = lineup;
    }

    public Lineup getLineup() {
        return lineup;
    }

    public void setLineup(Lineup lineup) {
        this.lineup = lineup;
    }
}
