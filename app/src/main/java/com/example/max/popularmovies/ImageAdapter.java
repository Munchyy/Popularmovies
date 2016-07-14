package com.example.max.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Custom adapter class for images in gridview
 */
public class ImageAdapter extends ArrayAdapter{

    private final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> imageUrls;
    private String[] strArrayImageUrls;

    public ImageAdapter(Context context, ArrayList<String> imageUrls) {
        super(context, R.layout.poster_image, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;
        strArrayImageUrls = this.imageUrls.toArray(new String[0]);
        inflater = LayoutInflater.from(this.context);
    }

    /**
     * @param newUrls
     *
     * Replaces the poster set with a new set of URL's
     * Clears the adapter
     * Adds the new set
     * Notifies of changed data
     */
    public void changeUrls(ArrayList<String> newUrls){
        this.imageUrls = newUrls;
        this.strArrayImageUrls = this.imageUrls.toArray(new String[0]);
        this.clear();
        this.addAll(this.imageUrls);
        //this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.poster_image,parent,false);
        }

        if(context!=null && strArrayImageUrls!=null){


            Picasso
                    .with(context)
                    .load(strArrayImageUrls[position])
                    .fit()
                    .into((ImageView) convertView);

            return convertView;
        }else{
            Log.e(LOG_TAG,"Something is null m8");
        }
        return null;
    }

}
