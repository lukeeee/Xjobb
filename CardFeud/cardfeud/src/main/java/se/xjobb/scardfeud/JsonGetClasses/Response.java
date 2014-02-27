package se.xjobb.scardfeud.JsonGetClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Svempa on 2014-01-30.
 *
 * This represents a json response. GameList -> Response
 *
 */
public class Response {

    /* Used to represent json objects */

    @SerializedName("game_id")
    public String gameId;

    @SerializedName("start_time")
    public String startTime;

    @SerializedName("finished_time")
    public String finishedTime;

    @SerializedName("lastevent")
    public String lastEvent;

    @SerializedName("lastevent_time")
    public String lastEventTime;

    @SerializedName("player_1")
    public String playerOne;

    @SerializedName("player_2")
    public String playerTwo;

    @SerializedName("player_name")
    public String playerName;

    @SerializedName("opponent_id")
    public String opponentId;

    @SerializedName("opponent_name")
    public String opponentName;

    @SerializedName("opponent_country")
    public String opponentCountry;

    @SerializedName("card_color")
    public String cardColor;

    @SerializedName("card_value")
    public String cardValue;

    @SerializedName("pass_prohibited")
    public String passProhibited;

    @SerializedName("last_round_details")
    public String lastRoundDetails;

    @SerializedName("this_round_details")
    public String thisRoundDetails;

    @SerializedName("last_round_points")
    public String lastRoundPoints;

    @SerializedName("this_round_points")
    public String thisRoundPoints;

    @SerializedName("my_turn")
    public String myTurn;

    @SerializedName("opponent_points")
    public String opponentPoints;

    @SerializedName("opponent_errors")
    public String opponentErrors;

    @SerializedName("opponent_wins")
    public String opponentWins;

    @SerializedName("player_points")
    public String playerPoints;

    @SerializedName("player_errors")
    public String playerErrors;

    @SerializedName("player_wins")
    public String playerWins;

    @SerializedName("chat_unread")
    public String chatUnread;

    @SerializedName("odds")
    public String odds;
}
