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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.skylix.videostatus.ModelClass.CategoryModel;

import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.DatabaseHandler;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;


/**
 * Created by NP on 25-Feb-16.
 */
public class VideoFavouriteMasterAdapter extends RecyclerView.Adapter<VideoFavouriteMasterAdapter.MyViewHolder> {


    List<CategoryModel> listGif;
    Activity activity;
    private DatabaseHandler db;
    private int id, favCounter;
    MediaController mediaController;

    public VideoFavouriteMasterAdapter(Activity activity, List<CategoryModel> listGif) {
        this.activity = activity;
        this.listGif = listGif;
        db = new DatabaseHandler(activity);
        mediaController = new MediaController(activity);
    }

    @Override
    public int getItemCount() {
        return listGif.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvGifId, tvGifType, tvGifFavCounter;
        SparkButton heart_button;
        public VideoView vvGif;
        View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;

            vvGif = (VideoView) view.findViewById(R.id.row_gif);
            tvGifId = (TextView) view.findViewById(R.id.rowVideoId);
            tvGifType = (TextView) view.findViewById(R.id.rowVideoType);
            tvGifFavCounter = (TextView) view.findViewById(R.id.favCounter);
            heart_button = (SparkButton) view.findViewById(R.id.heart_button);
            vvGif = (VideoView) view.findViewById(R.id.rowVideo);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_video_favourite_master, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        String FavMasterLog = listGif.get(position).getFav_id() + "--" +
                listGif.get(position).getId() + "--" +
                listGif.get(position).getTitle() + "--" +
                listGif.get(position).getCat_type() + "--" +
                listGif.get(position).getCat_alias() + "--" +
                listGif.get(position).getUrl() + "--" +
                listGif.get(position).getFav_counter() + "--" +
                listGif.get(position).getLikes() + "--" +
                listGif.get(position).getDislikes();
        Log.d("Database", " FavMasterAdapterLog==>" + FavMasterLog);


        Integer counter = position + 1;

        holder.heart_button.setChecked(false);
        try {
            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = holder.vvGif.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.vvGif.setLayoutParams(params);
//            mediaController.show();
            Uri video = Uri.parse(listGif.get(position).getUrl());
            Log.d("GifURL", "Adapter Url:--" + listGif.get(position).getUrl());
            holder.vvGif.setMediaController(null);
            holder.vvGif.setVideoURI(video);
            holder.vvGif.seekTo(100);
            holder.vvGif.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(100, 100);
                    mediaPlayer.setLooping(true);
                }
            });
            holder.vvGif.start();
//            String VideoURL = "http://192.168.1.143/AndroidImageUpload/dpandstatus/gif/1.mp4";
//            Uri video = Uri.parse(listGif.get(position).getUrl());
//            holder.vvGif.setMediaController(null);
//            holder.vvGif.setVideoURI(video);
//            holder.vvGif.start();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        try {
            Log.d("Reading: ", "Reading all contacts..");
            List<CategoryModel> data = db.getAllVideo();

            for (CategoryModel statusModel : data) {

                if (listGif.get(position).getId().equals(statusModel.getId())) {
                    Log.d("Result", "Equals==>" + listGif.get(position).getId() + "==>" + statusModel.getId());
                    holder.heart_button.setChecked(true);
                }
            }
        } catch (SQLiteException e) {
            Log.d("result", "Error message==>" + e.toString());
        }
        holder.tvGifId.setText("#" + String.valueOf(counter));

        holder.tvGifType.setText(listGif.get(position).getCat_type());
        holder.tvGifFavCounter.setText(listGif.get(position).getFav_counter());

//        holder.tvGifType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, Gifdetail.class);
//                intent.putExtra(Config.KEY_INTENT_GIF, Config.GlobalHashMap);
//                Log.d("Result", "GIFMASTER" + Config.GlobalHashMap);
//                activity.startActivity(intent);
//
//            }
//        });

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

//                    db.addfavourite(new CategoryModel(id, DP_TYPE, DP, String.valueOf(favCounter)));
//                    new UpdateGif().execute();
                } else {
                    Log.d("Result", "Button Boolean==>" + buttonState);
                    favCounter = Integer.parseInt(holder.tvGifFavCounter.getText().toString().trim());
                    Log.d("result", "Fav Text ==>" + favCounter);
                    favCounter--;
                    Log.d("result", "Counter==>" + favCounter);

                    holder.tvGifFavCounter.setText(String.valueOf(favCounter));
                    id = listGif.get(position).getId();

//                    new UpdateGif().execute();
                    db.deleteVideo(new CategoryModel(id));


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

