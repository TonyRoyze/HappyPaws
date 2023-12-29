package com.example.happypaws.Activities.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypaws.Activities.Shared.RegisterPage;
import com.example.happypaws.Classes.RecViewAdaptors.OwnerReviewRecViewAdaptor;
import com.example.happypaws.Classes.RecViewAdaptors.OwnerToReviewRecViewAdaptor;
import com.example.happypaws.Classes.Review;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;

import java.util.List;

public class OwnerReviewsPage extends AppCompatActivity {
    private TextView label;
    private ImageView backIcon, profileIcon;
    private RecyclerView myReviewList, toReviewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_reviews_page);

        backIcon = findViewById(R.id.ic_back_arrow);
        profileIcon = findViewById(R.id.ic_profile);
        label = findViewById(R.id.my_reviews_label);
        myReviewList = findViewById(R.id.my_review_list_rec_view);
        toReviewList = findViewById(R.id.to_review_list_rec_view);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Review> reviews = databaseHelper.getReviews(id);
        List<Review> pendingReviews = databaseHelper.getPendingReviews(id);


        OwnerReviewRecViewAdaptor reviewRecViewAdaptor = new OwnerReviewRecViewAdaptor(this);
        reviewRecViewAdaptor.setReviews(reviews);
        myReviewList.setAdapter(reviewRecViewAdaptor);
        myReviewList.setLayoutManager(new LinearLayoutManager(this));

        if(pendingReviews.size() > 0) {
            OwnerToReviewRecViewAdaptor toReviewRecViewAdaptor = new OwnerToReviewRecViewAdaptor(this);
            toReviewRecViewAdaptor.setReviews(pendingReviews);
            toReviewList.setAdapter(toReviewRecViewAdaptor);
            toReviewList.setLayoutManager(new LinearLayoutManager(this));
        } else {
            toReviewList.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) label.getLayoutParams();
            params.setMargins(0, dpToPx(10), 0, 0); // Assuming 'label' is a view, dpToPx() converts dp to pixels
            label.setLayoutParams(params);
            myReviewList.getLayoutParams().height = dpToPx(700);
        }



        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ownerMainPage = new Intent(OwnerReviewsPage.this, OwnerMainPage.class);
                ownerMainPage.putExtra("id", id);
                startActivity(ownerMainPage);
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage =  new Intent(OwnerReviewsPage.this, RegisterPage.class);
                profilePage.putExtra("id", id);
                startActivity(profilePage);
            }
        });

    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}