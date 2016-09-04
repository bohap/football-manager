package com.android.finki.mpip.footballdreamteam.model;

import com.android.finki.mpip.footballdreamteam.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 27.07.2016.
 */
public class Position extends BaseModel<Integer> implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("players_count")
    private int playersCount;

    private List<Player> players;

    private List<Position> positions;

    public enum POSITION {
        KEEPER("keeper"),
        RIGHT_BACK("right back"),
        LEFT_BACK("left back"),
        CENTRE_BACK("centre back"),
        DEFENSIVE_MIDFIELD("defensive midfield"),
        CENTRE_MIDFIELD("centre midfield"),
        ATTACKING_MIDFIELD("attacking midfield"),
        RIGHT_WING("right wing"),
        LEFT_WING("left wing"),
        CENTRE_FORWARD("centre forward"),
        SECONDARY_FORWARD("secondary forward");

        private final String name;

        POSITION(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum POSITION_PLACE {
        KEEPERS,
        DEFENDERS,
        MIDFIELDERS,
        ATTACKERS
    }

    public static final int[] resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.centreCentreBack, R.id.leftBack, R.id.rightBack,
            R.id.leftCentreMidfield, R.id.rightCentreMidfield, R.id.centreCentreMidfield,
            R.id.attackingMidfield, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
            R.id.rightCentreForward, R.id.centreCentreForward};

    public Position() {
    }

    public Position(int id, String name, int playersCount) {
        this.id = id;
        this.name = name;
        this.playersCount = playersCount;
    }

    public Position(int id, String name) {
        this(id, name, 0);
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

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }
}
