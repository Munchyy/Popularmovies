package com.example.max.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;


public class PosterFragment extends Fragment {

    private ImageAdapter mPosterAdapter;
    private final String LOG_TAG = PosterFragment.class.getSimpleName();

    private String fullJsonString = null;

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
         * PROJECT INFO: https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.cntdg23jy69n
         */

        /**
         * 1) Get a query for movie information
         * 2) Carry out the http shit in an AsyncTask
         * 3) Parse the json to find the poster urls
         *  EXTENSION    3.1) Store meta data about the movie for clicking on the poster?
         * 4) Send a String[] of the poster full urls to the adapter, which with use picasso
         *      to get the images and will then set them into the gridview
         */

        mPosterAdapter = new ImageAdapter(getActivity(), new ArrayList<String>());
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_posters);
        gridView.setAdapter(mPosterAdapter);

        //Values for query
        String sortValue = "popularity.desc";
        int pageNumber = 1;
        //Make instance of FetchMovieListTask, set it up and execute
        FetchMovieListTask fetchMovieListTask = new FetchMovieListTask();
        fetchMovieListTask.execute(sortValue, Integer.toString(pageNumber));

        //Add the on click listener to find what was clicked
        //for now uses a Toast to show what was clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),""+position,Toast.LENGTH_SHORT).show();


                Log.d(LOG_TAG,(String) mPosterAdapter.getItem(position));
                String posterImageUrl = (String) mPosterAdapter.getItem(position);

                Intent intent = new Intent(getActivity(),PosterDetailActivity.class)
                    .putExtra("movieInfo", jsonMovieInfo(fullJsonString, position))
                        .putExtra("posterImageURL",posterImageUrl);
                startActivity(intent);

            }
        });

        return rootView;
    }
    public String jsonMovieInfo(String jsonString, int index){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject movieObject = jsonObject.getJSONArray("results").getJSONObject(index);
            return movieObject.toString();
        }catch(JSONException e){
            Log.e(LOG_TAG, "Error parsing JSON for intent",e);
            return null;
        }
    }



    public class FetchMovieListTask extends AsyncTask<String, Void, ArrayList<String>>{

        private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();
        private String jsonString;

        public String getJsonString() {
            return jsonString;
        }

        /**
         * @param params takes the string of the value to sort by e.g. most popular descending, and the page count for the posters
         * @return returns the arraylist of urls
         */
        @Override
        protected ArrayList<String> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            if (params.length == 2){
                try {

                    URL queryUrl = new URL(getMovieListUri(params[0], params[1]));

                    //open the connection and connect with http get
                    urlConnection = (HttpURLConnection) queryUrl.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    //Read input into a string
                    InputStream iStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (iStream==null)
                        return null;
                    reader = new BufferedReader(new InputStreamReader(iStream));

                    String line;
                    while ((line = reader.readLine()) != null){
                        //append new line for readability. does not affect parsing
                        buffer.append(line + "\n");
                    }

                    //if steam was empty
                    if (buffer.length() == 0)
                        return null;

                    jsonString = buffer.toString();

                    try{
                        return getPosterUrlsFromJson(jsonString);
                    }catch(JSONException e){
                        Log.e(LOG_TAG,"Error parsing JSON",e);
                        return null;
                    }
                }catch(IOException e){
                    Log.e(LOG_TAG, "Error ",e);

                    return null;
                }finally{
                    if(urlConnection!=null){
                        urlConnection.disconnect();
                    }

                    if(reader != null)
                        try{
                            reader.close();
                        }catch(final IOException e){
                            Log.e(LOG_TAG, "Error closing stream",e);
                        }
                }
            }
            else{
                Log.e(LOG_TAG, "There were not 2 parameters for the url");
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            /**
             * TODO
             *
             * Call the ImageAdapter with the string[] containing the urls
             */
            mPosterAdapter.changeUrls(result);
            fullJsonString = jsonString;

        }
        /**Get JSON Object, then the array of results.
         * Go through each result and find the poster path, adding each to an ArrayList
         * Iterate through the array and call the getFullPosterUrl method
         */
        public ArrayList<String> getPosterUrlsFromJson(String jsonString) throws JSONException{


            ArrayList<String> urlsArray = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            for(int i=0; i < resultsArray.length(); i++){
                urlsArray.add(resultsArray.optJSONObject(i).getString("poster_path"));
            }

            ListIterator<String> iterator = urlsArray.listIterator();
            while(iterator.hasNext()){
                String relUrl = iterator.next();
                iterator.set(getFullPosterUrl(relUrl));
            }


            return urlsArray;
        }

        //takes the section of the url from json and adds it to the base url
        public String getFullPosterUrl(String relativeURL){
            String fullUrl;
            final String BASE_URL= "http://image.tmdb.org/t/p/";
            final String IMAGE_SIZE = "w185/";

            fullUrl = BASE_URL + IMAGE_SIZE + relativeURL;
            return fullUrl;
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

    }

}
