package com.example.happypaws.Classes.RecViewAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypaws.Classes.Review;
import com.example.happypaws.Classes.User;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;

import java.util.ArrayList;
import java.util.List;

public class OwnerReviewRecViewAdaptor extends RecyclerView.Adapter<OwnerReviewRecViewAdaptor.ViewHolder> {
    List<Review> reviews = new ArrayList<>();
    List<User> users = new ArrayList<>();
    DatabaseHelper databaseHelper;
    Context context;

    public OwnerReviewRecViewAdaptor(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.review = review;

        this.users = databaseHelper.getAllUsers();

        for (User user : users){
            if (user.getId() == review.getFromId())
                holder.userName.setText(user.getName());
        }

        holder.reviewComment.setText(review.getReview());
        holder.reviewRating.setRating((float) review.getRating());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, reviewComment;
        RatingBar reviewRating;
        Review review;
        OwnerReviewRecViewAdaptor adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            reviewComment = itemView.findViewById(R.id.review_comment);
            reviewRating = itemView.findViewById(R.id.review_rating);

        }

        public ViewHolder linkAdapter(OwnerReviewRecViewAdaptor adapter) {
            this.adapter = adapter;
            return this;
        }

    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
