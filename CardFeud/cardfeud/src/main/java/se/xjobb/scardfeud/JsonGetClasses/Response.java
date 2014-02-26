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
                this.opponentCountry,
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
        String[] data = new String[27];
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
        this.opponentCountry = data[10];
        this.cardColor = data[11];
        this.cardValue = data[12];
        this.passProhibited = data[13];
        this.lastRoundDetails = data[14];
        this.thisRoundDetails = data[15];
        this.lastRoundPoints = data[16];
        this.thisRoundPoints = data[17];
        this.myTurn = data[18];
        this.opponentPoints = data[19];
        this.opponentErrors = data[20];
        this.opponentWins = data[21];
        this.playerPoints = data[22];
        this.playerErrors = data[23];
        this.playerWins = data[24];
        this.chatUnread = data[25];
        this.odds = data[26];
    }

}
