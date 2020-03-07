package com.breiter.movietowatch.data.net;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/";
    private static final String API_KEY = "xxx xxx xxx xxx xxx xxx"; //Enter your API_KEY
    private static final int PAGE_LIMIT = 1;

    private MoviesAPI moviesAPI;
    private Retrofit retrofit;

    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        moviesAPI = retrofit.create(MoviesAPI.class);
    }

    public Call<MovieResponse> searchMovies(String query) {
        return (moviesAPI.searchMovieByTitle(API_KEY, query, PAGE_LIMIT));
    }
}

