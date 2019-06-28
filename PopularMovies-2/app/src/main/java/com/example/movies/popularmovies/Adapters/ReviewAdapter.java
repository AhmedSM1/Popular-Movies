package com.example.movies.popularmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movies.popularmovies.Model.Review;
import com.example.movies.popularmovies.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item,viewGroup,false);
       return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder myViewHolder, int i) {
        final Review review = reviews.get(i);
        myViewHolder.title.setText(review.getAuthor());
        String url = review.getUrl();
        myViewHolder.url.setText(url);
        Linkify.addLinks(myViewHolder.url,Linkify.WEB_URLS);


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title , url;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.review_author);
            url = itemView.findViewById(R.id.review_link);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                }
            });
        }
    }

}
