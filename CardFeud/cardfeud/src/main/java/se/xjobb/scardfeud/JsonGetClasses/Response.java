package se.xjobb.scardfeud.JsonGetClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Svempa on 2014-01-30.
 *
 * This represents a json response. GameList -> Response
 *
 */
public class Response implements Parcelable {

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

    /* Everything below is used to pass objects of this type along with intents */

    @Override
    public int describeContents() {
        return 0;
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Response> CREATOR = new Parcelable.Creator<Response>() {
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeStrin("");
        dest.writeStringArray(new String[]{
                this.gameId,
                this.startTime,
                this.finishedTime,
                this.lastEvent,
                this.lastEventTime,
                this.playerOne,
                this.playerTwo,
                this.playerName,
                this.opponentId,
                this.opponentName,
                this.cardColor,
                this.cardValue,
                this.passProhibited,
                this.lastRoundDetails,
                this.thisRoundDetails,
                this.lastRoundPoints,
                this.thisRoundPoints,
                this.myTurn,
                this.opponentPoints,
                this.opponentErrors,
                this.opponentWins,
                this.playerPoints,
                this.playerErrors,
                this.playerWins,
                this.chatUnread,
                this.odds});
    }

    public Response (Parcel in) {
        // gameId = in.readString();
        String[] data = new String[26];
        in.readStringArray(data);
        this.gameId = data[0];
        this.startTime = data[1];
        this.finishedTime = data[2];
        this.lastEvent = data[3];
        this.lastEventTime = data[4];
        this.playerOne = data[5];
        this.playerTwo = data[6];
        this.playerName = data[7];
        this.opponentId = data[8];
        this.opponentName = data[9];
        this.cardColor = data[10];
        this.cardValue = data[11];
        this.passProhibited = data[12];
        this.lastRoundDetails = data[13];
        this.thisRoundDetails = data[14];
        this.lastRoundPoints = data[15];
        this.thisRoundPoints = data[16];
        this.myTurn = data[17];
        this.opponentPoints = data[18];
        this.opponentErrors = data[19];
        this.opponentWins = data[20];
        this.playerPoints = data[21];
        this.playerErrors = data[22];
        this.playerWins = data[23];
        this.chatUnread = data[24];
        this.odds = data[25];
    }

}
