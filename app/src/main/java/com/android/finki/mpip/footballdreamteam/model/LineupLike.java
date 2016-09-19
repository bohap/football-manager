package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Borce on 29.07.2016.
 */
public class LineupLike extends BaseModel implements Serializable {

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
        if (userId < 1 && user != null) {
            return user.getId();
        }
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        this.userId = user.getId();
    }

    public Lineup getLineup() {
        return lineup;
    }

    public void setLineup(Lineup lineup) {
        this.lineup = lineup;
        this.lineupId = lineup.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LineupLike)) {
            return false;
        }
        LineupLike like = (LineupLike) o;
        return this.userId == like.getUserId() && this.lineupId == like.getLineupId();
    }

    /**
     * Checks if ita same with the given model.
     *
     * @param model model to be checked
     * @return whatever the model are same
     */
    @Override
    public boolean same(BaseModel model) {
        if (!(model instanceof LineupLike)) {
            return false;
        }
        LineupLike like = (LineupLike) model;
        return this.userId == like.getUserId() &&
                this.lineupId == like.getLineupId() &&
                super.equalsFields(this.createdAt, like.getCreatedAt());
    }
}
