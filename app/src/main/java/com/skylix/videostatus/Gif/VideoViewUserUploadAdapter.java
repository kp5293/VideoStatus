package com.skylix.videostatus.Gif;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.skylix.videostatus.ModelClass.CategoryModel;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.DatabaseHandler;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;


/**
 * Created by Destiny on 10-Jul-17.
 */

public class VideoViewUserUploadAdapter extends RecyclerView.Adapter<VideoViewUserUploadAdapter.MyViewHolder> {


    List<CategoryModel> listGif;
    private Activity activity;
    private DatabaseHandler db;
    private int id, favCounter;

    public VideoViewUserUploadAdapter(Activity activity, List<CategoryModel> listGif) {
        this.activity = activity;
        this.listGif = listGif;
        db = new DatabaseHandler(activity);
    }

    @Override
    public int getItemCount() {
        return listGif.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvId, tvType, tvGifFavCounter, tvApprovalStatus,tvVideoTitle;
        SparkButton heart_button;
        public VideoView vvGif;
        View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;

            tvId = (TextView) view.findViewById(R.id.id);
            tvType = (TextView) view.findViewById(R.id.type);
            heart_button = (SparkButton) view.findViewById(R.id.heart_button);
            tvGifFavCounter = (TextView) view.findViewById(R.id.row_dp_fav_counter);
            vvGif = (VideoView) view.findViewById(R.id.row_gif);
            tvApprovalStatus = (TextView) view.findViewById(R.id.approval_status);
            tvVideoTitle = (TextView) view.findViewById(R.id.row_video_title);

        }
    }


    @Override
    public VideoViewUserUploadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_video_view_user_upload, parent, false);

        return new VideoViewUserUploadAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoViewUserUploadAdapter.MyViewHolder holder, final int position) {
        Integer pos = position + 1;
        holder.tvId.setText("#" + pos);

        holder.tvType.setText(listGif.get(position).getCat_type());
        holder.heart_button.setChecked(false);
        holder.tvGifFavCounter.setText(listGif.get(position).getFav_counter());
        holder.tvVideoTitle.setText(listGif.get(position).getTitle());


        try {

            Uri video = Uri.parse(listGif.get(position).getUrl());
            holder.vvGif.setMediaController(null);
            holder.vvGif.setVideoURI(video);
            holder.vvGif.start();
            holder.vvGif.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(0, 0);
                }
            });

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        if (String.valueOf(listGif.get(position).getApproved()).equals("0")) {
            Log.d("result", "" + String.valueOf(listGif.get(position).getApproved()).equals("0"));
            holder.tvApprovalStatus.setText("Not Approved");
        } else {
            holder.tvApprovalStatus.setText("Approved");
        }
//        holder.tvApprovalStatus.setText("Approval Status:-" + listGif.get(position).getApproved());
//
//
        try {
            Log.d("Reading: ", "Reading all contacts..");
            List<CategoryModel> allGif = db.getAllVideo();

            for (CategoryModel dm : allGif) {
                if (listGif.get(position).getId().equals(dm.getId())) {
                    Log.d("RESULT", "EQUAL==>" + listGif.get(position).getId().equals(dm.getId()));
                    holder.heart_button.setChecked(true);
                }
            }


        } catch (SQLiteException e) {
            Log.d("result", "Error message==>" + e.toString());
        }
        holder.heart_button.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    favCounter = Integer.parseInt(holder.tvGifFavCounter.getText().toString().trim());
                    Log.d("result", "Fav Text ==>" + favCounter);
                    favCounter++;
                    Log.d("result", "Counter==>" + favCounter);

                    holder.tvGifFavCounter.setText(String.valueOf(favCounter));

                    Log.d("Insert: ", "Inserting ..");

                    id = listGif.get(position).getId();
                    String DP_TYPE = listGif.get(position).getCat_type();
                    String DP = listGif.get(position).getUrl();
                    Log.d("Result;-->", "DP URI:--" + DP);
                    String log = " Id: " + id + " ,DP_TYPE: " + DP_TYPE +
                            " ,FAV_COUNTER: " + favCounter;

                    Log.d("Result: ", log);

//                    db.addVideo(new CategoryModel(id, DP_TYPE, DP, String.valueOf(favCounter)));
//                    Functions.updateGifCounter(activity, Config.KEY_ID, String.valueOf(id), Config.KEY_DP_FAV_COUNTER, String.valueOf(favCounter)
//                            , Config.URL_GIF_FAV_MASTER);
                } else {
                    Log.d("Result", "Button Boolean==>" + buttonState);
                    favCounter = Integer.parseInt(holder.tvGifFavCounter.getText().toString().trim());
                    Log.d("result", "Fav Text ==>" + favCounter);
                    favCounter--;
                    Log.d("result", "Counter==>" + favCounter);
                    holder.tvGifFavCounter.setText(String.valueOf(favCounter));

                    if (favCounter < 0) {
                        holder.tvGifFavCounter.setText("0");
                    }

                    id = listGif.get(position).getId();

//                    Functions.updateStatusCounter(activity, Config.KEY_ID, String.valueOf(id), Config.KEY_DP_FAV_COUNTER,
//                            String.valueOf(favCounter), Config.URL_GIF_FAV_MASTER);
//                    db.deletegif(new CategoryModel(id));


                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

    }
}

