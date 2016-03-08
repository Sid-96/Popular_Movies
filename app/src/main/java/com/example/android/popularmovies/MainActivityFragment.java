package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private static final String APIKEY = "YOUR API KEY HERE";
    private String urlString;
    Uri.Builder builder = new Uri.Builder();
    Uri.Builder builderl;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movies> mMoviesData;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        builderl = new Uri.Builder();
        if(id==R.id.refresh){
            new fetchMoviesTask().execute(urlString);
            mProgressBar.setVisibility(View.VISIBLE);
            return true;
        }
        if(id==R.id.sortByRating){
            builderl.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by","vote_average.desc")
                    .appendQueryParameter("api_key",APIKEY);
            urlString = builderl.build().toString();
            new fetchMoviesTask().execute(urlString);
            mProgressBar.setVisibility(View.VISIBLE);
            return true;
        }

        if(id==R.id.sortByPopularity){
            builderl.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by","popularity.desc")
                    .appendQueryParameter("api_key",APIKEY);
            urlString = builderl.build().toString();
            new fetchMoviesTask().execute(urlString);
            mProgressBar.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by","popularity.desc")
                .appendQueryParameter("api_key",APIKEY);
        urlString = builder.build().toString();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.movies_grid);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mMoviesData = new ArrayList<Movies>();
        mMovieAdapter = new MovieAdapter(getActivity(),R.layout.movies_item, mMoviesData);
        mGridView.setAdapter(mMovieAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movies movies = (Movies) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("title", movies.getMovieName());
                intent.putExtra("poster_path", movies.getImage());
                intent.putExtra("overview", movies.getOverview());
                intent.putExtra("rating", movies.getRating());
                intent.putExtra("release_date", movies.getReleaseDate());

                startActivity(intent);
            }
        });
        new fetchMoviesTask().execute(urlString);
        mProgressBar.setVisibility(View.VISIBLE);
        return rootView;
    }


    public class fetchMoviesTask extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            StringBuffer buffer = null;
            String movieJsonStr = null;
            BufferedReader reader = null;
            InputStream inputStream = null;
            int success = 0;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //Read inputStream into a string
                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();
                if(inputStream==null)
                    return success;
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=reader.readLine())!=null){
                    buffer.append(line + "\n");
                }
                if(buffer.length()==0)
                    return success;
                movieJsonStr = buffer.toString();
                parseResult(movieJsonStr);
                success = 1;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return success;
        }

        @Override
        protected void onPostExecute(Integer success) {
            if(success==1){
                if(mMoviesData!=null){
                    mMovieAdapter.setMovies(mMoviesData);
                }

            }
            else {
                Toast.makeText(getActivity(),"Failed to fetch data!",Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void parseResult(String movieJsonStr) {
        try {
            mMoviesData.clear();
            JSONObject root = new JSONObject(movieJsonStr);
            JSONArray results = root.getJSONArray("results");
            String movieImageBaseUrl = "http://image.tmdb.org/t/p/w342";
            for(int i=0 ; i<results.length() ; i++){
                Movies movies_item = new Movies();
                JSONObject result = results.getJSONObject(i);
                String movieTitle = result.getString("title");
                String movieImageLocalPath = result.getString("poster_path");
                String movieImage = movieImageBaseUrl+movieImageLocalPath;
                String overview = result.getString("overview");
                String rating = result.getString("vote_average");
                String release = result.getString("release_date");
                movies_item.setImage(movieImage);
                movies_item.setMovieName(movieTitle);
                movies_item.setOverview(overview);
                movies_item.setRating(rating);
                movies_item.setReleaseDate(release);
                mMoviesData.add(movies_item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}