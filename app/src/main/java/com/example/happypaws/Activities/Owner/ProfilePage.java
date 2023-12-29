package com.example.happypaws.Activities.Owner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.happypaws.Activities.Shared.LoginPage;
import com.example.happypaws.Activities.Shared.RegisterPage;
import com.example.happypaws.Classes.RecViewAdaptors.OwnerReviewRecViewAdaptor;
import com.example.happypaws.Classes.RecViewAdaptors.OwnerToReviewRecViewAdaptor;
import com.example.happypaws.Classes.Review;
import com.example.happypaws.Classes.User;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        ImageView backIcon = findViewById(R.id.ic_back_arrow);
        ImageView btnLogout = findViewById(R.id.ic_logout);
        MaterialButton btnEdit = findViewById(R.id.btn_edit);
        TextView userName = findViewById(R.id.user_name);
        TextView userEmail = findViewById(R.id.user_email);
        TextView reviewsTab = findViewById(R.id.reviews_tab);
        TextView toReviewTab = findViewById(R.id.my_reviews_tab);


        RecyclerView myReviewList = findViewById(R.id.my_review_list_rec_view);
        RecyclerView toReviewList = findViewById(R.id.to_review_list_rec_view);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<User> users = databaseHelper.getAllUsers();
        List<Review> reviews = databaseHelper.getReviews(id);
        List<Review> pendingReviews = databaseHelper.getPendingReviews(id);

        OwnerReviewRecViewAdaptor reviewRecViewAdaptor = new OwnerReviewRecViewAdaptor(this);
        reviewRecViewAdaptor.setReviews(reviews);
        myReviewList.setAdapter(reviewRecViewAdaptor);
        myReviewList.setLayoutManager(new LinearLayoutManager(this));

        OwnerToReviewRecViewAdaptor toReviewRecViewAdaptor = new OwnerToReviewRecViewAdaptor(this);
        toReviewRecViewAdaptor.setReviews(pendingReviews);
        toReviewList.setAdapter(toReviewRecViewAdaptor);
        toReviewList.setLayoutManager(new LinearLayoutManager(this));
        toReviewList.setVisibility(View.GONE);

        if (id >= 0) {
            User user = null;

            for (User f : users) {
                if (f.getId() == id) {
                    user = f;
                }
            }

            userName.setText(user.getName());
            userEmail.setText(user.getEmail());
        }

        toReviewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myReviewList.setVisibility(View.GONE);
                reviewsTab.setBackgroundResource(R.drawable.tab_background);
                reviewsTab.setTextColor(getResources().getColor(R.color.primary_content));

                toReviewList.setVisibility(View.VISIBLE);
                toReviewTab.setBackgroundResource(R.drawable.selected_tab_background);
                toReviewTab.setTextColor(getResources().getColor(R.color.secondary_content));

            }
        });

        reviewsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReviewList.setVisibility(View.GONE);
                toReviewTab.setBackgroundResource(R.drawable.tab_background);
                toReviewTab.setTextColor(getResources().getColor(R.color.primary_content));

                myReviewList.setVisibility(View.VISIBLE);
                reviewsTab.setBackgroundResource(R.drawable.selected_tab_background);
                reviewsTab.setTextColor(getResources().getColor(R.color.secondary_content));

            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ownerMainPage = new Intent(ProfilePage.this, OwnerMainPage.class);
                ownerMainPage.putExtra("id", id);
                startActivity(ownerMainPage);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage =  new Intent(ProfilePage.this, RegisterPage.class);
                profilePage.putExtra("id", id);
                startActivity(profilePage);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ProfilePage.this, LoginPage.class);
                startActivity(login);
            }
        });

    }

}