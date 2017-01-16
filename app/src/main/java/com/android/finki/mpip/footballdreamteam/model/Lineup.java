package com.android.finki.mpip.footballdreamteam.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Borce on 27.07.2016.
 */
public class Lineup extends IdModel<Integer> implements Serializable, Comparable<Lineup> {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("likes_count")
    private int likesCount;

    @SerializedName("comments_count")
    private int commentsCount;

    @SerializedName("user")
    private User user;

    private List<Player> players;

    private List<Position> positions;

    private List<User> likes;

    private List<Comment> comments;

    public Lineup() {
    }

    public Lineup(int id, int userId, Date createdAt, Date updatedAt, int likesCount,
                  int commentsCount, User user) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.user = user;
    }

    public Lineup(int id, Date createdAt, Date updatedAt, int likesCount,
                  int commentsCount, User user) {
        this.id = id;
        this.userId = user.getId();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.user = user;
    }

    public Lineup(int id, int userId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Lineup(int id, User user, Date createdAt, Date updatedAt) {
        this(id, user.getId(), createdAt, updatedAt, 0, 0, user);
    }

    public Lineup(int id, User user) {
        this(id, null, null, 0, 0, user);
    }

    public Lineup(int id, int userId) {
        this(id, userId, null, null, 0, 0, null);
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(id).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Lineup && this.id == ((Lineup) o).getId();
    }

    /**
     * Checks if its same with the given model.
     *
     * @param model model to be checked
     * @return whatever the model are same
     */
    @Override
    public boolean same(BaseModel model) {
        if (!(model instanceof Lineup)) {
            return false;
        }
        Lineup lineup = (Lineup) model;
        return this.id == lineup.getId() &&
                this.userId == lineup.getUserId() &&
                super.equalsFields(this.createdAt, lineup.getCreatedAt()) &&
                super.equalsFields(this.updatedAt, lineup.getUpdatedAt());
    }

    /**
     * Compare with the given lineup by the update at time.
     *
     * @param lineup lineup with which is compared
     * @return the  result of the comparison
     */
    @Override
    public int compareTo(@NonNull Lineup lineup) {
        if (this.updatedAt == null || lineup.getUpdatedAt() == null) {
            return 0;
        }
        if (this.updatedAt.after(lineup.getUpdatedAt())) {
            return -1;
        }
        if (this.updatedAt.equals(lineup.getUpdatedAt())) {
            return 0;
        }
        return 1;
    }
}
