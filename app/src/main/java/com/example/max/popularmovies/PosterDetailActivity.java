package com.example.max.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class PosterDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_detail);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentPanel, new PosterDetailFragment())
                    .commit();
        }
    }

    /**
     * TODO
     * Get the json data into the text fields
     *
     * Get the poster image using the url and pascal and put into the imageview
     *
     * Make sure the imageView is in the right position
     */
    public static class PosterDetailFragment extends Fragment {
        private static final String LOG_TAG = PosterFragment.class.getSimpleName();

        private String movieJsonString;
        private String posterImageURL;

        public PosterDetailFragment(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_poster_detail,container,false);


            Intent intent = getActivity().getIntent();
            if(intent != null && intent.hasExtra("movieInfo") && intent.hasExtra("posterImag")){
                movieJsonString = intent.getStringExtra("movieInfo");
                posterImageURL = intent.getStringExtra("posterImageURL");
            }else{
                Toast.makeText(getActivity(),"Error getting info, please go back",Toast.LENGTH_LONG).show();
            }

            return rootView;
        }
    }
}
