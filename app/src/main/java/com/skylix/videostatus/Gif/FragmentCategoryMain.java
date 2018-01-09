package com.skylix.videostatus.Gif;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

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

/**
 * Created by Destiny on 12-Aug-17.
 */

public class FragmentCategoryMain extends Fragment {

    //Initialize Spinner

    ArrayList<CategoryModel> CategoryAL = new ArrayList<CategoryModel>();
    ArrayList<CategoryModel> LanguageAL = new ArrayList<CategoryModel>();
    private View FragmentView;
    private ProgressDialog pd;

    private RecyclerView recyclerView, hrRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CategoryMainAdapter categoryMainAdapter;
    private LanguageMainAdapter languageMainAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentView = inflater.inflate(R.layout.recycler_view, null);


        if (Connectivity.getInstance(getActivity()).isOnline()) {
            init(FragmentView);
        } else {
            new AlertDialog(getActivity(), getString(R.string.no_inernet_title), getString(R.string.no_inernet_message),
                    getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());
        }

        return FragmentView;

    }

    @Override
    public void onStart() {
        super.onStart();
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // To start fetching the data when app start, uncomment below line to start the async task.
//                new CategoryJsonTask().execute(Config.URL_VIDEO_CATEGORY);
////                new LanguageJsonTask().execute(Config.URL_LANGUAGE);
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    @SuppressLint("ResourceAsColor")
    private void init(View view) {


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(categoryMainAdapter);

        hrRecyclerView = (RecyclerView) view.findViewById(R.id.hrRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        hrRecyclerView.setLayoutManager(llm);
        hrRecyclerView.setAdapter( languageMainAdapter );

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeColors (R.color.colorAccent);

        pd = new ProgressDialog(getActivity());
        pd.setTitle(getString(R.string.app_name));
        pd.setMessage("Loading Category For Video Status...");
        pd.setCancelable(false);

        // To start fetching the data when app start, uncomment below line to start the async task.
        new CategoryJsonTask().execute(Config.URL_VIDEO_CATEGORY);
        new LanguageJsonTask().execute(Config.URL_LANGUAGE);

    }

    @SuppressLint("StaticFieldLeak")
    private class LanguageJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
            LanguageAL.clear();
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
                    CategoryModel cm = new CategoryModel();
                    cm.setId(dpObject.getInt(Config.KEY_ID));
                    cm.setLanguage(dpObject.getString(Config.KEY_LANGUAGE));
                    Log.d("Result", dpObject.toString());
                    LanguageAL.add(cm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            if (LanguageAL.size() > 0) {

                languageMainAdapter = new LanguageMainAdapter(getActivity(), LanguageAL);
                hrRecyclerView.setAdapter(languageMainAdapter);
                categoryMainAdapter.notifyDataSetChanged();

            } else {
                new AlertDialog(getActivity(), getString(R.string.no_data_title), getString(R.string.no_data_message),
                        getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());

            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class CategoryJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
            CategoryAL.clear();
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
                    CategoryModel cm = new CategoryModel();
                    cm.setCat_id(dpObject.getInt(Config.KEY_CATEGORY_ID));
                    cm.setCat_type(dpObject.getString(Config.KEY_CATEGORY_TYPE));
                    cm.setCat_alias(dpObject.getString(Config.KEY_CATEGORY_ALIAS));
                    cm.setUrl(dpObject.getString(Config.KEY_CATEGORY_URL));
                    Log.d("Result", dpObject.toString());
                    CategoryAL.add(cm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            if (CategoryAL.size() > 0) {

                categoryMainAdapter = new CategoryMainAdapter(getActivity(), CategoryAL);
                recyclerView.setAdapter(categoryMainAdapter);
                categoryMainAdapter.notifyDataSetChanged();

            } else {
                new AlertDialog(getActivity(), getString(R.string.no_data_title), getString(R.string.no_data_message),
                        getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());

            }

        }
    }

}