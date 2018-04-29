package com.example.jxr.smarter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jxr.smarter.StringHash;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by jxr on 25/4/18.
 */

public class DBManager {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usage.db";
    private final Context context;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBStructure.tableEntry.TABLE_NAME + " (" +
                    DBStructure.tableEntry._ID + " INTEGER PRIMARY KEY," +
                    DBStructure.tableEntry.COLUMN_ID + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_AIR + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_FRIDGE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_WASH + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_DAY + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_HOUR + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_TEMP + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_RESID + TEXT_TYPE +
                    " );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBStructure.tableEntry.TABLE_NAME;


    private ArrayList<ElectricityUsage> usageList;

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }

    private MySQLiteOpenHelper myDBHelper;
    private SQLiteDatabase db;
    private String[] columns = {
            DBStructure.tableEntry.COLUMN_ID,
            DBStructure.tableEntry.COLUMN_AIR,
            DBStructure.tableEntry.COLUMN_FRIDGE,
            DBStructure.tableEntry.COLUMN_WASH,
            DBStructure.tableEntry.COLUMN_DAY,
            DBStructure.tableEntry.COLUMN_HOUR,
            DBStructure.tableEntry.COLUMN_TEMP,
            DBStructure.tableEntry.COLUMN_RESID
    };

    public DBManager(Context ctx) {
        this.context = ctx;
        myDBHelper = new MySQLiteOpenHelper(context);
    }

    public DBManager open() throws SQLException {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        myDBHelper.close();
    }

    public long insertUsage(String usageid, String air, String fridge, String wash, String day, String hour, String temp, String resid) {
        ContentValues values = new ContentValues();
        values.put(DBStructure.tableEntry.COLUMN_ID, usageid);
        values.put(DBStructure.tableEntry.COLUMN_AIR, air);
        values.put(DBStructure.tableEntry.COLUMN_FRIDGE, fridge);
        values.put(DBStructure.tableEntry.COLUMN_WASH, wash);
        values.put(DBStructure.tableEntry.COLUMN_DAY, day);
        values.put(DBStructure.tableEntry.COLUMN_HOUR, hour);
        values.put(DBStructure.tableEntry.COLUMN_TEMP, temp);
        values.put(DBStructure.tableEntry.COLUMN_RESID, resid);
        return db.insert(DBStructure.tableEntry.TABLE_NAME, null, values);
    }

    public Cursor getAll() {
        return db.query(DBStructure.tableEntry.TABLE_NAME, columns, null, null, null, null, null);
    }



    public int deleteUsage(String rowId) {
        String [] selectionArgs = {
                String.valueOf(rowId)
        };
        String selection = DBStructure.tableEntry.COLUMN_ID + " LIKE?";
        return db.delete(DBStructure.tableEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void dropTable() {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public void reCreateTable() {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public ArrayList<ElectricityUsage> getUsageList() {
        usageList = new ArrayList<>();
        Cursor cursor = getAll();
        while(cursor.moveToNext()) {
            ElectricityUsage usageEntry = new ElectricityUsage(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7));
            usageList.add(usageEntry);
        }
        cursor.close();

        return usageList;
    }
}
