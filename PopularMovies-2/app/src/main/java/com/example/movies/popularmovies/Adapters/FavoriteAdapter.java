package com.example.movies.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.movies.popularmovies.Model.Movie;
import com.example.movies.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavouritesAdapterViewHolder> {

        public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
        private List<Movie> mMovies;
        private Context context;
        private FavouritesAdapterOnClickHandler favouritesAdapterOnClickHandler;

        public interface FavouritesAdapterOnClickHandler {
            void onClick(Movie movie);
        }

        public FavoriteAdapter(Context context, FavouritesAdapterOnClickHandler favouritesAdapterOnClickHandler) {
            this.favouritesAdapterOnClickHandler = favouritesAdapterOnClickHandler;
            this.context = context;
        }

        public class FavouritesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView posterImageView;
            public FavouritesAdapterViewHolder(View view) {
                super(view);
                posterImageView = view.findViewById(R.id.imageView);
                view.setOnClickListener(this);
            }


            @Override
            public void onClick(View view) {
                int adapterPosition = getAdapterPosition();
                Movie movie = mMovies.get(adapterPosition);
                favouritesAdapterOnClickHandler.onClick(movie);
            }
        }

        @Override
        public FavouritesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            context = parent.getContext();
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
            return new FavouritesAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FavouritesAdapterViewHolder holder, int position) {

            Movie movie = mMovies.get(position);
            String poster = POSTER_PATH + movie.getPosterPath();
            Picasso.with(context)
                    .load(poster)
                    .centerCrop()
                    .into(holder.posterImageView);
        }

        @Override
        public int getItemCount() {
            if (null == mMovies) return 0;
            return mMovies.size();
        }

        public List<Movie> getMovies() {
            return mMovies;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public void setFavouriteMovieData(List<Movie> movies) {
            mMovies = movies;
            notifyDataSetChanged();
        }

    }
