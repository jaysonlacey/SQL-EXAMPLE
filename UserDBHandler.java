package com.tippool.jayson.tippool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class UserDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userdb.db";
    private static final String TABLE_NAME = "usertable";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME_USERNAME = "username";
    private static final String COLUMN_NAME_TIPAMOUNT = "tipamount";
    private static final String COLUMN_NAME_HOURAMOUNT = "houramount";
    private static final String COLUMN_NAME_RESULTAMOUNT = "resultamount";

    public UserDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                "'" + COLUMN_NAME_USERNAME + "'" + " TEXT " +
                COLUMN_NAME_TIPAMOUNT + " INTEGER " +
                COLUMN_NAME_HOURAMOUNT + " INTEGER " +
                COLUMN_NAME_RESULTAMOUNT + " INTEGER" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    /**
    Add a new user to the database.
    @param userName The user's name to add.
     */
    void addUser(String userName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USERNAME, userName);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
        Log.i("UserDBHandler", "User created with name: " + userName);
    }

    /**
     * Delete a user from the database.
     * @param userName The user's name to remove from the existing database.
     */
    public void deleteUser(String userName) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + "'" + TABLE_NAME + "'" + " WHERE " + COLUMN_NAME_USERNAME + " = \"" + userName + "\";");
    }

    /**
     * Retrieve each user in the database and store it into an array.
     * @return The array of users from the database.
     */
    public ArrayList<String> getUserArray() {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName;
        int counter = getNumberOfUsers();
        ArrayList<String> userList = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT " + COLUMN_NAME_USERNAME + " FROM " + TABLE_NAME + ";", null);

        c.moveToFirst();

        while (counter > 0) {
            if (c.getString(c.getColumnIndex(COLUMN_NAME_USERNAME)) != null) {
                userName = c.getString(c.getColumnIndex(COLUMN_NAME_USERNAME));
                userList.add(userName);
            }
            c.moveToNext();
            counter--;
        }
        c.close();
        return userList;
    }

    /**
     * Retrieve the number of stored users in the database.
     * @return The Integer result containing the number of users in the database.
     */
    public int getNumberOfUsers() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);
        int cnt = c.getCount();
        c.close();
        return cnt;
    }
}
