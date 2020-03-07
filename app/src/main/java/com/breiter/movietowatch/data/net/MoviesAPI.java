package com.breiter.movietowatch.data.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MoviesAPI {

    @GET("search/movie")
    Call<MovieResponse> searchMovieByTitle(@Query("api_key") String api_key,
                                           @Query("query") String query,
                                           @Query("page") Integer pageLimit);

}
