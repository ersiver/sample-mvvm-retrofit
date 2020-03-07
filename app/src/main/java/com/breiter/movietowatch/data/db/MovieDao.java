package com.breiter.movietowatch.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.breiter.movietowatch.data.entity.Movie;
import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Update
    void updateMovie(Movie movie);

    @Query("SELECT * FROM movie_table")
    LiveData<List<Movie>> getAllMovies();


    @Query("SELECT * FROM movie_table WHERE id = :id")
    LiveData<Movie> getMovieById(int id);
}
