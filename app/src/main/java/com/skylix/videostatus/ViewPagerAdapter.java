package com.skylix.videostatus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.skylix.videostatus.Gif.FragmentCategoryMain;
import com.skylix.videostatus.Gif.FragmentLatest;
import com.skylix.videostatus.Gif.FragmentPopularMain;
import com.skylix.videostatus.Gif.UploadVideoMain;
import com.skylix.videostatus.Gif.ViewUserDownload;

/**
 * Created by Skylix PC1 on 13-11-2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentCategoryMain();
        } else if (position == 1) {
            return new FragmentLatest();
        } else if (position == 2) {
            return new FragmentPopularMain();
        }else if (position == 3) {
            return new UploadVideoMain();
        }
        else return new ViewUserDownload();
    }

    @Override
    public int getCount() {
        return 5;
    }
}