package com.android.finki.mpip.footballdreamteam.rest.web;

import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Borce on 27.07.2016.
 */
public interface TeamApi {

    /**
     * Get all team in the web api.
     *
     * @param shortResponse indicates if the response should be minified, by default is true
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return existing teams in the web api
     */
    @GET("teams")
    Call<List<Team>> index(@Query("short") Boolean shortResponse, @Query("limit") Integer limit,
                       @Query("skip") Integer skip);

    /**
     * Get a team by his id.
     *
     * @param id team id
     * @return existing team
     */
    @GET("teams/{id}")
    Call<Team> get(@Path("id") int id);

    /**
     * Get all players that plays for the given team.
     *
     * @param id team id
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return team players
     */
    @GET("teams/{id}/players")
    Call<List<Player>> players(@Path("id") int id, @Query("limit") Integer limit,
                       @Query("skip") Integer skip);
}