package com.skylix.videostatus.Gif;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.skylix.videostatus.ModelClass.CategoryModel;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.Config;
import com.skylix.videostatus.tools.DatabaseHandler;

import java.util.List;


/**
 * Created by Destiny on 10-Jul-17.
 */

public class CategoryMasterAdapter extends RecyclerView.Adapter<CategoryMasterAdapter.MyViewHolder> {


    List<CategoryModel> listGif;
    private Activity activity;
    private DatabaseHandler db;
    private int id, favCounter;

    public CategoryMasterAdapter(Activity activity, List<CategoryModel> listGif) {
        this.activity = activity;
        this.listGif = listGif;
        db = new DatabaseHandler(activity);
    }

    @Override
    public int getItemCount() {
        return listGif.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivCategoryIcon;
        public TextView  tvName;



        View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;

            ivCategoryIcon = (ImageView) view.findViewById(R.id.ivCategoryIcon);
            tvName = (TextView) view.findViewById(R.id.row_name);
        }
    }


    @Override
    public CategoryMasterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_video_master, parent, false);

        return new CategoryMasterAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryMasterAdapter.MyViewHolder holder, final int position) {
        Glide.with(activity)
                .load(listGif.get(position).getThumnail())
                .placeholder(R.drawable.gificon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivCategoryIcon);


        holder.tvName.setText(listGif.get(position).getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VideoDetail.class);
                intent.putExtra("intentCatAlias", String.valueOf(listGif.get(position).getCat_alias()));
                intent.putExtra(Config.KEY_INTENT_VIDEO, String.valueOf(listGif.get(position).getId()));
                Log.d("Result", "Category Master Adapter:---" + listGif.get(position).getCat_alias());
                activity.startActivity(intent);
            }
        });

    }
}

