package com.skylix.videostatus.tools;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Destiny on 08-Jul-17.
 */

public class Config {

//    public static final String masterUrl = "http://192.168.0.102/admin/videostatus/";
        public static final String masterUrl = "http://www.skylixtechnology.in/admin/videostatus/";

    //JSON array name
    public static final String JSON_ARRAY = "result";
    public static final String downloadDirectory = "VideoStatus";
    public static final String mainUrl = masterUrl + "uploads/";

    public static final String IMAGE_UPLOAD_URL = masterUrl + "UploadVideoMain.php";
    public static final String URL_VIDEO_CATEGORY = masterUrl + "VideoCategoryList.php?submit=dp";
    public static final String LATEST_MASTER_URL = masterUrl + "LatestvideoList.php";
    public static final String URL_POPULAR_VIDEO_LIST= masterUrl + "PopularVideoList.php";
    public static final String URL_LANGUAGE = masterUrl + "LanguageList.php";
    public static final String GIF_MASTER_URL = masterUrl + "VideoSelectedCategory.php?cat_alias=";
    public static final String URL_LANGUAGE_SELECT = masterUrl + "VideoSelectedLAnguage.php?language=";
    public static final String URL_USER_UPLOAD = masterUrl + "UserVideoMasterAdd.php";
    public static final String URL_FILL_CATEGORY_SPINNER = masterUrl + "FillCategorySpinner.php";
    public static final String URL_FILL_LANGUAGE_SPINNER = masterUrl + "FillLanguage.php";
    public static final String URL_VIDEO_LIKES_UPDATE = masterUrl + "VideoLikesUpdate.php";
    public static final String URL_VIDEO_DETAIL = masterUrl + "VideoDetail.php?id=";
    public static final String URL_VIDEO_VIEW_USER_UPLOAD = masterUrl + "VideoViewUserUpload.php?email=";


    public final static String APP_TITLE = "Dp and Status";
    public final static String APP_PACKAGE_NAME = "snapapp.blooddonor.bloodfinder";

    //Keys that will be used to send the request to php scripts

    public static final String KEY_APPROVED = "approved";
    public static final String KEY_EMAIL = "email";

    public static final String KEY_CATEGORY_ID = "cat_id";
    public static final String KEY_CATEGORY_TYPE = "cat_type";
    public static final String KEY_CATEGORY_ALIAS = "cat_alias";
    public static final String KEY_CATEGORY_URL = "url";
    public static final String KEY_LANGUAGE = "language";

    public static final String KEY_ID = "id";
    public static final String KEY_FAV_COUNTER = "fav_counter";
    public static final String KEY_DATE = "date";
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";
    public static final String KEY_THUMNAIL = "thumnail";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_DISLIKES = "dislikes";

    public static final String KEY_INTENT_VIDEO = "intentVideo";
    public static final String KEY_INTENT_LANGUAGE = "intentLanguauge";
    public static final String KEY_INTENT_LATEST = "intentLatest";
    public static final String KEY_INTENT_POPULAR= "intentPopular";


    @NonNull
    public static String readURL(String theUrl) {
            StringBuilder content = new StringBuilder();
            try {
                // create a url object
                URL url = new URL(theUrl);
                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();
                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return content.toString();
    }

    public static String getPath(Uri uri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public static String getVideoPath(Uri uri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = activity.getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    public static String createAlias(String i1) {

        String input = i1.replace(" ", "").toLowerCase();
        Log.d("RESULT", "DEMO:----" + input);
        return input;
    }
    public static String renameTitle(String i1) {

        String input = i1.replace(" ", "_").toLowerCase();
        Log.d("RESULT", "DEMO:----" + input);
        return input;
    }
    public static String renameDownloadTitle(String i1) {

        String input = i1.replace("_", " ").toLowerCase();
        Log.d("RESULT", "DEMO:----" + input);
        return input;
    }
}