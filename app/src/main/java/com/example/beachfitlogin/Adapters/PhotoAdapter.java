package com.example.beachfitlogin.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;

public class PhotoAdapter extends ArrayAdapter {
    Context context;
    List<String> data = new ArrayList<String>();
    int resourceID;
    LayoutInflater inflater;

    public PhotoAdapter(Context context, int resourceID, List<String> data)
    {
        super(context, resourceID, data);
        this.resourceID = resourceID;
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }
    static class ViewHolder{ ImageView imageView;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null)
        {
            row = inflater.inflate(resourceID, parent, false);
            holder = new ViewHolder();
            //holder.imageView = (ImageView)row.findViewById(R.id.im);
            row.setTag(holder);
        } else
        {
            holder = (ViewHolder) row.getTag();
        }
        Glide.with(context).load("file://" + data.get(position)).fitCenter()
                .centerCrop().into(holder.imageView);
        return row;
    }
}
