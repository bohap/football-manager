package com.android.finki.mpip.footballdreamteam.rest.web;

import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.rest.model.LineupPosition;
import com.android.finki.mpip.footballdreamteam.rest.model.UserLike;
import com.android.finki.mpip.footballdreamteam.rest.request.CommentRequest;
import com.android.finki.mpip.footballdreamteam.rest.request.LineupRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.CommentResponse;
import com.android.finki.mpip.footballdreamteam.rest.response.LineupResponse;
import com.android.finki.mpip.footballdreamteam.rest.response.ServerResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Borce on 28.07.2016.
 */
public interface LineupApi {

    /**
     * Get all lineups in the web api.
     *
     * @param shortResponse indicates if the response should be minified, by default is true
     * @param latest        indicates if the lineup should be ordered by the latest
     * @param limit         number of records to be returned
     * @param skip          number of records to be skipped
     * @return existing lineups in the web api
     */
    @GET("lineups")
    Call<List<Lineup>> index(@Query("short") Boolean shortResponse, @Query("latest") Boolean latest,
                             @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Get a lineup by her id.
     *
     * @param id lineup id
     * @return existing lineup
     */
    @GET("lineups/{id}")
    Call<Lineup> get(@Path("id") int id);

    /**
     * Create a new lineup.
     *
     * @param lineup params for the new lineup
     * @return success response
     */
    @POST("lineups")
    Call<LineupResponse> store(@Body LineupRequest lineup);

    /**
     * Update a existing lineup.
     *
     * @param id     lineup id
     * @param lineup params for the lineup
     * @return success response
     */
    @PUT("lineups/{id}")
    Call<LineupResponse> update(@Path("id") int id, @Body LineupRequest lineup);

    /**
     * Delete a existing lineup.
     *
     * @param id lineup id
     * @return Void indicating that the lineup was deleted successfully
     */
    @DELETE("lineups/{id}")
    Call<Void> delete(@Path("id") int id);

    /**
     * Get all players that are in the given lineup.
     *
     * @param id    lineup id
     * @param limit number of records to be returned
     * @param skip  number of records to be skipped
     * @return lineup players
     */
    @GET("lineups/{id}/players")
    Call<List<Player>> players(@Path("id") int id, @Query("limit") Integer limit,
                               @Query("skip") Integer skip);

    /**
     * Get all positions that are in the given lineup.
     *
     * @param id    player id
     * @param limit number of records to be returned
     * @param skip  number of records to be skipped
     * @return lineup positions
     */
    @GET("lineups/{id}/players/positions")
    Call<List<LineupPosition>> playersPositions(@Path("id") int id, @Query("limit") Integer limit,
                                                @Query("skip") Integer skip);

    /**
     * Get all users that liked the lineup.
     *
     * @param id     lineup id
     * @param latest indicates if the lineup should be ordered by the latest
     * @param limit  number of records to be returned
     * @param skip   number of records to be skipped
     * @return users that liked the lineup
     */
    @GET("lineups/{id}/likes")
    Call<List<UserLike>> likes(@Path("id") int id, @Query("latest") Boolean latest,
                               @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Add a new like to the lineup.
     *
     * @param id lineup id
     * @return success response
     */
    @POST("lineups/{id}/likes")
    @Headers("Connection:close")
    Call<ServerResponse> addLike(@Path("id") int id);

    /**
     * Delete the lineup like from the user.
     *
     * @param id lineup id
     * @return Empty server response
     */
    @DELETE("lineups/{id}/likes")
    @Headers("Connection:close")
    Call<Void> deleteLike(@Path("id") int id);

    /**
     * Get all comments for the lineup.
     *
     * @param id     lineup id
     * @param latest indicates if the comments should be ordered by the latest
     * @param limit  number of records to be returned
     * @param skip   number of records to be skipped
     * @return lineup comments
     */
    @GET("lineups/{id}/comments")
    Call<List<Comment>> comments(@Path("id") int id, @Query("latest") Boolean latest,
                                 @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Create a new comment for the lineup.
     *
     * @param id      lineup id
     * @param comment params for the comment
     * @return success response
     */
    @POST("lineups/{id}/comments")
    Call<CommentResponse> addComment(@Path("id") int id, @Body CommentRequest comment);

    /**
     * Update a existing comment.
     *
     * @param id        lineup id
     * @param commentId comment id
     * @param comment   params for the comment
     * @return success response
     */
    @PUT("lineups/{id}/comments/{commentId}")
    Call<CommentResponse> updateComment(@Path("id") int id, @Path("commentId") int commentId,
                                        @Body CommentRequest comment);

    /**
     * Delete a existing comment.
     *
     * @param id        lineup id
     * @param commentId comment id
     * @return Void indicating that the response was delete successfully
     */
    @DELETE("lineups/{id}/comments/{commentId}")
    Call<Void> deleteComment(@Path("id") int id, @Path("commentId") int commentId);
}
