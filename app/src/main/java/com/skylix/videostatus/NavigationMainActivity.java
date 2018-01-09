package com.skylix.videostatus;


import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.skylix.videostatus.Gif.FragmentCategoryMain;
import com.skylix.videostatus.Gif.FragmentLatest;
import com.skylix.videostatus.Gif.FragmentPopularMain;
import com.skylix.videostatus.Gif.UploadVideoMain;
import com.skylix.videostatus.Gif.VideoFavouriteMaster;
import com.skylix.videostatus.Gif.ViewUserDownload;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Skylix PC1 on 13-11-2017.
 */

public class NavigationMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, 1, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.category:
                viewPager.setCurrentItem(0);
                Toast.makeText(NavigationMainActivity.this, "Category", Toast.LENGTH_SHORT).show();

                break;
            case R.id.latest:
                viewPager.setCurrentItem(1);
                break;
            case R.id.popular:
                viewPager.setCurrentItem(2);
                break;
            case R.id.upload:
                viewPager.setCurrentItem(3);
                break;
            case R.id.favourite:
                Intent intent = new Intent(NavigationMainActivity.this, VideoFavouriteMaster.class);
                startActivity(intent);
                break;
            case R.id.userDownload:
                viewPager.setCurrentItem(4);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentCategoryMain(), "Category");
        adapter.addFrag(new FragmentLatest(), "Latest");
        adapter.addFrag(new FragmentPopularMain(), "Popular");
        adapter.addFrag(new UploadVideoMain(), "Upload");
        adapter.addFrag(new ViewUserDownload(), "Downloads");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
//        if (navItemIndex == 5) {
        getMenuInflater().inflate(R.menu.logout, menu);
//        }

        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.main, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            FirebaseAuth.getInstance().signOut();
//                            Intent i1 = new Intent(ChooserActivity.this, GoogleSignInActivity.class);
//                            startActivity(i1);
//                            Toast.makeText(ChooserActivity.this, getString(R.string.toast_logout), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
                        }
                    });
            return (true);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}