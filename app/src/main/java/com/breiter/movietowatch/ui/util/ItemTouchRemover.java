package com.breiter.movietowatch.ui.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.breiter.movietowatch.R;
import com.breiter.movietowatch.ui.adapter.MovieAdapter;
import com.breiter.movietowatch.ui.viewmodel.MovieViewModel;


public class ItemTouchRemover extends ItemTouchHelper.SimpleCallback {
    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private Context context;

    public ItemTouchRemover(MovieViewModel movieViewModel, MovieAdapter movieAdapter, Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.movieViewModel = movieViewModel;
        this.movieAdapter = movieAdapter;
        this.context = context;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        movieViewModel.deleteMovie(movieAdapter.getMovieAt(viewHolder.getAdapterPosition()));
        Toast.makeText(context, "Movie deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView
            recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Drawable icon = context.getDrawable(R.drawable.ic_delete);
        ColorDrawable background = new ColorDrawable(Color.RED);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX < 0) {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());

        } else {
            background.setBounds(0, 0, 0, 0);
            icon.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        icon.draw(c);
    }
}
