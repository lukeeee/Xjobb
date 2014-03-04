package se.xjobb.scardfeud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas on 2014-02-27.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "friendManager";
    // Contacts table name
    private static final String TABLE_FRIENDS = "friends";
    // Contacts Table Columns names
    private static final String KEY_ID = "user_id";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_COUNTRY + " TEXT,"
                + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_FRIENDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addFriend(Friends contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getUserID());
        values.put(KEY_COUNTRY, contact.getCountry()); // Country
        values.put(KEY_NAME, contact.getFriend()); // Friend

        // Inserting Row
        db.insert(TABLE_FRIENDS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Friends getFriends(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FRIENDS, new String[]{KEY_ID,
                KEY_COUNTRY, KEY_NAME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Friends country = new Friends((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return country
        return country;
    }

    // Getting All Country
    public List<Friends> getAllContacts() {
        List<Friends> contactList = new ArrayList<Friends>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FRIENDS +
                " ORDER BY name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Friends contact = new Friends();
                contact.setUserID(cursor.getString(0));
                contact.setCountry(cursor.getString(1));
                contact.setFriend(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single Country
    public int updateCountry(Friends contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getUserID());
        values.put(KEY_COUNTRY, contact.getCountry());
        values.put(KEY_NAME, contact.getFriend());

        // updating row
        return db.update(TABLE_FRIENDS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getUserID())});
    }

    // Deleting single Country
    public void deleteFriend(Friends friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIENDS, KEY_ID + " = ?",
                new String[]{String.valueOf(friend.getUserID())});
        db.close();
    }

    // Getting Country Count
    public int getCountryCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FRIENDS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
