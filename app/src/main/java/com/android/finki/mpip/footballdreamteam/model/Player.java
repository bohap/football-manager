package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Borce on 27.07.2016.
 */
public class Player extends BaseModel<Integer> implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("team_id")
    private int teamId;

    @SerializedName("position_id")
    private int positionId;

    @SerializedName("name")
    private String name;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("date_of_birth")
    private Date dateOfBirth;

    @SerializedName("players_count")
    private int playersCount;

    @SerializedName("team")
    private Team team;

    @SerializedName("position")
    private Position position;

    @SerializedName("pivot")
    private LineupPlayer lineupPlayer;

    private List<Lineup> lineups;

    public static final String LINEUP_POSITION_FILED_NAME = "lineupPositionId";

    public Player() {
    }

    public Player(int id, int teamId, int positionId, String name, String nationality,
                  Date dateOfBirth, int playersCount, Team team, Position position,
                  LineupPlayer lineupPlayer) {
        this.id = id;
        this.teamId = teamId;
        this.positionId = positionId;
        this.name = name;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.playersCount = playersCount;
        this.team = team;
        this.position = position;
        this.lineupPlayer = lineupPlayer;
    }

    public Player(int id, Team team, Position position, String name,
                  String nationality, Date dateOfBirth, int playersCount) {
        this(id, team.getId(), position.getId(), name, nationality, dateOfBirth,
                playersCount, team, position, null);
    }

    public Player(int id, int teamId, int positionId, String name,
                  String nationality, Date dateOfBirth) {
        this(id, teamId, positionId, name, nationality, dateOfBirth, 0, null, null, null);
    }

    public Player(int id, String name) {
        this(id, -1, -1, name, null, null);
    }

    public Player(int id, int teamId, int positionId, String name, LineupPlayer lineupPlayer) {
        this(id, teamId, positionId, name, null, null, 0, null, null, lineupPlayer);
    }

    public Player(int id, int teamId, int positionId, LineupPlayer lineupPlayer) {
        this(id, teamId, positionId, null, null, null, 0, null, null, lineupPlayer);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Lineup> getLineups() {
        return lineups;
    }

    public void setLineups(List<Lineup> lineups) {
        this.lineups = lineups;
    }

    public LineupPlayer getLineupPlayer() {
        return lineupPlayer;
    }

    public void setLineupPlayer(LineupPlayer lineupPlayer) {
        this.lineupPlayer = lineupPlayer;
    }

    public int getLineupId() {
        if (lineupPlayer == null) {
            return 0;
        }
        return lineupPlayer.getLineupId();
    }

    public int getLineupPositionId() {
        if (lineupPlayer == null) {
            return 0;
        }
        return lineupPlayer.getPositionId();
    }
}