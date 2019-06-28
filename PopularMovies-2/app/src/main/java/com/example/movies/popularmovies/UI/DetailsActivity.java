package com.example.movies.popularmovies.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movies.popularmovies.API.Client;
import com.example.movies.popularmovies.API.Services;
import com.example.movies.popularmovies.Adapters.ReviewAdapter;
import com.example.movies.popularmovies.Adapters.TrailerAdapter;
import com.example.movies.popularmovies.Database.AppExecutors;
import com.example.movies.popularmovies.Database.DetailsViewModel;
import com.example.movies.popularmovies.Database.DetailsViewModelFactory;
import com.example.movies.popularmovies.Database.FavoriteDatabase;
import com.example.movies.popularmovies.Model.Movie;
import com.example.movies.popularmovies.Model.Review;
import com.example.movies.popularmovies.Model.ReviewReply;
import com.example.movies.popularmovies.Model.Trailer;
import com.example.movies.popularmovies.Model.TrailerReply;
import com.example.movies.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final String API_KEY = "";
    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w/500";
    private Movie mCurrentMovie;List<Review> reviews= new ArrayList<>();
    List<Trailer> trailers= new ArrayList<>();
    private FavoriteDatabase mDb;
    Context context;
    boolean isFavourite;
    private ImageView star;
    private RecyclerView trailerRecyclerView, reviewRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        mDb = FavoriteDatabase.getDatabase(getApplicationContext());
        mCurrentMovie = getIntent().getParcelableExtra("movie");


         DetailsViewModelFactory modelFactory = new DetailsViewModelFactory(mDb, mCurrentMovie.getId());
      final DetailsViewModel viewModel = ViewModelProviders.of(this, modelFactory).get(DetailsViewModel.class);
                viewModel.getMovieLiveData().observe(this,new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie == null) {

                    isFavourite = false;
                    star.setImageResource(R.drawable.unfav);
                } else {

                    isFavourite = true;
                    star.setImageResource(R.drawable.fav);
                }
            }
        });
        star = findViewById(R.id.favoriteStar);
        star.setImageResource(R.drawable.unfav);
                      star.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              if (!isFavourite) {
                                  saveFavorite();
                                  star.setImageResource(R.drawable.fav);
                                  isFavourite = true;
                                  Toast.makeText(getApplicationContext(), "Added to My Favourites", Toast.LENGTH_SHORT).show();
                              } else {
                                  deleteFavorite();
                                  star.setImageResource(R.drawable.unfav);
                                  isFavourite = false;
                                  Toast.makeText(getApplicationContext(), "Removed from My Favourites", Toast.LENGTH_SHORT).show();
                              }
                          }
                      });


        String movieReleaseDate = mCurrentMovie.getReleaseDate();
        TextView releaseDateTextView = findViewById(R.id.release);
        releaseDateTextView.setText(movieReleaseDate);


        String movieTitle = mCurrentMovie.getTitle();
        TextView titleTextView = findViewById(R.id.movieTitle);
        titleTextView.setText(movieTitle);

        ImageView posterImageView = findViewById(R.id.movieImage);
    Picasso.with(this).load("https://image.tmdb.org/t/p/w500"+mCurrentMovie.getPosterPath()).into(posterImageView);
        TextView voteAverageTextView = findViewById(R.id.movieVote);
        voteAverageTextView.setText(String.valueOf(mCurrentMovie.getVoteAverage())+"/10");
        String overview = mCurrentMovie.getOverview();
        TextView overviewTextView = findViewById(R.id.movieOverview);
        overviewTextView.setText(overview);

     populateTrailers(mCurrentMovie.getId());
     populateReview(mCurrentMovie.getId());
    }
    public void saveFavorite() {
        final Movie favoriteMovie = new Movie(
                mCurrentMovie.getId(),
                mCurrentMovie.getTitle(),
                mCurrentMovie.getReleaseDate(),
                mCurrentMovie.getVoteAverage(),
                mCurrentMovie.getOverview(),
                mCurrentMovie.getPosterPath()
        );

        final FavoriteDatabase database = FavoriteDatabase.getDatabase(context);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.databaseDAO().insertMovie(favoriteMovie);
            }
        });
    }

    private void deleteFavorite() {
        final Movie favoriteMovie = new Movie(
                mCurrentMovie.getId(),
                mCurrentMovie.getTitle(),
                mCurrentMovie.getReleaseDate(),
                mCurrentMovie.getVoteAverage(),
                mCurrentMovie.getOverview(),
                mCurrentMovie.getPosterPath()
        );
        final FavoriteDatabase database = FavoriteDatabase.getDatabase(context);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.databaseDAO().deleteMovie(favoriteMovie);
            }
        });
    }

    //getting the trailers
    private void populateTrailers(int movie_id){
        Client client = new Client();
        Services services = client.getClient().create(Services.class);
        Call<TrailerReply> trailerCall;
        trailerCall = services.getTrailer(movie_id,API_KEY);
        trailerCall.enqueue(new Callback<TrailerReply>() {
            @Override
            public void onResponse(Call<TrailerReply> call, Response<TrailerReply> response) {
                TrailerReply trailerReply = response.body();
                trailers.clear();
                trailers.addAll(trailerReply.getResults());
                trailerRecyclerView=findViewById(R.id.trailersRecyclerView);
                trailerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                trailerAdapter = new TrailerAdapter(getApplicationContext(), trailers);
                trailerRecyclerView.setAdapter(trailerAdapter);
                trailerAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<TrailerReply> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"This Movie doesnt have trailers",Toast.LENGTH_LONG).show();
            }
        });}

    //getting The reviews
    private void populateReview(int movie_id){
        Client client = new Client();
        Services services = client.getClient().create(Services.class);
        Call<ReviewReply> call;
        call = services.getReviwes(movie_id,API_KEY);
        call.enqueue(new Callback<ReviewReply>() {
            @Override
            public void onResponse(Call<ReviewReply> call, Response<ReviewReply> response) {
                List<Review> reviews = response.body().getReviews();
                reviewAdapter = new ReviewAdapter(getApplicationContext(),reviews);
                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getApplicationContext(),1);
                RecyclerView recyclerView2 = findViewById(R.id.reviewRecyclerView);
                recyclerView2.setLayoutManager(gridLayoutManager2);
                recyclerView2.setAdapter(reviewAdapter);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ReviewReply> call, Throwable t) {
                Log.d("Error", "Failed to get a response");
                Toast.makeText(getApplicationContext(), "unable to fetch data",Toast.LENGTH_SHORT).show();
            }
        });




    }


}