package com.skylix.videostatus.demo;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.skylix.videostatus.demo.demo2_1.cursor;

/**
 * Created by Skylix PC1 on 14-11-2017.
 */

public class demo2_1 extends Activity implements AdapterView.OnItemClickListener {
    static  Cursor cursor;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(demo2_1.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};

        String[] mediaColumns = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE};

        cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, MediaStore.Video.Media.DATA + " like?", new String[]{"%VideoStatus%"}, null);

        ArrayList<VideoViewInfo> videoRows = new ArrayList<VideoViewInfo>();

        if (cursor.moveToFirst()) {
            do {

                VideoViewInfo newVVI = new VideoViewInfo();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor = managedQuery(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    newVVI.thumbPath = thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                    Log.v("", newVVI.thumbPath);
                }

                newVVI.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                newVVI.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                Log.v("", newVVI.title);
                newVVI.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                Log.v("", newVVI.mimeType);
                videoRows.add(newVVI);
            } while (cursor.moveToNext());
        }
        recyclerView.setAdapter(new VideoGalleryAdapter(this, videoRows));

    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

    }
}

class VideoViewInfo {
    String filePath;
    String mimeType;
    String thumbPath;
    String title;
}

class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.MyViewHolder> {

    private Activity activity;
    private List<VideoViewInfo> videoItems;

    public VideoGalleryAdapter(Activity activity, List<VideoViewInfo> videoItems) {
        this.activity = activity;
        this.videoItems = videoItems;

    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView videoThumb;
        public TextView videoTitle;


        View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;

            videoThumb = (ImageView) view.findViewById(R.id.image);
            videoTitle = (TextView) view.findViewById(R.id.text);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_user_download, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (videoItems.get(position).thumbPath != null) {
//            videoThumb.setImageURI(Uri
//                    .parse(videoItems.get(position).thumbPath));

            Glide.with(activity)
                    .load(videoItems.get(position).thumbPath)
                    .placeholder(R.drawable.gificon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.videoThumb);

        }
        String renameTitle = Config.renameDownloadTitle(videoItems.get(position).title);
        holder.videoTitle.setText(renameTitle);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor.moveToPosition(position)) {
                    int fileColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                    int mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);
                    String videoFilePath = cursor.getString(fileColumn);
                    String mimeType = cursor.getString(mimeColumn);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    File newFile = new File(videoFilePath);
                    intent.setDataAndType(Uri.fromFile(newFile), mimeType);
                   activity.startActivity(intent);
                }
//                Intent intent = new Intent(activity, VideoDetail.class);
////                intent.putExtra(Config.KEY_INTENT_VIDEO, listGif.get(position).getUrl());
////                intent.putExtra("ID", String.valueOf(listGif.get(position).getId()));
//                intent.putExtra(Config.KEY_INTENT_VIDEO, String.valueOf(listGif.get(position).getId()));
//                Log.d("Result", "Category Master Adapter:---" + listGif.get(position).getId());
//                activity.startActivity(intent);

            }
        });


    }
}