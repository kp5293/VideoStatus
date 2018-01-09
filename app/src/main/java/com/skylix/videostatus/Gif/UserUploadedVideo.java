package com.skylix.videostatus.Gif;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skylix.videostatus.ModelClass.CategoryModel;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.Config;
import com.skylix.videostatus.tools.Connectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Skylix PC1 on 27-09-2017.
 */

public class UserUploadedVideo extends ActionBarActivity {


    String userChoosenTask;

    private VideoViewUserUploadAdapter gifViewUserUploadAdapter;
    ArrayList<CategoryModel> gifaAl = new ArrayList<CategoryModel>();
    private ProgressDialog pd;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Toolbar toolbar;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
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
                onBackPressed(); // Implemented by activity
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UserUploadedVideo.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        user = FirebaseAuth.getInstance().getCurrentUser();
        pd = new ProgressDialog(this);

        if (Connectivity.getInstance(UserUploadedVideo.this).isOnline()) {

            new GifViewUserDataJson().execute(Config.URL_VIDEO_VIEW_USER_UPLOAD + user.getEmail());
            Log.d("RESULT", "GIF MASTER URL:--" + Config.URL_VIDEO_VIEW_USER_UPLOAD +  user.getEmail());


        } else {
//            new AlertDialog(UserUploadedVideo.this, getString(R.string.alert_title), getString(R.string.inernet_alert_message),
//                    getString(R.string.alert_pos_btn), new NavigationMainActivity());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GifViewUserDataJson().execute(Config.URL_VIDEO_VIEW_USER_UPLOAD + user.getEmail());
                Log.d("RESULT", "GIF MASTER URL:--" + Config.URL_VIDEO_VIEW_USER_UPLOAD +  user.getEmail());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class GifViewUserDataJson extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loadning ..");
            pd.setCancelable(false);
            pd.setTitle(getString(R.string.app_name));
            pd.setCanceledOnTouchOutside(false);

            gifaAl.clear();
        }

        @Override
        protected String doInBackground(String... params) {
            return Config.readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {

            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray(Config.JSON_ARRAY);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dpObject = jsonArray.getJSONObject(i);
                    CategoryModel gm = new CategoryModel();
                    gm.setId(dpObject.getInt(Config.KEY_ID));
                    gm.setCat_type(dpObject.getString(Config.KEY_CATEGORY_TYPE));
                    gm.setCat_alias(dpObject.getString(Config.KEY_CATEGORY_ALIAS));
                    gm.setTitle(dpObject.getString(Config.KEY_TITLE));
                    gm.setLanguage(dpObject.getString(Config.KEY_LANGUAGE));
                    gm.setApproved(dpObject.getString(Config.KEY_APPROVED));
                    gm.setFav_counter(dpObject.getString(Config.KEY_FAV_COUNTER));
                    gm.setDate(dpObject.getString(Config.KEY_DATE));
                    gm.setUrl(dpObject.getString(Config.KEY_URL));
                    gm.setThumnail(dpObject.getString(Config.KEY_THUMNAIL));
                    gm.setEmail(dpObject.getString(Config.KEY_EMAIL));
                    gm.setLikes(dpObject.getString(Config.KEY_LIKES));
                    gm.setDislikes(dpObject.getString(Config.KEY_DISLIKES));

                    gifaAl.add(gm);
                    Log.d("RESULT", "ARRAY LIST:--" + gifaAl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (gifaAl.size() > 0) {
                gifViewUserUploadAdapter = new VideoViewUserUploadAdapter(UserUploadedVideo.this, gifaAl);
                recyclerView.setAdapter(gifViewUserUploadAdapter);
                gifViewUserUploadAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(UserUploadedVideo.this, "No Data Found...", Toast.LENGTH_SHORT).show();
//                new AlertDialog(UserUploadedVideo.this, getString(R.string.alert_title), getString(R.string.no_data_message),
//                        getString(R.string.alert_pos_btn), new NavigationMainActivity());
            }
        }
    }
}
