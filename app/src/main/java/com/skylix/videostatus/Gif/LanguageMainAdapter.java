package com.skylix.videostatus.Gif;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skylix.videostatus.ModelClass.CategoryModel;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.Config;

import java.util.List;


/**
 * Created by Destiny on 12-Aug-17.
 */

public class LanguageMainAdapter extends RecyclerView.Adapter<LanguageMainAdapter.MyViewHolder> {


    List<CategoryModel> listGif;
    private Activity activity;
//    String input;

    public LanguageMainAdapter(Activity activity, List<CategoryModel> listGif) {
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
        private TextView ivName;
        private View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            ivName = (TextView) view.findViewById(R.id.tvLanguage);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_language_main, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.ivName.setText(listGif.get(position).getLanguage());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
               
                Intent intent = new Intent(activity, CategoryMaster.class);
                intent.putExtra(Config.KEY_INTENT_LANGUAGE, listGif.get(position).getLanguage()); // converting model json into string type and sending it via intent
                activity.startActivity(intent);

//
            }
        });

    }
}

