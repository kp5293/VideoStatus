package com.skylix.videostatus.Gif;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.skylix.videostatus.ModelClass.CategoryModel;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.Connectivity;
import com.skylix.videostatus.tools.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by NP on 26-Jan-16.
 */
public class VideoFavouriteMaster extends AppCompatActivity {

    ArrayList<CategoryModel> r = new ArrayList<>();
    private DatabaseHandler db;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    VideoFavouriteMasterAdapter gifFavouriteMasterAdapter;
    Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        if (Connectivity.getInstance(VideoFavouriteMaster.this).isOnline()) {

            db = new DatabaseHandler(this);

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VideoFavouriteMaster.this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
recyclerView.setAdapter(gifFavouriteMasterAdapter);
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);


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
            try {
                r.clear();
                Log.d("Reading: ", "Reading all contacts..");
                List<CategoryModel> gifModelList = db.getAllVideo();

                for (CategoryModel sm : gifModelList) {
                    sm.getFav_id();
                    sm.getId();
                    sm.getTitle();
                    sm.getCat_type();
                    sm.getCat_alias();
                    sm.getUrl();
                    sm.getFav_counter();
                    sm.getLikes();
                    sm.getDislikes();
                    r.add(sm);

                    String FavMasterLog=  sm.getFav_id()+ "--" +
                    sm.getId()+ "--" +
                    sm.getTitle()+ "--" +
                    sm.getCat_type()+ "--" +
                    sm.getCat_alias()+ "--" +
                    sm.getUrl()+ "--" +
                    sm.getFav_counter()+ "--" +
                    sm.getLikes()+ "--" +
                    sm.getDislikes();
                    Log.d("Database", " FavMasterLog==>" + FavMasterLog);
                }
            } catch (SQLiteException e) {
                Log.d("result", "Error message==>" + e.toString());
            }
            if (r.size() > 0) {

                gifFavouriteMasterAdapter = new VideoFavouriteMasterAdapter(this, r);
                recyclerView.setAdapter(gifFavouriteMasterAdapter);
                gifFavouriteMasterAdapter.notifyDataSetChanged();
                Toast.makeText(this, "No Found..", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
//                new AlertDialog(VideoFavouriteMaster.this, getString(R.string.alert_title), getString(R.string.no_data_message),
//                        getString(R.string.alert_pos_btn),new NavigationMainActivity());
            }

        } else {
            Toast.makeText(this, "No I", Toast.LENGTH_SHORT).show();
//            new AlertDialog(VideoFavouriteMaster.this, getString(R.string.alert_title), getString(R.string.inernet_alert_message),
//                    getString(R.string.alert_pos_btn),new NavigationMainActivity());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

//    public void shuffle() {
//        Collections.shuffle(r, new Random(System.currentTimeMillis()));
//
//        gifFavouriteMasterAdapter = new VideoFavouriteMasterAdapter(this, r);
//        recyclerView.setAdapter(gifFavouriteMasterAdapter);
//        gifFavouriteMasterAdapter.notifyDataSetChanged();
//
//    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
