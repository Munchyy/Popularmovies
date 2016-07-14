package com.example.max.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Custom adapter class for images in gridview
 */
public class ImageAdapter extends ArrayAdapter{

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private String[] imageUrls;

    public ImageAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.poster_image, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.poster_image,parent,false);
        }

        if(context!=null && imageUrls!=null){
            Picasso
                    .with(context)
                    .load(imageUrls[position])
                    .fit()
                    .into((ImageView) convertView);

            return convertView;
        }else{
            Log.e(LOG_TAG,"Something is null m8");
        }
        return null;
    }
}
