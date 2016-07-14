package com.example.max.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


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
         * MY API KEY: b3c6e37e7ffe2d3c2f3e379a9858b159
         */
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


}
