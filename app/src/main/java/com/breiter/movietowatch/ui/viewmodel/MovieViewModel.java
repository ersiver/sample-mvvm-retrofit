package com.breiter.movietowatch.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.breiter.movietowatch.data.MovieRepository;
import com.breiter.movietowatch.data.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private MediatorLiveData<List<Movie>> movieList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        movieList = new MediatorLiveData<>();
        getAllMovies();
    }

    private void getAllMovies() {
        movieList.addSource(movieRepository.getSavedMovies(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieList.postValue(movies);
            }
        });
    }
    public LiveData<List<Movie>> getSavedMovies() {
        return movieList;
    }

    public void saveMovie(Movie movie) {
        movieRepository.saveMovie(movie);
    }

    public void deleteMovie(Movie movie) {
        movieRepository.deleteMovie(movie);
    }

    public void updateMovie(Movie movie) {
        movieRepository.updateMovie(movie);
    }

    public LiveData<List<Movie>> searchMovie(String query) {
        return movieRepository.searchMovie(query);
    }

    public LiveData<List<Movie>> getMovieById(int id) {
        return movieRepository.getMovieById(id);
    }
}
