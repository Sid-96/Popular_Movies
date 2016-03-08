package com.example.android.popularmovies;

/**
 * Created by Sid on 13-Feb-16.
 */
public class Movies {
    String movieName;
    String image;
    String overview;
    String releaseDate;
    String rating;
    String popularity;

    public Movies(){

    }

    public Movies(String movieName, String image){
        this.movieName = movieName;
        this.image = image;
    }

    public void setMovieName(String movieName){
        this.movieName = movieName;
    }

    public String getMovieName(){
        return movieName;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public void setOverview(String overview){ this.overview = overview; }

    public String getOverview(){ return overview; }

    public void setRating(String rating){ this.rating = rating; }

    public String getRating(){ return rating; }

    public void setReleaseDate(String releaseDate){ this.releaseDate = releaseDate; }

    public String getReleaseDate(){ return releaseDate; }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

}
