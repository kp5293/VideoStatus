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

import java.util.List;


/**
 * Created by Destiny on 12-Aug-17.
 */

public class CategoryDetailLatestAdapter extends RecyclerView.Adapter<CategoryDetailLatestAdapter.MyViewHolder> {


    List<CategoryModel> listGif;
    private Activity activity;

    public CategoryDetailLatestAdapter(Activity activity, List<CategoryModel> listGif) {
        this.activity = activity;
        this.listGif = listGif;

    }

    @Override
    public int getItemCount() {
        return listGif.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivCategoryIcon;
        public TextView tvName;
        View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;

            ivCategoryIcon = (ImageView) view.findViewById(R.id.ivCategoryIcon);
            tvName = (TextView) view.findViewById(R.id.row_name);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category_detail_latest, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

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

                intent.putExtra(Config.KEY_INTENT_VIDEO, String.valueOf(listGif.get(position).getId()));
                intent.putExtra("intentCatAlias", String.valueOf(listGif.get(position).getCat_alias()));
                Log.d("Result", "CategoryDetailLatest:---" + listGif.get(position).getCat_alias() + "---"
                        + listGif.get(position).getId());
                activity.startActivity(intent);
                activity.finish();
            }
        });

//        if (listGif.get(position).getId() == Config.videoId) {
////            Log.d("Result", "Equal:---" + listGif.get(position).getId());
////            Log.d("Result", "Equal:---" + Config.videoId);
////            holder.mView.setVisibility(View.GONE);
////            listGif.remove(listGif.get(position).getId());
//
//            listGif.remove(position);
//            Toast.makeText(activity, "removed...", Toast.LENGTH_SHORT).show();
//
//        } else {
//            holder.mView.setVisibility(View.VISIBLE);
//            Log.d("Result", "Not Equal:---" + listGif.get(position).getId());
//            Log.d("Result", "Not Equal:---" + Config.videoId);
//            Toast.makeText(activity, "Not removed...", Toast.LENGTH_SHORT).show();
//        }
    }
}

