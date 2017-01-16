package com.android.finki.mpip.footballdreamteam.rest.response;

import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 29.07.2016.
 */
public class CommentResponse extends ServerResponse implements Serializable {

    @SerializedName("comment")
    private Comment comment;

    public CommentResponse() {
    }

    public CommentResponse(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
