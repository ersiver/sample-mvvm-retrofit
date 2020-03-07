package com.breiter.movietowatch.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import com.breiter.movietowatch.R;
import com.breiter.movietowatch.data.entity.Movie;
import com.breiter.movietowatch.ui.viewmodel.MovieViewModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";
    private MovieViewModel movieViewModel;
    private Movie movie;
    private Intent intent;
    private TextView titleTextView;
    private TextView overviewTextView;
    private TextView ratingTextView;
    private TextView releasedTextView;
    private TextView languageTextView;
    private ImageView backImageView;
    private ImageView posterImageView;
    private ImageView addToSavedImageView;
    private ImageView deleteFromSavedImageView;
    private ImageView searchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        bindViews();                        //1
        getMovieFromIntent();               //2
        displayMovieDetails();              //3
        manageSaveIcon();                   //4
        clickToGoBack(backImageView);       //5
        clickToSearchMovie(searchImageView);//6

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        addToSavedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieViewModel.saveMovie(movie);
                movie.setSaved(true);
                movieViewModel.updateMovie(movie);
                manageSaveIcon();
            }
        });

        deleteFromSavedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieViewModel.deleteMovie(movie);
                addToSavedImageView.setVisibility(View.VISIBLE);
                deleteFromSavedImageView.setVisibility(View.INVISIBLE);
            }
        });
    }

    //1
    private void bindViews() {
        titleTextView = findViewById(R.id.titleTextView);
        overviewTextView = findViewById(R.id.overviewTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        releasedTextView = findViewById(R.id.releasedTextView);
        languageTextView = findViewById(R.id.languageTextView);
        backImageView = findViewById(R.id.backImageView);
        posterImageView = findViewById(R.id.posterImageView);
        addToSavedImageView = findViewById(R.id.addToSavedImageView);
        deleteFromSavedImageView = findViewById(R.id.deleteFromSavedImageView);
        searchImageView = findViewById(R.id.searchImageView);
    }

    //2
    private void getMovieFromIntent() {
        Gson gson = new Gson();
        intent = getIntent();
        movie = gson.fromJson(intent.getStringExtra("EXTRA_MOVIE"), Movie.class);
    }

    //3
    private void displayMovieDetails() {

        String title = movie.getTitle() != null ? movie.getTitle() : "";
        titleTextView.setText(title);

        String overview = movie.getOverview() != null ? movie.getOverview() : "";
        overviewTextView.setText(overview);

        String rating = movie.getRating() != null ? movie.getRating().toString() : "";
        ratingTextView.setText(rating);

        String releasedDate = movie.getReleaseDate() != null ? movie.getReleaseDate() : "";
        releasedTextView.setText(releasedDate);

        String language = movie.getLanguage() != null ? movie.getLanguage().toUpperCase() : "";
        languageTextView.setText(language);

        String posterPath = movie.getPosterPath() != null ? TMDB_IMAGE_URL + movie.getPosterPath() : "";
        Glide.with(this).load(posterPath).into(posterImageView);
    }

    //4
    private void manageSaveIcon() {
        if (movie.isSaved()) {
            addToSavedImageView.setVisibility(View.INVISIBLE);
            deleteFromSavedImageView.setVisibility(View.VISIBLE);
        } else {
            addToSavedImageView.setVisibility(View.VISIBLE);
            deleteFromSavedImageView.setVisibility(View.INVISIBLE);
        }
    }

    //5
    private void clickToGoBack(ImageView backImageView) {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity(); //5a
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity(); //5a
    }

    //5a
    private void goToMainActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    //6
    private void clickToSearchMovie(ImageView searchImageView) {
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchMovieActivity(); //6a
            }
        });
    }

    //6a
    private void goToSearchMovieActivity() {
        Intent backToSearchActivity = new Intent(this, SearchActivity.class);
        startActivity(backToSearchActivity);
    }
}
