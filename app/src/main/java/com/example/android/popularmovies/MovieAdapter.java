package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sid on 13-Feb-16.
 */

public class MovieAdapter extends ArrayAdapter<Movies>{

    private Context mContext;
    private int layoutResourceId;
    private List<Movies> movies = new ArrayList<Movies>();

    /**
     * @param mContext  The current context, used to inflate layout file
     * @param movies    The list of the Movies object to be displayed in GridView
     */
    public MovieAdapter(Context mContext,int layoutResourceId, List<Movies> movies){
        super(mContext,layoutResourceId,movies);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.movies = movies;
    }

    /**
     * Sets the movies arraylist.
     * @param movies
     */
    public void setMovies(ArrayList<Movies> movies){

        this.movies = movies;
        notifyDataSetChanged();
    }

    /**
     *
     * @param position  The AdapterView(GridView) position
     * @param convertView The view to be inflated
     * @param parent The parent ViewGroup used for inflation
     * @return  the view for position in AdapterView
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId,parent,false);
            holder = new ViewHolder();
            holder.movieImageView = (ImageView) convertView.findViewById(R.id.movie_image);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Movies mMovie = movies.get(position);
        Picasso.with(mContext).load(mMovie.getImage()).into(holder.movieImageView);
        return convertView;
    }

    static class ViewHolder{
        ImageView movieImageView;
    }
}
