package com.skylix.videostatus.demo;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skylix.videostatus.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Skylix PC1 on 14-11-2017.
 */

public class demo1 extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Cursor cursor;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<VideoViewInfo> videoRows = new ArrayList<VideoViewInfo>();
        ListView listView = (ListView) findViewById(R.id.ListView);

        String[] mediaColumns = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE};
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, MediaStore.Video.Media.DATA + " like?", new String[]{"%VideoStatus%"}, null);


        if (cursor.moveToFirst()) {

            do {
                VideoViewInfo newVVI = new VideoViewInfo();

                newVVI.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                newVVI.thumbnail = ThumbnailUtils.createVideoThumbnail(newVVI.filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                newVVI.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                Log.v("VideoGallery", "Title " + newVVI.title);
                newVVI.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                Log.v("VideoGallery", "Mime " + newVVI.mimeType);

                videoRows.add(newVVI);
            } while (cursor.moveToNext());

        }
        listView.setAdapter(new VideoGalleryAdapter(this, videoRows));
        listView.setOnItemClickListener(this);

    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

        int fileColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        int mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);
        String videoFilePath = cursor.getString(fileColumn);
        String mimeType = cursor.getString(mimeColumn);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        File newFile = new File(videoFilePath);
        intent.setDataAndType(Uri.fromFile(newFile), mimeType);
        startActivity(intent);

    }


    class VideoViewInfo {
        String filePath;
        String mimeType;

        String title;
        Bitmap thumbnail;
    }

    class VideoGalleryAdapter extends BaseAdapter {
        private Context context;
        private List<VideoViewInfo> videoItems;
        LayoutInflater inflater;

        public VideoGalleryAdapter(Context _context, ArrayList<VideoViewInfo> _items) {
            context = _context;
            videoItems = _items;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return videoItems.size();
        }

        public Object getItem(int position) {
            return videoItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            View videoRow = inflater.inflate(R.layout.list_item, null);

            ImageView videoThumb = (ImageView) videoRow.findViewById(R.id.ImageView);
            videoThumb.setImageBitmap(videoItems.get(position).thumbnail);

            TextView videoTitle = (TextView) videoRow.findViewById(R.id.TextView);
            videoTitle.setText(videoItems.get(position).title);

            return videoRow;
        }
    }
}


