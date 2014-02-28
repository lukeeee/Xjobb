package se.xjobb.scardfeud;

/**
 * Created by Lukas on 2014-02-27.
 */
public class Friends{
    private String _country;
    private String _friend;
    private String _userId;

    // Empty constructor
    public Friends() {

    }

    // constructor
    public Friends(String _userId, String country, String _friend) {
        this._userId = _userId;
        this._country = country;
        this._friend = _friend;
    }

    // getting country
    public String getCountry() {
        return this._country;
    }

    // setting country
    public void setCountry(String country) {
        this._country = country;
    }

    // getting friend
    public String getFriend() {
        return this._friend;
    }

    // setting friend
    public void setFriend(String friend) {
        this._friend = friend;
    }

    // getting ID
    public String getUserID() {
        return this._userId;
    }

    // setting _id
    public void setUserID(String userID) {
        this._userId = userID;
    }

    @Override
    public String toString() {
        return _country + ", " + _friend;
    }
}
