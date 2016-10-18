package com.example.kunal.imagesqlitemachinetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KUNAL on 10/7/2016.
 */

public class DBHelper {

    private final Context mContext;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    List<byte[]> listofImages = new ArrayList<byte[]>();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ImageDB";
    private static final String TABLE = "Details";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";

    private static final String CREATE_IMAGES_TABLE =
            "CREATE TABLE " + TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_IMAGE + " BLOB NOT NULL );";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_IMAGES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_IMAGES_TABLE);
            onCreate(db);
        }
    }

    public DBHelper(Context ctx) {
        mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public DBHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    // Insert the image to the Sqlite DB
    public void insertImage(byte[] imageBytes) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_IMAGE, imageBytes);
        mDb.insert(TABLE, null, cv);
    }


    public List<byte[]> retreiveImagesFromDB() {
        listofImages.clear();
        Cursor cur = mDb.query(true, TABLE, new String[]{KEY_IMAGE,},
                null, null, null, null,
                KEY_ID + " DESC", null);

        if(cur!= null) {
            if(cur.moveToFirst()) {
                do {
                    byte[] blob = cur.getBlob(cur.getColumnIndex(KEY_IMAGE));
                    listofImages.add(blob);
                }
                while(cur.moveToNext());
            }
        }

        cur.close();
        return listofImages;
    }
}