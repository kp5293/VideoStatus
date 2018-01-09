package com.skylix.videostatus.Gif;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skylix.videostatus.ModelClass.CategoryModel;
import com.skylix.videostatus.R;

import java.util.List;


/**
 * Created by Destiny on 11-Aug-17.
 */

public class CategorySpinnerAdapter extends BaseAdapter {

    Activity a;
    List<CategoryModel> listGif;


    public CategorySpinnerAdapter(Activity a, List<CategoryModel> listGif) {
        this.a = a;
        this.listGif = listGif;

    }

    @Override
    public int getCount() {
        return listGif.size();
    }

    @Override
    public Object getItem(int position) {
        return listGif.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = a.getLayoutInflater();
        viewholder viewHolder = new viewholder();
        if (convertView == null) {


            convertView = inflater.inflate(R.layout.row_spinner_layout, parent, false);
            viewHolder.id_text = (TextView) convertView.findViewById(R.id.id);
            viewHolder.name_txt = (TextView) convertView.findViewById(R.id.txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (viewholder) convertView.getTag();
        }

            viewHolder.id_text.setText(listGif.get(position).getCat_id().toString());
            viewHolder.name_txt.setText(listGif.get(position).getCat_type());

        return convertView;
    }

    private class viewholder {
        private TextView name_txt;
        private TextView id_text;
    }
}