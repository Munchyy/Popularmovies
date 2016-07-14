package com.example.max.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class PosterFragment extends Fragment {

    private ListAdapter mPosterAdapter;

    public PosterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_poster,container,false);

        /**
         * TODO: MAKE A URI BUILDER TO GET THE TMDB IMAGES AUTOMATICALLY
         * https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.cntdg23jy69n
         */

        /**
         * 1) Get a query for movie information
         * 2) Carry out the http shit in an AsyncTask
         * 3) Parse the json to find the poster urls
         *      3.1) Store meta data about the movie for clicking on the poster?
         * 4) Send a String[] of the poster full urls to the adapter, which with use picasso
         *      to get the images and will then set them into the gridview
         */


        //Construct URL for query
        String sortValue = "popularity.desc";
        int pageNumber = 1;



        //Example Posters
        String[] urls = {
                "https://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
                "https://image.tmdb.org/t/p/w185/inVq3FRqcYIRl2la8iZikYYxFNR.jpg",
                "https://image.tmdb.org/t/p/w185/9KQX22BeFzuNM66pBA6JbiaJ7Mi.jpg",
                "https://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "https://image.tmdb.org/t/p/w185/5N20rQURev5CNDcMjHVUZhpoCNC.jpg",
                "https://image.tmdb.org/t/p/w185/is6QqgiPQlI3Wmk0bovqUFKM56B.jpg",
                "https://image.tmdb.org/t/p/w185/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
                "https://image.tmdb.org/t/p/w185/tSFBh9Ayn5uiwbUK9HvD2lrRgaQ.jpg",
                "https://image.tmdb.org/t/p/w185/h28t2JNNGrZx0fIuAw8aHQFhIxR.jpg",
                "https://image.tmdb.org/t/p/w185/6FxOPJ9Ysilpq0IgkrMJ7PubFhq.jpg",
                "https://image.tmdb.org/t/p/w185/z09QAf8WbZncbitewNk6lKYMZsh.jpg",
                "https://image.tmdb.org/t/p/w185/cGOPbv9wA5gEejkUN892JrveARt.jpg",
        };
        mPosterAdapter = new ImageAdapter(getActivity(), urls);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_posters);
        gridView.setAdapter(mPosterAdapter);

        //Add the on click listener to find what was clicked
        //for now uses a Toast to show what was clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),""+position,Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }




    public class FetchMovieListTask extends AsyncTask<String, Void, String>{

        private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {

            try {
                if (params.length == 2){
                    URL queryUrl = new URL(getMovieListUri(params[0], params[1]));

                    /**
                     * TODO:
                     * Do the http connection and receive the json file
                     */

                }
                else{
                    Log.e(LOG_TAG, "There were not 2 parameters for the url");
                    return null;
                }

            }catch(MalformedURLException e){
                Log.e(LOG_TAG, "Bad URI");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result){

            /**
             * TODO:
             * Get the Poster URL's from the result jsonString
             *
             * Call the ImageAdapter with the string[] containing the urls
             */
        }

        public String getMovieListUri(String sortValue, String pageNumber){

            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            final String SORT_PARAM = "sort_by";
            final String PAGE_NUM_PARAM = "page";
            final String APPID_PARAM = "api_key";

            //gets a uri given the parameters passed into the method
            Uri queryUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sortValue)
                    .appendQueryParameter(PAGE_NUM_PARAM, pageNumber)
                    .appendQueryParameter(APPID_PARAM,BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            return queryUri.toString();
        }

        public String[] getPosterUriFromJson(String jsonString){

            /**
             * TODO:
             * Implement the code to fetch the urls for the posters from the json string
             */
            return null;
        }

    }

}
