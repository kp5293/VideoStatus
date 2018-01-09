package com.skylix.videostatus.Gif;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.skylix.videostatus.ModelClass.CategoryModel;
import com.skylix.videostatus.NavigationMainActivity;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.AlertDialog;
import com.skylix.videostatus.tools.Config;
import com.skylix.videostatus.tools.Connectivity;
import com.skylix.videostatus.tools.DatabaseHandler;
import com.skylix.videostatus.tools.DownloadTask;
import com.skylix.videostatus.tools.Functions;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Skylix PC1 on 22-11-2017.
 */

public class VideoDetail extends ActionBarActivity {

    ArrayList<CategoryModel> LatestAL = new ArrayList<CategoryModel>();
    private ProgressDialog pd;
    private RecyclerView hrRecyclerView;
    private CategoryDetailLatestAdapter categoryDetailLatestAdapter;
    public VideoView vvGif;
    ImageView imgIncrement, imgDecrement, imgDownload, imgShare;
    TextView rowIncrement, rowDecrement, tvTitle, tvFavCounter, similar;
    DatabaseHandler db;
    boolean IncButtonClicked = false;
    boolean DecButtonClicked = false;
    Toolbar toolbar;
    SparkButton heart_button;
    Integer id, favCounter;
    String intentCateAlias, intentID, strFavVideoTitle, strFavVideoType, strFavVideoAlias, strFavUrl;
    static String downloadUrl = "";
    MediaController mediaController;
    String intentLaest, intentPopular, intentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        if (Connectivity.getInstance(VideoDetail.this).isOnline()) {
            db = new DatabaseHandler(VideoDetail.this);
            vvGif = (VideoView) findViewById(R.id.row_gif);
            rowIncrement = (TextView) findViewById(R.id.row_increment);
            rowDecrement = (TextView) findViewById(R.id.row_decrement);
            tvTitle = (TextView) findViewById(R.id.row_name);
            tvFavCounter = (TextView) findViewById(R.id.favCounter);
            similar = (TextView) findViewById(R.id.similar);

            heart_button = (SparkButton) findViewById(R.id.heart_button);
            mediaController = new MediaController(this);
            imgIncrement = (ImageView) findViewById(R.id.imgIncrement);
            imgDecrement = (ImageView) findViewById(R.id.imgDecrement);
            imgDownload = (ImageView) findViewById(R.id.imgDownload);
            imgShare = (ImageView) findViewById(R.id.imgShare);

            hrRecyclerView = (RecyclerView) findViewById(R.id.hrRecyclerView);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            hrRecyclerView.setLayoutManager(llm);
            hrRecyclerView.setAdapter(categoryDetailLatestAdapter);

            pd = new ProgressDialog(this);
            pd.setTitle(getString(R.string.app_name));
            pd.setMessage("Loading Video...");
            pd.setCancelable(false);

            Intent intent = getIntent();
            if (intent.getExtras() != null) {
//                intentCateAlias = intent.getStringExtra(Config.KEY_INTENT_VIDEO);
                intentCateAlias = intent.getStringExtra("intentCatAlias");
                intentID = intent.getStringExtra(Config.KEY_INTENT_VIDEO);
                Log.d("Result", "getCategoryName:---" + intentCateAlias);
                id = Integer.parseInt(intentID);
                intentLaest = intent.getStringExtra(Config.KEY_INTENT_LATEST);
                Log.d("Result", "Latest Video:---" + intentLaest);

                intentPopular = intent.getStringExtra(Config.KEY_INTENT_POPULAR);
                Log.d("Result", "Popular Video:---" + intentPopular);

                intentLanguage = intent.getStringExtra(Config.KEY_INTENT_LANGUAGE);
                Log.d("Result", "Language Video:---" + intentLanguage);
            }
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {

                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            if (intentLaest != null) {
                new LatestJsonTask().execute(Config.LATEST_MASTER_URL);

            }
            if (intentCateAlias != null) {
                new LatestJsonTask().execute(Config.LATEST_MASTER_URL);

            }
            if (intentPopular != null) {
                new LatestJsonTask().execute(Config.URL_POPULAR_VIDEO_LIST);
            }
            if (intentLanguage != null) {


                new LatestJsonTask().execute(Config.URL_LANGUAGE_SELECT + intentLanguage);
                Log.d("RESULT", "Language Select URL:--" + Config.URL_LANGUAGE_SELECT + intentLanguage);
            }


            new GifDetailJSON().execute(Config.URL_VIDEO_DETAIL + intentID);

            imgIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IncrementListner();
                    Functions.updateGifCounter(VideoDetail.this, Config.URL_VIDEO_LIKES_UPDATE,
                            Config.KEY_ID, intentID, Config.KEY_LIKES, rowIncrement.getText().toString(),
                            Config.KEY_DISLIKES, rowDecrement.getText().toString(), Config.KEY_FAV_COUNTER,
                            String.valueOf(favCounter));

                }
            });

            imgDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DecrementListner();
                    Functions.updateGifCounter(VideoDetail.this, Config.URL_VIDEO_LIKES_UPDATE,
                            Config.KEY_ID, intentID, Config.KEY_LIKES, rowIncrement.getText().toString(),
                            Config.KEY_DISLIKES, rowDecrement.getText().toString(), Config.KEY_FAV_COUNTER,
                            String.valueOf(favCounter));
                }
            });

            imgDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    new DownloadTask(VideoDetail.this, strFavUrl);
                }
            });

            imgShare.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    downloadUrl = strFavUrl;
                    new DownloadingTask().execute();

                }
            });

            try {
//                Log.d("Reading: ", "Reading all contacts..");
                List<CategoryModel> gif = db.getAllVideo();

                for (CategoryModel sm : gif) {

                    if (String.valueOf(id).equals(String.valueOf(sm.getId()))) {
                        heart_button.setChecked(true);
                        String Data = "Fav Id:-" + sm.getFav_id() + "-ID:-" + sm.getId() +
                                "-Cat type:-" + sm.getCat_type() + "-Cat Alias:-" + sm.getCat_alias()
                                + "-Url:-" + sm.getUrl() + " -Likes:-" + sm.getLikes()
                                + "-Dislikes:-" + sm.getDislikes() + "-Fav Counter:-" + sm.getFav_counter();
//                    Log.d("Reading: ", "Reading all contacts2.." + arl.get(position).get(Config.KEY_ID));

                        Log.d("Reading: ", "Reading all contacts1.." + sm.getId());
                        Log.d("Reading: ", "Reading all contacts1.." + Data);
                    } else {
                        Log.d("Reading: ", "Reading all contacts1.." + sm.getId());
                        Log.d("Reading: ", "Reading all contacts1.." + id);
                        Toast.makeText(VideoDetail.this, "Id is different..", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (SQLiteException e) {
                Log.d("result", "Error message==>" + e.toString());
            }

            heart_button.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        favCounter = Integer.parseInt(tvFavCounter.getText().toString().trim());
//                        Log.d("result", "Fav Text ==>" + favCounter);
                        favCounter++;
//                        Log.d("result", "Counter==>" + favCounter);

                        tvFavCounter.setText(String.valueOf(favCounter));
                        String log = " Id: " + id +
                                " Video_Type:-" + strFavVideoType +
                                " Video_Alias:-" + strFavVideoAlias +
                                " Likes:-" + rowIncrement.getText().toString() +
                                " DisLikes:-" + rowDecrement.getText().toString() +
                                " FAV_COUNTER:--" + favCounter;

                        Log.d("Result: ", "Message:--" + log);

                        db.addVideo(new CategoryModel(id, strFavVideoTitle, strFavVideoType, strFavVideoAlias, strFavUrl,
                                String.valueOf(favCounter), rowIncrement.getText().toString(), rowDecrement.getText().toString()));

                        Functions.updateGifCounter(VideoDetail.this, Config.URL_VIDEO_LIKES_UPDATE,
                                Config.KEY_ID, intentID, Config.KEY_LIKES, rowIncrement.getText().toString(),
                                Config.KEY_DISLIKES, rowDecrement.getText().toString(), Config.KEY_FAV_COUNTER,
                                String.valueOf(favCounter));
                    } else {

//                        Log.d("Result", "Button Boolean==>" + buttonState);
                        favCounter = Integer.parseInt(tvFavCounter.getText().toString().trim());
//                        Log.d("result", "Fav Text ==>" + favCounter);
                        favCounter--;
//                        Log.d("result", "Counter==>" + favCounter);
                        tvFavCounter.setText(String.valueOf(favCounter));

                        if (favCounter < 0) {
                            tvFavCounter.setText("0");
                        }
                        db.deleteVideo(new CategoryModel(id));
                        Functions.updateGifCounter(VideoDetail.this, Config.URL_VIDEO_LIKES_UPDATE,
                                Config.KEY_ID, intentID, Config.KEY_LIKES, rowIncrement.getText().toString(),
                                Config.KEY_DISLIKES, rowDecrement.getText().toString(), Config.KEY_FAV_COUNTER,
                                String.valueOf(favCounter));


                    }
                }

                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                }

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                }
            });


        } else {
            new AlertDialog(VideoDetail.this, getString(R.string.no_inernet_title), getString(R.string.no_inernet_message),
                    getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());
        }


    }

    @Override
    public void onStart() {
        super.onStart();
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // To start fetching the data when app start, uncomment below line to start the async task.
//                new LanguageJsonTask().execute(Config.URL_LANGUAGE);
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    @SuppressLint("StaticFieldLeak")
    private class LatestJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
            LatestAL.clear();
        }

        @Override
        protected String doInBackground(String... params) {
            return Config.readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dpObject = jsonArray.getJSONObject(i);
                    if (dpObject.getInt(Config.KEY_ID) != id) {
                        CategoryModel cm = new CategoryModel();
                        cm.setId(dpObject.getInt(Config.KEY_ID));
                        cm.setCat_type(dpObject.getString(Config.KEY_CATEGORY_TYPE));
                        cm.setCat_alias(dpObject.getString(Config.KEY_CATEGORY_ALIAS));
                        cm.setTitle(dpObject.getString(Config.KEY_TITLE));
                        cm.setLanguage(dpObject.getString(Config.KEY_LANGUAGE));
                        cm.setApproved(dpObject.getString(Config.KEY_APPROVED));
                        cm.setFav_counter(dpObject.getString(Config.KEY_FAV_COUNTER));
                        cm.setDate(dpObject.getString(Config.KEY_DATE));
                        cm.setUrl(dpObject.getString(Config.KEY_CATEGORY_URL));
                        cm.setThumnail(dpObject.getString(Config.KEY_THUMNAIL));
                        cm.setEmail(dpObject.getString(Config.KEY_EMAIL));
                        cm.setLikes(dpObject.getString(Config.KEY_LIKES));
                        cm.setDislikes(dpObject.getString(Config.KEY_DISLIKES));

                        Log.d("Result", dpObject.toString());
                        LatestAL.add(cm);

                    } else {
                        Log.d("Result", "Detail Equal:---" + dpObject.getInt(Config.KEY_ID));
                        Log.d("Result", "Detail Equal:---" + id);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            if (LatestAL.size() > 0) {

                categoryDetailLatestAdapter = new CategoryDetailLatestAdapter(VideoDetail.this, LatestAL);
                hrRecyclerView.setAdapter(categoryDetailLatestAdapter);
                categoryDetailLatestAdapter.notifyDataSetChanged();

            } else {
                new AlertDialog(VideoDetail.this, getString(R.string.no_data_title), getString(R.string.no_data_message),
                        getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());

            }

        }
    }

    private void IncrementListner() {
        Log.d("Result", "Boolean:--" + IncButtonClicked);
        if (Integer.parseInt(rowIncrement.getText().toString()) >= 0) {

            if (!IncButtonClicked) {

                String origialIncValue = rowIncrement.getText().toString();
                Integer incrementedValue = Integer.valueOf(origialIncValue) + 1;
                Log.d("Result", "original Vlaue:---" + incrementedValue);
                rowIncrement.setText(String.valueOf(incrementedValue));
                imgIncrement.setImageResource(R.drawable.like_on);
                imgDecrement.setImageResource(R.drawable.dislike_off);
                IncButtonClicked = true;
                if (DecButtonClicked) {

                    String origialValue = rowDecrement.getText().toString();
                    Integer decrementedValue = Integer.valueOf(origialValue) - 1;
                    Log.d("Result", "original Vlaue 1:---" + decrementedValue);
                    rowDecrement.setText(String.valueOf(decrementedValue));
                    DecButtonClicked = false;
                } else {
                    Toast.makeText(VideoDetail.this, "Decrement Button Is not clicked 1...", Toast.LENGTH_SHORT).show();

                }
            } else {
                if (Integer.parseInt(rowDecrement.getText().toString()) <= 0) {
                    rowDecrement.setText("0");
                    Toast.makeText(VideoDetail.this, "Already 0...", Toast.LENGTH_SHORT).show();
                } else {
                    if (!DecButtonClicked) {
                        Toast.makeText(VideoDetail.this, "Decrement Button Is not clicked 1...", Toast.LENGTH_SHORT).show();

                    } else {
                        String origialValue = rowDecrement.getText().toString();
                        Integer incrementedValue = Integer.valueOf(origialValue) - 1;
                        Log.d("Result", "original Vlaue 1:---" + incrementedValue);
                        rowDecrement.setText(String.valueOf(incrementedValue));
                        DecButtonClicked = false;
                    }
                }
            }

        } else {
            rowIncrement.setText("0");
        }
    }

    private void DecrementListner() {
        Log.d("Result", "Boolean:--" + IncButtonClicked);
        if (Integer.parseInt(rowDecrement.getText().toString()) >= 0) {

            if (!DecButtonClicked) {

                String origialDecValue = rowDecrement.getText().toString();
                Integer incrementedValue = Integer.valueOf(origialDecValue) + 1;
                Log.d("Result", "original Vlaue:---" + incrementedValue);
                rowDecrement.setText(String.valueOf(incrementedValue));
                imgDecrement.setImageResource(R.drawable.dislike_on);
                imgIncrement.setImageResource(R.drawable.like_off);
                DecButtonClicked = true;
                if (IncButtonClicked) {

                    String origialValue = rowIncrement.getText().toString();
                    Integer decrementedValue = Integer.valueOf(origialValue) - 1;
                    Log.d("Result", "original Vlaue 1:---" + decrementedValue);
                    rowIncrement.setText(String.valueOf(decrementedValue));

                    IncButtonClicked = false;
                } else {
                    Toast.makeText(VideoDetail.this, "Decrement Button Is not clicked 1...", Toast.LENGTH_SHORT).show();

                }
            } else {
                if (Integer.parseInt(rowIncrement.getText().toString()) <= 0) {
                    rowIncrement.setText("0");
                    Toast.makeText(VideoDetail.this, "Already 0...", Toast.LENGTH_SHORT).show();
                } else {
                    if (!IncButtonClicked) {
                        Toast.makeText(VideoDetail.this, "Decrement Button Is not clicked 1...", Toast.LENGTH_SHORT).show();

                    } else {
                        String origialValue = rowIncrement.getText().toString();
                        Integer decrementedValue = Integer.valueOf(origialValue) - 1;
                        Log.d("Result", "original Vlaue 1:---" + decrementedValue);
                        rowIncrement.setText(String.valueOf(decrementedValue));
                        IncButtonClicked = false;
                    }
                }
            }

        } else {
            rowDecrement.setText("0");
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class GifDetailJSON extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return Config.readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dpObject = jsonArray.getJSONObject(i);

                    id = dpObject.getInt(Config.KEY_ID);
                    strFavVideoTitle = dpObject.getString(Config.KEY_TITLE);
                    strFavVideoType = dpObject.getString(Config.KEY_CATEGORY_TYPE);
                    strFavVideoAlias = dpObject.getString(Config.KEY_CATEGORY_ALIAS);
                    strFavUrl = dpObject.getString(Config.KEY_URL);


                    tvTitle.setText(dpObject.getString(Config.KEY_TITLE));
                    rowIncrement.setText(dpObject.getString(Config.KEY_LIKES));
                    rowDecrement.setText(dpObject.getString(Config.KEY_DISLIKES));
                    tvFavCounter.setText(dpObject.getString(Config.KEY_FAV_COUNTER));
                    Log.d("RESULT", "DP HHASHMAP" + jsonArray.length());
                }

            } catch (NullPointerException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = vvGif.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            vvGif.setLayoutParams(params);
            mediaController.show();
            Uri video = Uri.parse(strFavUrl);
            vvGif.setMediaController(mediaController);
            vvGif.setVideoURI(video);
            vvGif.seekTo(100);
            vvGif.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(100, 100);
                    mediaPlayer.setLooping(true);
                }
            });

        }
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {
        private String downloadFileName = "";
        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadFileName = downloadUrl.replace(Config.mainUrl, "");//Create file name by picking download file name from URL
            Log.e("RESULT", downloadFileName);
//            buttonText.setEnabled(false);
//            buttonText.setText(R.string.downloadStarted);//Set Button Text when download started
            Toast.makeText(VideoDetail.this, R.string.downloadStarted, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
//                    buttonText.setEnabled(true);
//                    buttonText.setText();//If Download completed then change button text
                    Toast.makeText(VideoDetail.this, R.string.downloadCompleted, Toast.LENGTH_SHORT).show();

                } else {
//                    buttonText.setText(R.string.downloadFailed);//If download failed change button text
                    Toast.makeText(VideoDetail.this, R.string.downloadFailed, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            buttonText.setEnabled(true);
//                            buttonText.setText(R.string.downloadAgain);//Change button text again after 3sec
                            Toast.makeText(VideoDetail.this, R.string.downloadCompleted, Toast.LENGTH_SHORT).show();
                        }
                    }, 3000);

                    Log.e("RESULT", "Download Failed");

                }

                String filepath = outputFile.getPath();
                Log.d("RESULT", "filepath:---" + filepath);
                Uri uri = Uri.parse(filepath);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("video/mp4");

                VideoDetail.this.startActivity(Intent.createChooser(shareIntent, "Share Video"));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(VideoDetail.this, R.string.downloadFailed, Toast.LENGTH_SHORT).show();
//                //Change button text if exception occurs
//                buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        buttonText.setEnabled(true);
//                        buttonText.setText(R.string.downloadAgain);
                        Toast.makeText(VideoDetail.this, R.string.downloadCompleted, Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
                Log.e("RESULT", "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("RESULT", "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());
                }
                apkStorage = new File(Environment.getExternalStorageDirectory() + "/"
                        + Config.downloadDirectory + "/");

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e("RESULT", "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File
                MediaScannerConnection.scanFile(VideoDetail.this, new String[]{outputFile.getPath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String s, Uri uri) {

                    }
                });

                Log.e("RESULT", "downloadFileName:---" + downloadFileName);
                Log.e("RESULT", "outputFile:---- " + outputFile);
                if (outputFile.exists())
                    outputFile.delete();
                try {
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
//                outputFile = null;
                Log.e("RESULT", "Download Error Exception " + e.getMessage());

                Log.e("RESULT", "apkStorage:---" + apkStorage);
            }

            return null;
        }
    }
}
