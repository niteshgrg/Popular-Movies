package com.example.android.popularmovies;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by nitesh on 9/20/15.
 */
public class GridViewAdapter extends BaseAdapter {

    Activity context;
    ArrayList<Bitmap> gridViewItems;
    private final LayoutInflater mInflater;

    public GridViewAdapter (Activity context, ArrayList<Bitmap> gridViewItems)
    {
        this.context = context;
        this.gridViewItems = gridViewItems;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
          return gridViewItems.size();
    }

    public Object getItem (int position)
    {
        return null;
    }

    public long getItemId (int position)
    {
        return 0;
    }

    public View getView (int position, View convertView, ViewGroup parent)
    {

        View view = convertView;
        if (view == null)
            view = mInflater.inflate(R.layout.grid_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.picture);
        imageView.setImageBitmap(gridViewItems.get(position));
        return view;

    }
}
