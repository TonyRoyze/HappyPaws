package com.example.happypaws.Activities.Caretaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypaws.Activities.Shared.LoginPage;
import com.example.happypaws.Activities.Shared.RegisterPage;
import com.example.happypaws.Classes.RecViewAdaptors.CaretakerReviewRecViewAdaptor;
import com.example.happypaws.Classes.Review;
import com.example.happypaws.Classes.User;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CaretakerReviewsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_reviews_page);

        ImageView backIcon = findViewById(R.id.ic_back_arrow);
        ImageView btnLogout = findViewById(R.id.ic_logout);
        MaterialButton btnEdit = findViewById(R.id.btn_edit);
        RecyclerView myReviewList = findViewById(R.id.my_review_list_rec_view);
        TextView userName = findViewById(R.id.user_name);
        TextView userEmail = findViewById(R.id.user_email);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Review> reviews = databaseHelper.getReviews(id);
        List<User> users = databaseHelper.getAllUsers();


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

        CaretakerReviewRecViewAdaptor reviewRecViewAdaptor = new CaretakerReviewRecViewAdaptor(this);
        reviewRecViewAdaptor.setReviews(reviews);
        myReviewList.setAdapter(reviewRecViewAdaptor);
        myReviewList.setLayoutManager(new LinearLayoutManager(this));

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent caretakerMainPage = new Intent(CaretakerReviewsPage.this, CaretakerMainPage.class);
                caretakerMainPage.putExtra("id", id);
                startActivity(caretakerMainPage);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage = new Intent(CaretakerReviewsPage.this, RegisterPage.class);
                profilePage.putExtra("id", id);
                startActivity(profilePage);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(CaretakerReviewsPage.this, LoginPage.class);
                startActivity(login);
            }
        });

    }
}