package com.android.finki.mpip.footballdreamteam.rest.web;

import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.rest.model.LineupLike;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Borce on 27.07.2016.
 */
public interface UsersApi {

    /**
     * Get all users in the web api.
     *
     * @param shortResponse indicates if the response should be minified, by default is minified
     * @param latest indicates if the users should be ordered by the latest
     * @param limit number of users to be returned
     * @param skip number of users to be skipped
     * @return existing users in the web api
     */
    @GET("users")
    Call<List<User>> index(@Query("short") Boolean shortResponse, @Query("latest") Boolean latest,
                        @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Get a user by his id.
     *
     * @param id user id.
     * @return existing user
     */
    @GET("users/{id}")
    Call<User> get(@Path("id") int id);

    /**
     * Get all lineups that the given user has created.
     *
     * @param id user id
     * @param latest indicates if the lineups should be ordered the by latest
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return user lineups
     */
    @GET("users/{id}/lineups")
    Call<List<Lineup>> lineups(@Path("id") int id, @Query("latest") Boolean latest,
                               @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Get all lineups that the given user has liked.
     *
     * @param id user id
     * @param latest indicates if the likes should be ordered by the latest
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return lineups that the user has liked
     */
    @GET("users/{id}/likes")
    Call<List<LineupLike>> likes(@Path("id") int id, @Query("latest") Boolean latest,
                                 @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Get all user comments
     *
     * @param id user id
     * @param latest indicates if the comments should be ordered by the latest
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return user comments
     */
    @GET("users/{id}/comments")
    Call<List<Comment>> comments(@Path("id") int id, @Query("latest") Boolean latest,
                                 @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Get the number of likes and comments for the autheticated user last lineup.
     *
     * @param lastChecked time in mills when the data was last checked
     * @return user last Lineup, if not Lineup is found a empry lineup with id 0 is returned.
     */
    @GET("users/me/statistic")
    Call<Lineup> statistic(@Query("date") Long lastChecked);
}
