package com.breiter.movietowatch.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.breiter.movietowatch.R;
import com.breiter.movietowatch.data.model.Movie;
import com.breiter.movietowatch.ui.adapter.MovieAdapter;
import com.breiter.movietowatch.ui.util.ItemSwipeHelper;
import com.breiter.movietowatch.ui.viewmodel.MovieViewModel;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;
    private ImageView searchImageView;
    private MovieAdapter movieAdapter;
    private RecyclerView savedMoviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        searchImageView = findViewById(R.id.searchImageView);
        savedMoviesRecyclerView = findViewById(R.id.searchedMoviesRecyclerView);

        setupRecyclerView();         //1
        showSavedMovieList();        //2
        clickToShowMovieDetails();   //3
        swipeToDeleteMovie();        //4
        searchMovie(searchImageView);//5
    }

    //1
    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter();
        savedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedMoviesRecyclerView.setHasFixedSize(true);
        savedMoviesRecyclerView.setAdapter(movieAdapter);
    }

    //2
    private void showSavedMovieList() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getSavedMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter.setMovies(movies);
            }
        });
    }

    //3
    private void clickToShowMovieDetails() {
        movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                goToMovieDetailsActivity(movie); //3a
            }
        });
    }

    //3a
    private void goToMovieDetailsActivity(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(movie);
        intent.putExtra("EXTRA_MOVIE", myJson);
        startActivity(intent);
    }

    //4
    private void swipeToDeleteMovie(){
        ItemSwipeHelper itemSwipeHelper = new ItemSwipeHelper(this, savedMoviesRecyclerView) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton(
                        getApplicationContext(),
                        R.drawable.ic_delete,
                        Color.parseColor("#FF3C30"), //red
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                movieViewModel.deleteMovie(movieAdapter.getMovieAt(pos));
                            }
                        }
                ));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemSwipeHelper);
        itemTouchHelper.attachToRecyclerView(savedMoviesRecyclerView);
    }

    //5
    private void searchMovie(ImageView searchImageView) {
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchMovieActivity(); //5a
            }
        });
    }

    //5a
    private void goToSearchMovieActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
