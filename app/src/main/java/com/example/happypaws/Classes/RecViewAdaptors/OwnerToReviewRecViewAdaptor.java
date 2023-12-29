package com.example.happypaws.Classes.RecViewAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypaws.Classes.Review;
import com.example.happypaws.Classes.User;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;

import java.util.ArrayList;
import java.util.List;

public class OwnerToReviewRecViewAdaptor extends RecyclerView.Adapter<OwnerToReviewRecViewAdaptor.ViewHolder> {
    List<Review> reviews = new ArrayList<>();
    List<User> users = new ArrayList<>();
    DatabaseHelper databaseHelper;
    Context context;

    public OwnerToReviewRecViewAdaptor(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_review_item, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.review = review;

        this.users = databaseHelper.getAllUsers();

        for (User user : users){
            if (user.getId() == review.getForId())
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
        TextView userName;
        EditText reviewComment;
        RatingBar reviewRating;
        Button btnPost;
        Review review;
        OwnerToReviewRecViewAdaptor adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            reviewComment = itemView.findViewById(R.id.review_comment);
            reviewRating = itemView.findViewById(R.id.review_rating);
            btnPost = itemView.findViewById(R.id.btn_post);

            btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    review.setReview(reviewComment.getText().toString());
                    review.setRating(reviewRating.getRating());
                    review.setStatus("Posted");

                    Review updatedReview = review;

                    boolean success = databaseHelper.updateReview(updatedReview);
                    if (success) {
                        Toast.makeText(context, "Review " + updatedReview.getReview() + "added", Toast.LENGTH_SHORT).show();
                        adapter.reviews.remove(getAdapterPosition());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed to update review", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        public ViewHolder linkAdapter(OwnerToReviewRecViewAdaptor adapter) {
            this.adapter = adapter;
            return this;
        }

    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
