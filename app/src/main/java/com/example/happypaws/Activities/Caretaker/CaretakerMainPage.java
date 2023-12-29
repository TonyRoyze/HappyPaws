package com.example.happypaws.Activities.Caretaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypaws.Classes.Post;
import com.example.happypaws.Classes.RecViewAdaptors.JobRecViewAdaptor;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;

import java.util.List;


public class CaretakerMainPage extends AppCompatActivity {

    private final DatabaseHelper databaseHelper = new DatabaseHelper(this);

    public static int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_main_page);

        ImageView profileIcon = findViewById(R.id.ic_profile);
        RecyclerView postList = findViewById(R.id.post_list_rec_view);

        List<Post> PostList = databaseHelper.getAllPosts();


        JobRecViewAdaptor jobRecViewAdaptor = new JobRecViewAdaptor(this);
        jobRecViewAdaptor.setPosts(PostList);
        postList.setAdapter(jobRecViewAdaptor);
        postList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);


        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage = new Intent(CaretakerMainPage.this, CaretakerReviewsPage.class);
                profilePage.putExtra("id", id);
                startActivity(profilePage);
            }
        });
    }
}