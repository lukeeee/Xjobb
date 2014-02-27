package se.xjobb.scardfeud.JsonGetClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Svempa on 2014-02-27.
 */
public class ResponseParcelable implements Parcelable {
/* Everything below is used to pass objects of this type along with intents */

    private Response response;

    public Response getResponse(){
        return response;
    }

    public ResponseParcelable(Response response){
        super();
        this.response = response;
    }

    private ResponseParcelable(Parcel in){
        response = new Response();
        response.gameId = in.readString();
        response.startTime = in.readString();
        response.finishedTime = in.readString();
        response.lastEvent = in.readString();
        response.lastEventTime = in.readString();
        response.playerOne = in.readString();
        response.playerTwo = in.readString();
        response.playerName = in.readString();
        response.opponentId = in.readString();
        response.opponentName = in.readString();
        response.opponentCountry = in.readString();
        response.cardColor = in.readString();
        response.cardValue = in.readString();
        response.passProhibited = in.readString();
        response.lastRoundDetails = in.readString();
        response.thisRoundDetails = in.readString();
        response.lastRoundPoints = in.readString();
        response.thisRoundPoints = in.readString();
        response.myTurn = in.readString();
        response.opponentPoints = in.readString();
        response.opponentErrors = in.readString();
        response.opponentWins = in.readString();
        response.playerPoints = in.readString();
        response.playerErrors = in.readString();
        response.playerWins = in.readString();
        response.chatUnread = in.readString();
        response.odds = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(response.gameId);
        dest.writeString(response.startTime);
        dest.writeString(response.finishedTime);
        dest.writeString(response.lastEvent);
        dest.writeString(response.lastEventTime);
        dest.writeString(response.playerOne);
        dest.writeString(response.playerTwo);
        dest.writeString(response.playerName);
        dest.writeString(response.opponentId);
        dest.writeString(response.opponentName);
        dest.writeString(response.opponentCountry);
        dest.writeString(response.cardColor);
        dest.writeString(response.cardValue);
        dest.writeString(response.passProhibited);
        dest.writeString(response.lastRoundDetails);
        dest.writeString(response.thisRoundDetails);
        dest.writeString(response.lastRoundPoints);
        dest.writeString(response.thisRoundPoints);
        dest.writeString(response.myTurn);
        dest.writeString(response.opponentPoints);
        dest.writeString(response.opponentErrors);
        dest.writeString(response.opponentWins);
        dest.writeString(response.playerPoints);
        dest.writeString(response.playerErrors);
        dest.writeString(response.playerWins);
        dest.writeString(response.chatUnread);
        dest.writeString(response.odds);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ResponseParcelable> CREATOR = new Parcelable.Creator<ResponseParcelable>() {
        public ResponseParcelable createFromParcel(Parcel in) {
            return new ResponseParcelable(in);
        }

        public ResponseParcelable[] newArray(int size) {
            return new ResponseParcelable[size];
        }
    };

}
