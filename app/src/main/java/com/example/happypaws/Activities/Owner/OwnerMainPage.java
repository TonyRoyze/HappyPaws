package com.example.happypaws.Activities.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypaws.Classes.Pet;
import com.example.happypaws.Classes.Post;
import com.example.happypaws.Classes.RecViewAdaptors.PetRecViewAdaptor;
import com.example.happypaws.Classes.RecViewAdaptors.PostRecViewAdaptor;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class OwnerMainPage extends AppCompatActivity {
    private MaterialTextView title;
    private RecyclerView petList, postList;
    private final DatabaseHelper databaseHelper = new DatabaseHelper(this);
    public static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main_page);

        ImageView profileIcon = findViewById(R.id.ic_profile);
        title = findViewById(R.id.title);
        petList = findViewById(R.id.pet_list_rec_view);
        postList = findViewById(R.id.post_list_rec_view);

        FloatingActionButton btnAdd = findViewById(R.id.btn_add);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        List<Pet> defaultPetList = databaseHelper.getMyPets(id);
        List<Post> activePostList = databaseHelper.getMyActivePosts(id);

        PetRecViewAdaptor petRecViewAdaptor = new PetRecViewAdaptor(this);
        petRecViewAdaptor.setPets(defaultPetList);
        petList.setAdapter(petRecViewAdaptor);
        petList.setLayoutManager(new LinearLayoutManager(this));

        PostRecViewAdaptor postRecViewAdaptor = new PostRecViewAdaptor(this);
        postRecViewAdaptor.setPosts(activePostList);
        postList.setAdapter(postRecViewAdaptor);
        postList.setLayoutManager(new LinearLayoutManager(this));

        petList.setVisibility(View.VISIBLE);
        postList.setVisibility(View.GONE);


        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage = new Intent(OwnerMainPage.this, ProfilePage.class);
                profilePage.putExtra("id", id);
                startActivity(profilePage);
            }
        });


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().equals(getString(R.string.post_details))) {
                    title.setText(R.string.pet_details);
                    petList.setVisibility(View.VISIBLE);
                    postList.setVisibility(View.GONE);
                } else {
                    title.setText(R.string.post_details);
                    petList.setVisibility(View.GONE);
                    postList.setVisibility(View.VISIBLE);
                }

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().equals(getString(R.string.post_details))) {
                    Intent addEditPost = new Intent(OwnerMainPage.this, AddEditPost.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", id);
                    addEditPost.putExtras(bundle);
                    startActivity(addEditPost);
                } else {
                    Intent addEditPet = new Intent(OwnerMainPage.this, AddEditPet.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", id);
                    addEditPet.putExtras(bundle);
                    startActivity(addEditPet);
                }
            }
        });

    }

    public int getId() {
        return id;
    }
}