package com.android.finki.mpip.footballdreamteam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Borce on 27.07.2016.
 */
public class Player extends IdModel<Integer> implements Serializable {

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

    public Player(int id, Team team, Position position, String name,
                  String nationality, Date dateOfBirth) {
        this(id, team.getId(), position.getId(), name, nationality, dateOfBirth,
                0, team, position, null);
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
        if (teamId < 1 && team != null) {
            return team.getId();
        }
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPositionId() {
        if (positionId < 1 && position != null) {
            return position.getId();
        }
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
        if (team != null) {
            this.teamId = team.getId();
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        if (position != null) {
            this.positionId = position.getId();
        }
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
        if (lineupPlayer != null) {
            return lineupPlayer.getLineupId();
        }
        return 0;
    }

    public int getLineupPositionId() {
        if (lineupPlayer == null) {
            return 0;
        }
        return lineupPlayer.getPositionId();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Player && this.id == ((Player) o).getId();
    }

    /**
     * Checks if its same with the given model.
     *
     * @param model model to be checked
     * @return whatever the models are same
     */
    @Override
    public boolean same(BaseModel model) {
        if (!(model instanceof Player)) {
            return false;
        }
        Player player = (Player) model;
        return this.id == player.getId() &&
                this.teamId == player.getTeamId() &&
                this.positionId == player.getPositionId() &&
                super.equalsFields(this.name, player.getName()) &&
                super.equalsFields(this.nationality, player.getNationality()) &&
                super.equalsFields(this.dateOfBirth, player.getDateOfBirth());
    }
}