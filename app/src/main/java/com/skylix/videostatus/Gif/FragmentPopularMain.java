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
 * Created by Skylix PC1 on 15-11-2017.
 */

public class FragmentPopularMain extends Fragment {

    ArrayList<CategoryModel> LatestAL = new ArrayList<CategoryModel>();
    private View FragmentView;
    private ProgressDialog pd;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PopularVideoMainAdapter popularVideoMainAdapter;

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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // To start fetching the data when app start, uncomment below line to start the async task.
                new CategoryJsonTask().execute(Config.URL_POPULAR_VIDEO_LIST);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void init(View view) {


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(popularVideoMainAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);


        pd = new ProgressDialog(getActivity());
        pd.setTitle(getString(R.string.app_name));
        pd.setMessage("Loading Sub Category For GIF ...");
        pd.setCancelable(false);

        // To start fetching the data when app start, uncomment below line to start the async task.
        new CategoryJsonTask().execute(Config.URL_POPULAR_VIDEO_LIST);

    }


    @SuppressLint("StaticFieldLeak")
    private class CategoryJsonTask extends AsyncTask<String, Void, String> {

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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
            if (LatestAL.size() > 0) {

                popularVideoMainAdapter = new PopularVideoMainAdapter(getActivity(), LatestAL);
                recyclerView.setAdapter(popularVideoMainAdapter);
                popularVideoMainAdapter.notifyDataSetChanged();

            } else {
                new AlertDialog(getActivity(), getString(R.string.no_data_title), getString(R.string.no_data_message),
                        getString(R.string.no_inernet_pos_btn), new NavigationMainActivity());

            }

        }
    }
}