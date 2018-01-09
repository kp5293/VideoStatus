package com.skylix.videostatus.Gif;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skylix.videostatus.NavigationMainActivity;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.AlertDialog;
import com.skylix.videostatus.tools.Config;
import com.skylix.videostatus.tools.Connectivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Skylix PC1 on 08-12-2017.
 */

public class ViewUserDownload extends Fragment {
    static Cursor cursor;
    RecyclerView recyclerView;
    private View FragmentView;
    ArrayList<VideoViewInfo> videoRows = new ArrayList<VideoViewInfo>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentView = inflater.inflate(R.layout.demo, null);


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

    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String[] mediaColumns = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE};

         cursor = getActivity().managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, MediaStore.Video.Media.DATA + " like?", new String[]{"%VideoStatus%"}, null);


        if (cursor.moveToFirst()) {

            videoRows.clear();
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

        recyclerView.setAdapter(new VideoGalleryAdapter(getActivity(), videoRows));

    }


    class VideoViewInfo {
        String filePath;
        String mimeType;

        String title;
        Bitmap thumbnail;
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
        public VideoGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_view_user_download, parent, false);

            return new VideoGalleryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final VideoGalleryAdapter.MyViewHolder holder, final int position) {

//            Glide.with(activity)
//                    .load(videoItems.get(position).thumbnail)
//                    .placeholder(R.drawable.gificon)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.videoThumb);


            String renameTitle = Config.renameDownloadTitle(videoItems.get(position).title);
            holder.videoTitle.setText(videoItems.get(position).title);
            holder.videoThumb.setImageBitmap(videoItems.get(position).thumbnail);

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
                }
            });


        }
    }
}

