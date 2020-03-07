package com.breiter.movietowatch.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.breiter.movietowatch.R;
import com.breiter.movietowatch.data.entity.Movie;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private static final String TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    private OnItemClickListener listener;
    private List<Movie> movieList = new ArrayList<>();
    private Context context;

    public void setMovies(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_movie, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieHolder holder, int position) {
        Movie movie = movieList.get(position);

        String title = movie.getTitle() != null ? movie.getTitle() : "";
        holder.titleTextVIew.setText(title);

        String posterPath = movie.getPosterPath() != null ? TMDB_IMAGE_URL + movie.getPosterPath() : "";
        Glide.with(context).load(posterPath).into(holder.posterImageView);

        String rating = movie.getRating() != null ? movie.getRating().toString() : "";
        holder.ratingTextVIew.setText(rating);

        holder.yearTextVIew.setText(getReleasedYear(movie));
    }

    private String getReleasedYear(Movie movie) {
        String fullDate = movie.getReleaseDate() != null ? movie.getReleaseDate() : "";
        if (fullDate.length() > 4)
            return fullDate.substring(0, 4);
        else return fullDate;
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public Movie getMovieAt(int position) {
        return movieList.get(position);
    }

    public void clearMovieList() {
        movieList.clear();
        notifyDataSetChanged();
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView posterImageView;
        private TextView titleTextVIew;
        private TextView yearTextVIew;
        private TextView ratingTextVIew;

        MovieHolder(@NonNull View itemView) {
            super(itemView);

            posterImageView = itemView.findViewById(R.id.posterImageView);
            titleTextVIew = itemView.findViewById(R.id.titleTextVIew);
            yearTextVIew = itemView.findViewById(R.id.yearTextVIew);
            ratingTextVIew = itemView.findViewById(R.id.ratingTextVIew);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(movieList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}