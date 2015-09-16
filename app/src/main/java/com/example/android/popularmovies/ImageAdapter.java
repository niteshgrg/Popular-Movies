package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Results;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final LayoutInflater mInflater;
    String BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    ArrayList<Results> images;

    public ImageAdapter(Context c, ArrayList<Results> temp) {
        super();
        mContext = c;
        images = temp;
        mInflater = LayoutInflater.from(mContext);

    }

    public void updateContent(ArrayList<Results> temp)
    {
        this.images.clear();
        this.images = temp;
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return images.size();

    }


    public String getItem(int position) {

        return images.get(position).getPoster_path();
    }

    public long getItemId(int position) {

        return 0;
    }

    public static class ViewHolder {
        public ImageView imageView;
    }
    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        View vi = convertView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            vi = mInflater.inflate(R.layout.grid_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) vi.findViewById(R.id.picture);
            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vi.getTag();
        }

        String url = BASE_URL + getItem(position);
        Log.d(LOG_TAG, url);

        Picasso.with(mContext).load(url).fit().into(holder.imageView);
        return vi;
    }
}