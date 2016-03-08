package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private TextView titleTextView;
    private ImageView imageView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private TextView overviewTextView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        String title = getActivity().getIntent().getStringExtra("title");
        String poster_path = getActivity().getIntent().getStringExtra("poster_path");
        String rating = getActivity().getIntent().getStringExtra("rating");
        String overview = getActivity().getIntent().getStringExtra("overview");
        String releaseDate = getActivity().getIntent().getStringExtra("release_date");

        titleTextView = (TextView) rootView.findViewById(R.id.detail_title);
        imageView = (ImageView) rootView.findViewById(R.id.detail_poster);
        releaseDateTextView = (TextView) rootView.findViewById(R.id.detail_date_textView);
        ratingTextView = (TextView) rootView.findViewById(R.id.detail_rating_textView);
        overviewTextView = (TextView) rootView.findViewById(R.id.detail_overview_textView);

        titleTextView.setText(title);
        Picasso.with(getActivity().getApplicationContext()).load(poster_path).resize(700,780).into(imageView);
        ratingTextView.setText("Rating: " + rating);
        releaseDateTextView.setText("Release Date: " + releaseDate);
        overviewTextView.setText("Overview \n" + overview);
        return rootView;
    }

}
