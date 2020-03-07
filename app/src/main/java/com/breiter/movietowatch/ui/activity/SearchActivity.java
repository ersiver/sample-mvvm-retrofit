package com.breiter.movietowatch.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.breiter.movietowatch.R;
import com.breiter.movietowatch.data.entity.Movie;
import com.breiter.movietowatch.ui.adapter.MovieAdapter;
import com.breiter.movietowatch.ui.viewmodel.MovieViewModel;
import com.google.gson.Gson;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private MovieViewModel movieViewModel;
    private ImageView searchImageView;
    private ImageView backImageView;
    private ImageView clearTextImageView;
    private MovieAdapter movieAdapter;
    private RecyclerView searchedMoviesRecyclerView;
    private EditText queryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        bindViews();                          //1
        displayListWhileTyping(queryEditText);//2
        clickToSearchMovie(searchImageView);  //3
        clickToShowMovieDetails();            //4
        clickToClearText(clearTextImageView); //5
        clickToGoBack(backImageView);         //6
    }

    //1
    private void bindViews() {
        searchImageView = findViewById(R.id.searchImageView);
        backImageView = findViewById(R.id.backImageView);
        clearTextImageView = findViewById(R.id.clearTextImageView);
        searchedMoviesRecyclerView = findViewById(R.id.searchedMoviesRecyclerView);
        queryEditText = findViewById(R.id.queryEditText);

        movieAdapter = new MovieAdapter();
        searchedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchedMoviesRecyclerView.setHasFixedSize(true);
        searchedMoviesRecyclerView.setAdapter(movieAdapter);
    }

    //2
    private void displayListWhileTyping(EditText queryEditText) {
        queryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 3)
                    getMovieList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //3
    private void clickToSearchMovie(ImageView searchImageView) {
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = queryEditText.getText().toString().trim();
                if (!query.isEmpty())
                    getMovieList(query); //3b
                else
                    Toast.makeText(SearchActivity.this, "Enter title", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //3b
    private void getMovieList(String query) {
        movieViewModel.searchMovie(query).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter.setMovies(movies);
            }
        });
    }

    //4
    private void clickToShowMovieDetails() {
        movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                goToMovieDetailsActivity(movie); //4a
            }
        });
    }

    //4a
    private void goToMovieDetailsActivity(final Movie movie) {
        final Intent intent = new Intent(this, MovieDetailsActivity.class);
        final Gson gson = new Gson();
        final String[] myJson = new String[1];

        movieViewModel.getMovieById(movie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie aMovie) {

                if (aMovie != null)
                    myJson[0] = gson.toJson(aMovie);
                else
                    myJson[0] = gson.toJson(movie);

                intent.putExtra("EXTRA_MOVIE", myJson[0]);
                intent.putExtra("EXTRA_ACTIVITY", "SEARCH_ACTIVITY");
                startActivity(intent);
            }
        });
    }

    //5
    private void clickToClearText(ImageView clearTextImageView) {
        clearTextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryEditText.setText("");
                movieAdapter.clearMovieList();
            }
        });
    }

    //6
    private void clickToGoBack(ImageView backImageView) {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity(); //6a
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity(); //6a
    }

    //6a
    private void goToMainActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
