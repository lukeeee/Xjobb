package se.xjobb.scardfeud;

/**
 * Created by Lukas on 2014-02-27.
 */
public class Friends{
    private int _id;
    private String _country;
    private String _friend;
    private int _userId;

    // Empty constructor
    public Friends() {

    }

    // constructor
    public Friends(int id, String country, String friend, int user_id) {
        _id = id;
        _country = country;
        _friend = friend;
        _userId = user_id;
    }

    // constructor
    public Friends(String country, String _friend, int _userId) {
        this._country = country;
        this._friend = _friend;
        this._userId = _userId;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting _id
    public void setID(int id) {
        this._id = id;
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
    public int getUserID() {
        return this._userId;
    }

    // setting _id
    public void setUserID(int userID) {
        this._userId = userID;
    }

    @Override
    public String toString() {
        return _country + ", " + _friend;
    }
}
