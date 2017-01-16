package com.android.finki.mpip.footballdreamteam.rest.web;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Borce on 27.07.2016.
 */
public interface PositionApi {

    /**
     * Get all positions in the web api.
     *
     * @param shortResponse indicates if the response should be minified, by default is true
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return existing positions in the web api
     */
    @GET("positions")
    Call<List<Position>> index(@Query("short") Boolean shortResponse,
                               @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Get all lineups that have the given position.
     *
     * @param id position id
     * @param latest order the lineups by the latest
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return lineups that have the given position
     */
    @GET("positions/{id}/lineups")
    Call<List<Lineup>> lineups(@Path("id") int id, @Query("latest") Boolean latest,
                           @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Get all players that plays on the given position.
     *
     * @param id position id
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return position players
     */
    @GET("positions/{id}/players")
    Call<List<Player>> players(@Path("id") int id, @Query("limit") Integer limit,
                           @Query("skip") Integer skip);

    /**
     * Get all players that plays in the lineup on the given position.
     *
     * @param id position id
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return players that plays in the lineup on the given position
     */
    @GET("positions/{id}/lineups/players")
    Call<List<Player>> lineupsPlayers(@Path("id") int id, @Query("limit") Integer limit,
                          @Query("skip") Integer skip);
}
