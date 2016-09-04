package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Borce on 29.07.2016.
 */
public class LineupLike implements Serializable {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("lineup_id")
    private int lineupId;

    @SerializedName("created_at")
    private Date createdAt;

    private User user;

    private Lineup lineup;

    public LineupLike() {
    }

    public LineupLike(int userId, int lineupId, Date createdAt, User user, Lineup lineup) {
        this.userId = userId;
        this.lineupId = lineupId;
        this.createdAt = createdAt;
        this.user = user;
        this.lineup = lineup;
    }

    public LineupLike(int userId, int lineupId, Date createdAt) {
        this(userId, lineupId, createdAt, null, null);
    }

    public LineupLike(User user, Lineup lineup, Date createdAt) {
        this(user.getId(), lineup.getId(), createdAt, user, lineup);
    }

    public LineupLike(int userId, int lineupId) {
        this(userId, lineupId, null, null, null);
    }

    public LineupLike(User user, Lineup lineup) {
        this(user, lineup, null);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLineupId() {
        return lineupId;
    }

    public void setLineupId(int lineupId) {
        this.lineupId = lineupId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lineup getLineup() {
        return lineup;
    }

    public void setLineup(Lineup lineup) {
        this.lineup = lineup;
    }
}
