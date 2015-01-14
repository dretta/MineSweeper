package com.daniel.minesweeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 1/12/2015.
 */
public class Database extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "sessionsManager.db";

    // Sessions table name
    private static final String TABLE_SESSIONS = "sessions";

    // Sessions Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_RESULT = "result";
    private static final String KEY_TIME = "time";
    private static final String KEY_EX = "exploration";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "("
                + KEY_ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP
                + KEY_RESULT + INTEGER_TYPE + COMMA_SEP
                + KEY_TIME + REAL_TYPE + COMMA_SEP
                + KEY_EX + REAL_TYPE + ")";
        db.execSQL(CREATE_SESSIONS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);

        // Create tables again
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    // Adding new session
    public void addSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,session.getId());
        values.put(KEY_RESULT,session.getResult()?1:0);
        values.put(KEY_TIME,(session.getTime()!=0.0f?session.getTime():null));
        Log.d("ADD_EX",""+session.getExploration());
        values.put(KEY_EX,session.getExploration());

        db.insert(TABLE_SESSIONS, null, values);
        db.close();
    }

    // Getting single session
    public Session getSession(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SESSIONS, new String[] { KEY_ID,
                        KEY_RESULT, KEY_TIME, KEY_EX }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Session session = new Session(Integer.parseInt(cursor.getString(0)),
                (cursor.getInt(1)==1), Float.parseFloat(cursor.getString(2)), cursor.getString(3)!=null?Float.parseFloat(cursor.getString(3)):0.0f);
        db.close();
        return session;
    }

    // Getting All Sessions
    public List<Session> getAllSessions() {
        List<Session> sessionList = new ArrayList<Session>();
        String selectQuery = "SELECT  * FROM " + TABLE_SESSIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        float time;
        if (cursor.moveToFirst()) {
            do {
                Session session = new Session();
                session.setId(Integer.parseInt(cursor.getString(0)));
                session.setResult(Integer.parseInt(cursor.getString(1)) == 1);
                time = (cursor.getString(2)!=null)?Float.parseFloat(cursor.getString(2)):0.0f;
                session.setTime(time);
                session.setExploration(Float.parseFloat(cursor.getString(3)));
                sessionList.add(session);
            } while (cursor.moveToNext());
        }
        db.close();
        return sessionList;
    }

    // Getting All Sessions with the same result
    public int getAllSessionsCountByResult(boolean result) {
        List<Session> sessionList = new ArrayList<Session>();
        String countQuery = "SELECT  * FROM " + TABLE_SESSIONS + " WHERE " + KEY_RESULT + " = " + (result?1:0);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        //db.close();
        return cursor.getCount();
    }


    // Getting sessions Count
    public int getSessionsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SESSIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        //db.close();
        return cursor.getCount();
    }

    public float getBestTime(){
        String query = "SELECT  MIN("+KEY_TIME+")" + " FROM " + TABLE_SESSIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor.getFloat(0);
    }

    public float getAverageTime(){
        String query = "SELECT  AVG("+KEY_TIME+")" + " FROM " + TABLE_SESSIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor.getFloat(0);
    }

    public float getExplorationPercent(){
        String query = "SELECT  AVG("+KEY_EX+")" + " FROM " + TABLE_SESSIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        float EX = cursor.getFloat(0);
        Log.d("EX",""+EX);
        float EX_100 = EX*100.0f;
        Log.d("EX_100",""+EX_100);
        String EX_STR = String.format("%.2f",EX_100);
        Log.d("EX_STR",EX_STR);
        float GET_EX = Float.parseFloat(EX_STR);
        Log.d("GET_EX",""+GET_EX);
        return GET_EX;
    }

    // Deleting single session
    public void deleteAllSessions() {
        List<Session> sessionList = getAllSessions();
        SQLiteDatabase db = this.getWritableDatabase();
        for (Session session : sessionList) {
            db.delete(TABLE_SESSIONS, KEY_ID + " = ?",
                    new String[] { String.valueOf(session.getId()) });
        }
        db.close();
    }

    
}