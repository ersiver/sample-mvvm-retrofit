package com.breiter.movietowatch.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.breiter.movietowatch.data.db.MovieDao;
import com.breiter.movietowatch.data.db.MovieDatabase;
import com.breiter.movietowatch.data.model.Movie;
import com.breiter.movietowatch.data.model.MovieResponse;
import com.breiter.movietowatch.data.net.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private MovieDao movieDao;
    private RetrofitClient retrofitClient;

    public MovieRepository(Application application) {
        MovieDatabase movieDatabase = MovieDatabase.getInstance(application);
        movieDao = movieDatabase.movieDao();
        retrofitClient = new RetrofitClient();
    }

    //Get Movies by query
    public LiveData<List<Movie>> searchMovie(String query) {
        final MutableLiveData<List<Movie>> mMovieList = new MutableLiveData();

        Call<MovieResponse> call = retrofitClient.searchMovies(query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();

                    if (movieResponse != null)
                        mMovieList.setValue(movieResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {

            }
        });
        return mMovieList;
 }

    //Get movie by ID
    public LiveData<Movie> getMovieById(int id) {
        return movieDao.getMovieById(id);
    }

    //Get saved movies
    public LiveData<List<Movie>> getSavedMovies() {
        return movieDao.getAllMovies();
    }

    //Save movie
    public void saveMovie(Movie movie) {
        new SaveMovieAsyncTask(movieDao).execute(movie); //1
    }

    //Delete movie
    public void deleteMovie(Movie movie) {
        new DeleteMovieAsyncTask(movieDao).execute(movie); //2
    }

    //Update movie
    public void updateMovie(Movie movie) {
        new UpdateMovieAsyncTask(movieDao).execute(movie); //3
    }


    //1
    private static class SaveMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

         SaveMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insertMovie(movies[0]);
            return null;
        }
    }

    //2
    private static class DeleteMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

         DeleteMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.deleteMovie(movies[0]);
            return null;
        }
    }

    //3
    private static class UpdateMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

         UpdateMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.updateMovie(movies[0]);
            return null;
        }
    }
}

