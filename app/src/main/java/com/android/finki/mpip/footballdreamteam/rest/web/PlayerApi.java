package com.android.finki.mpip.footballdreamteam.rest.web;

import com.android.finki.mpip.footballdreamteam.rest.model.PlayerLineup;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.rest.model.LineupPosition;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Borce on 28.07.2016.
 */
public interface PlayerApi {

    /**
     * Get all players in the web api.
     *
     * @param shortResponse indicates if the response should ne minified
     * @param limit number of records ot be returned
     * @param skip number of records to be skipped
     * @return existing players in the web api
     */
    @GET("players")
    Call<List<Player>> index(@Query("short") Boolean shortResponse, @Query("limit") Integer limit,
                            @Query("skip") Integer skip);

    /**
     * Get a player by his id.
     *
     * @param id player id
     * @return existing player
     */
    @GET("players/{id}")
    Call<Player> get(@Path("id") int id);

    /**
     * Get all lineups in which the player i added.
     *
     * @param id player id
     * @param latest indicates if the lineups should be ordered by the latest
     * @param limit number of records to be returned
     * @param skip number of records to be skipped
     * @return lineups in which the player is added
     */
    @GET("players/{id}/lineups")
    Call<List<PlayerLineup>> lineups(@Path("id") int id, @Query("latest") Boolean latest,
                                     @Query("limit") Integer limit, @Query("skip") Integer skip);

    /**
     * Get all positions the player plays in the lineup.
     *
     * @param id player id
     * @param limit number of records to be returned
     * @param skip  number of records to be skipped
     * @return positions that ht player plays in the lineup
     */
    @GET("players/{id}/lineups/positions")
    Call<List<LineupPosition>> lineupsPositions(@Path("id") int id,
                            @Query("limit") Integer limit, @Query("skip") Integer skip);
}
