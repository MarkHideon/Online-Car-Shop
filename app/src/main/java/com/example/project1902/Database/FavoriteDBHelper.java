package com.example.project1902.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FavoriteDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Favorites.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "FavoriteCars";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_CAR_TITLE = "carTitle";

    public FavoriteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_CAR_TITLE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isFavorite(String username, String carTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COLUMN_USERNAME + "=? AND " + COLUMN_CAR_TITLE + "=?",
                new String[]{username, carTitle}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addFavorite(String username, String carTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_CAR_TITLE, carTitle);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean removeFavorite(String username, String carTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_USERNAME + "=? AND " + COLUMN_CAR_TITLE + "=?",
                new String[]{username, carTitle});
        return result > 0;
    }

    // ✅ Hàm mới: Lấy danh sách tên các xe yêu thích theo username
    public ArrayList<String> getAllFavoriteTitles(String username) {
        ArrayList<String> favoriteTitles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_CAR_TITLE},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                favoriteTitles.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAR_TITLE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return favoriteTitles;
    }
}
