package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Borce on 27.07.2016.
 */
public class Comment extends IdModel<Integer> implements Serializable {

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

    public Comment(int id) {
        this.id = id;
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
        if (user != null) {
            this.userId = user.getId();
        }
    }

    public Lineup getLineup() {
        return lineup;
    }

    public void setLineup(Lineup lineup) {
        this.lineup = lineup;
        if (lineup != null) {
            this.lineupId = lineup.getId();
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Comment && this.id == ((Comment) o).getId();
    }

    /**
     * Checks if its same with the given model.
     *
     * @param model model to be checked
     * @return whatever the model are same
     */
    @Override
    public boolean same(BaseModel model) {
        if (!(model instanceof Comment)) {
            return false;
        }
        Comment comment = (Comment) model;
        return this.id == comment.getId() &&
                this.userId == comment.getUserId() &&
                this.lineupId == comment.getLineupId() &&
                super.equalsFields(this.body, comment.getBody()) &&
                super.equalsFields(this.createdAt, comment.getCreatedAt()) &&
                super.equalsFields(this.updatedAt, comment.getUpdatedAt());
    }
}
