package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Borce on 27.07.2016.
 */
public class Comment extends BaseModel<Integer> implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("lineup_id")
    private int lineupId;

    @SerializedName("body")
    private String body;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("user")
    private User user;

    @SerializedName("lineup")
    private Lineup lineup;

    public Comment() {
    }

    public Comment(int id, int userId, int lineupId, String body, Date createdAt, Date updatedAt,
                   User user, Lineup lineup) {
        this.id = id;
        this.userId = userId;
        this.lineupId = lineupId;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.lineup = lineup;
    }

    public Comment(int id, User user, Lineup lineup, String body, Date createdAt, Date updatedAt) {
        this(id, user.getId(), lineup.getId(), body, createdAt, updatedAt, user, lineup);
    }

    public Comment(int id, String body, Date createdAt, Date updatedAt, User user, Lineup lineup) {
        this(id, user.getId(), lineup.getId(), body, createdAt, updatedAt, user, lineup);
    }

    public Comment(int id, int userId, int lineupId, String body, Date createdAt, Date updatedAt) {
        this(id, userId, lineupId, body, createdAt, updatedAt, null, null);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Comment && this.id == ((Comment) o).getId();
    }
}
