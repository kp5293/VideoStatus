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

public class CategoryMainAdapter extends RecyclerView.Adapter<CategoryMainAdapter.MyViewHolder> {


    List<CategoryModel> listGif;
    private Activity activity;
    String input;

    public CategoryMainAdapter(Activity activity, List<CategoryModel> listGif) {
        this.activity = activity;
        this.listGif = listGif;
    }

    @Override
    public int getItemCount() {
        return listGif.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCategoryIcon;
        private TextView ivName;
        private View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;

            ivCategoryIcon = (ImageView) view.findViewById(R.id.ivCategoryIcon);
            ivName = (TextView) view.findViewById(R.id.tvName);


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_fragment_cat_main, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.ivName.setText(listGif.get(position).getCat_type());

        Glide.with(activity)
                .load(listGif.get(position).getUrl())
                .placeholder(R.drawable.gificon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivCategoryIcon);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RESULT", "GIF SUB CAT MAIN ADAPTER:----" + position);
                Log.d("Result", "intentDp:---" + listGif.get(position).getCat_type());

                String input = listGif.get(position).getCat_alias();
                input = input.replace(" ", "").toLowerCase();
                Log.d("RESULT", "DEMO:----" + input);

                Intent intent = new Intent(activity, CategoryMaster.class);
                intent.putExtra(Config.KEY_INTENT_VIDEO, input); // converting model json into string type and sending it via intent
                activity.startActivity(intent);

            }
        });
        input = listGif.get(position).getCat_type();
        createAlias();
//        Functions.countSubCategoryrow(holder.tvCount, Config.KEY_GIF_TYPE, Config.COUNT_GIF_SUB_CAT_URL + input,"gifs");

    }

    private String createAlias() {

        input = input.replace(" ", "").toLowerCase();
        Log.d("RESULT", "DEMO:----" + input);
        return input;
    }
}

