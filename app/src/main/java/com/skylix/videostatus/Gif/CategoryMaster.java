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

import com.skylix.videostatus.ModelClass.CategoryModel;
import com.skylix.videostatus.NavigationMainActivity;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.AlertDialog;
import com.skylix.videostatus.tools.Config;
import com.skylix.videostatus.tools.Connectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CategoryMaster extends ActionBarActivity {


    String intentCategoryAlias,intentLanguage;
    private CategoryMasterAdapter categoryMasterAdapter;
    private LanguageMasterAdapter languageMasterAdapter;
    ArrayList<CategoryModel> al = new ArrayList<CategoryModel>();
    private ProgressDialog pd;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        if (Connectivity.getInstance(CategoryMaster.this).isOnline()) {
            Log.d("Result", "-----------------------CategoryMaster-----------------------------");

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CategoryMaster.this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
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

            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                intentCategoryAlias = intent.getStringExtra(Config.KEY_INTENT_VIDEO);
                intentLanguage = intent.getStringExtra(Config.KEY_INTENT_LANGUAGE);
                Log.d("Result", "getCategoryName:---" + intentCategoryAlias
                        +"-Language-"+intentLanguage);
            }

            pd = new ProgressDialog(this);
            pd.setTitle(getString(R.string.app_name));
            pd.setCancelable(false);
            pd.setMessage("Loading GIF");
            if (intentCategoryAlias!=null) {
                new CategoryJSONTask().execute(Config.GIF_MASTER_URL + intentCategoryAlias);
                Log.d("RESULT", "GIF MASTER URL:--" + Config.GIF_MASTER_URL + intentCategoryAlias);
            }else{
                new LanguageJSONTask().execute(Config.URL_LANGUAGE_SELECT + intentLanguage);
                Log.d("RESULT", "Language Select URL:--" + Config.URL_LANGUAGE_SELECT + intentLanguage);
            }

        } else {
            new AlertDialog(CategoryMaster.this, getString(R.string.no_inernet_title), getString(R.string.no_inernet_message),
                    getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (intentCategoryAlias!=null) {
                    new CategoryJSONTask().execute(Config.GIF_MASTER_URL + intentCategoryAlias);
                    Log.d("RESULT", "GIF MASTER URL:--" + Config.GIF_MASTER_URL + intentCategoryAlias);
                }else{
                    new LanguageJSONTask().execute(Config.URL_LANGUAGE_SELECT + intentLanguage);
                    Log.d("RESULT", "Language Select URL:--" + Config.URL_LANGUAGE_SELECT + intentLanguage);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    public class CategoryJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
            al.clear();
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

                    Log.d("GifURL", "View user Upload URL:--" + dpObject.getString(Config.KEY_THUMNAIL));
                    al.add(gm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            if (al.size() > 0) {

                categoryMasterAdapter = new CategoryMasterAdapter(CategoryMaster.this, al);
                recyclerView.setAdapter(categoryMasterAdapter);
                categoryMasterAdapter.notifyDataSetChanged();

            } else {
                new AlertDialog(CategoryMaster.this, getString(R.string.no_data_message), getString(R.string.no_data_message),
                        getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());

            }
        }
    }

    public class LanguageJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
            al.clear();
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

                    Log.d("GifURL", "View user Upload URL:--" + dpObject.getString(Config.KEY_THUMNAIL));
                    al.add(gm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            if (al.size() > 0) {

                languageMasterAdapter = new LanguageMasterAdapter(CategoryMaster.this, al);
                recyclerView.setAdapter(languageMasterAdapter);
                languageMasterAdapter.notifyDataSetChanged();

            } else {
                new AlertDialog(CategoryMaster.this, getString(R.string.no_data_message), getString(R.string.no_data_message),
                        getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());

            }
        }
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_refresh) {
//            new JSONTask().execute(Config.GIF_MASTER_URL + intentGif);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
