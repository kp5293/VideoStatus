package com.skylix.videostatus.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.skylix.videostatus.ModelClass.CategoryModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Destiny on 22-Jul-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "videostatus";


    private static final String TABLE_VIDEO_FAVOURITE = "video_favourite";
    // TABLE_JOKE Table Columns names
    private static final String KEY_VIDEO_FAV_ID = "fav_id";
//    private static final String KEY_VIDEO_ID = "id";
//    private static final String KEY_VIDEO_CAT_TYPE = "cat_type";
//    private static final String KEY_VIDEO_TYPE = "cat_alias";
//    private static final String KEY_VIDEO_URL = "url";
//    private static final String KEY_VIDEO_FAV_COUNTER = "fav_counter";
//    private static final String KEY_VIDEO_LIKES = "likes";
//    private static final String KEY_VIDEO_DISLIKES = "dislikes";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        Log.d("Result","Path:-"+Environment.getExternalStorageDirectory()
                + File.separator + Config.downloadDirectory
                + File.separator + DATABASE_NAME );
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_GIF_TABLE = "CREATE TABLE " + TABLE_VIDEO_FAVOURITE + "("
                + KEY_VIDEO_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Config.KEY_ID + " INTEGER,"
                + Config.KEY_TITLE + " TEXT,"
                + Config.KEY_CATEGORY_TYPE + " TEXT,"
                + Config.KEY_CATEGORY_ALIAS + " TEXT,"
                + Config.KEY_URL + " TEXT,"
                + Config.KEY_LIKES + " TEXT,"
                + Config.KEY_DISLIKES + " TEXT,"
                + Config.KEY_FAV_COUNTER + " TEXT" + ")";
        Log.d("Result","VIdeo List:-"+CREATE_GIF_TABLE);
        db.execSQL(CREATE_GIF_TABLE);
            }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO_FAVOURITE);
        onCreate(db);
    }

    public void addVideo(CategoryModel categoryModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Config.KEY_ID, categoryModel.getId());
        values.put(Config.KEY_TITLE, categoryModel.getTitle());
        values.put(Config.KEY_CATEGORY_TYPE, categoryModel.getCat_type());
        values.put( Config.KEY_CATEGORY_ALIAS, categoryModel.getCat_alias());
        values.put( Config.KEY_URL, categoryModel.getUrl());
        values.put(Config.KEY_LIKES, categoryModel.getLikes());
        values.put(Config.KEY_DISLIKES, categoryModel.getDislikes());
        values.put(Config.KEY_FAV_COUNTER, categoryModel.getFav_counter());

        db.insert(TABLE_VIDEO_FAVOURITE, null, values);
        db.close();
    }

    public void deleteVideo(CategoryModel gifModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIDEO_FAVOURITE, Config.KEY_ID + " = ?",
                new String[]{String.valueOf(gifModel.getId())});
        db.close();
    }

    public List<CategoryModel> getAllVideo() {
        List<CategoryModel> gifModelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_FAVOURITE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CategoryModel gm = new CategoryModel();
                gm.setFav_id(cursor.getInt(0));
                gm.setId(cursor.getInt(1));
                gm.setTitle(cursor.getString(2));
                gm.setCat_type(cursor.getString(3));
                gm.setCat_alias(cursor.getString(4));
                gm.setUrl(cursor.getString(5));
                gm.setLikes(cursor.getString(6));
                gm.setDislikes(cursor.getString(7));
                gm.setFav_counter(cursor.getString(8));
                // Adding contact to list
                gifModelList.add(gm);
                Log.d("Result","VIdeo List:-"+gifModelList);
            } while (cursor.moveToNext());
        }
        return gifModelList;
    }

}