package com.android.finki.mpip.footballdreamteam.model;

import android.support.annotation.NonNull;

import com.android.finki.mpip.footballdreamteam.utility.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Borce on 27.07.2016.
 */
public class User extends BaseModel<Integer> implements Serializable, Comparable<User> {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("lineups_count")
    private int lineupsCount;

    @SerializedName("likes_count")
    private int likesCount;

    @SerializedName("comments_count")
    private int commentsCount;

    private List<Lineup> lineups;

    private List<Lineup> likes;

    private List<Comment> comments;

    public User() {
    }

    public User(int id, String name, String email, Date createdAt, Date updatedAt,
                int lineupsCount, int likesCount, int commentsCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lineupsCount = lineupsCount;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
    }

    public User(int id, String name, String email, Date createdAt, Date updatedAt) {
        this(id, name, email, createdAt, updatedAt, 0, 0, 0);
    }

    public User(int id, String name) {
        this(id, name, null, null, null);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getLineupsCount() {
        return lineupsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
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

    public void setLineupsCount(int lineupsCount) {
        this.lineupsCount = lineupsCount;
    }

    public List<Lineup> getLineups() {
        return lineups;
    }

    public void setLineups(List<Lineup> lineups) {
        this.lineups = lineups;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Lineup> getLikes() {
        return likes;
    }

    public void setLikes(List<Lineup> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User)o;
        return this.getId().equals(user.getId());
    }

    @Override
    public int compareTo(@NonNull User user) {
        if (this.getId().equals(user.getId())) {
            return 0;
        }
        return 1;
    }
}