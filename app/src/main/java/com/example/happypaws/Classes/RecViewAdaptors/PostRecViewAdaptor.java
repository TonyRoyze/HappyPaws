package com.example.happypaws.Classes.RecViewAdaptors;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypaws.Activities.Owner.AddEditPost;
import com.example.happypaws.Activities.Owner.OwnerMainPage;
import com.example.happypaws.Classes.Pet;
import com.example.happypaws.Classes.Post;
import com.example.happypaws.Classes.User;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostRecViewAdaptor extends RecyclerView.Adapter<PostRecViewAdaptor.ViewHolder>{
    List<Post> posts = new ArrayList<>();
    List<Pet> pets = new ArrayList<>();
    List<User> users = new ArrayList<>();
    Context context;
    DatabaseHelper databaseHelper;
    String userType;
    public PostRecViewAdaptor(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post =  posts.get(position);
        holder.post = post;

        this.pets = databaseHelper.getAllPets();
        this.users = databaseHelper.getAllUsers();

        for (Pet pet : pets) {
            if (pet.getId() == post.getPetId()) {
                holder.petName.setText(pet.getName());
                holder.petAge.setText(String.valueOf(pet.getAge()));
                holder.petBreed.setText(pet.getBreed());
                holder.petSex.setText(pet.getSex());
                holder.petNote.setText(pet.getNote());

                holder.pet = pet;

                if(Objects.equals(pet.getType(), "Cat")){
                    holder.petIcon.setImageResource(R.mipmap.ic_cat);
                } else if (Objects.equals(pet.getType(), "Dog")) {
                    holder.petIcon.setImageResource(R.mipmap.ic_dog);
                }

            }
        }

        int ownerId = OwnerMainPage.id;

        for (User user : users) {
            if (user.getId() == ownerId) {
                holder.userName.setText(user.getName());
                this.userType = user.getType();
            }
        }

        holder.userContact.setText(post.getPhone());
        holder.userLocation.setText(post.getLocation());

        holder.postStartDate.setText(post.getStartDate());
        holder.postEndDate.setText(post.getEndDate());

        holder.postServices.setText(post.getServices());

        if (Objects.equals(post.getStatus(), "In Progress")) {
            holder.btnSelect.setText(R.string.in_progress);
            holder.btnSelect.setTextColor(context.getResources().getColor(R.color.success));
            holder.btnSelect.setStrokeColorResource(R.color.success);
        } else {
            holder.btnSelect.setText(R.string.pending);
            holder.btnSelect.setTextColor(context.getResources().getColor(R.color.error));
            holder.btnSelect.setStrokeColorResource(R.color.error);

        }

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout postCard;
        MaterialButton btnSelect, btnEdit;
        TextView petName, petBreed, petSex, petAge, petNote,
                userName, userLocation, userContact,
                postStartDate, postEndDate,postServices;

        ImageView petIcon;
        Pet pet;
        Post post;
        PostRecViewAdaptor adapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postCard = itemView.findViewById(R.id.post_card);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnSelect = itemView.findViewById(R.id.btn_select);
            petIcon = itemView.findViewById(R.id.pet_icon);
            petName = itemView.findViewById(R.id.pet_name);
            petBreed = itemView.findViewById(R.id.pet_breed);
            petSex = itemView.findViewById(R.id.pet_sex);
            petAge = itemView.findViewById(R.id.pet_age);
            petNote = itemView.findViewById(R.id.pet_note);

            userName = itemView.findViewById(R.id.user_name);
            userLocation = itemView.findViewById(R.id.user_location);
            userContact = itemView.findViewById(R.id.user_contact);

            postStartDate = itemView.findViewById(R.id.post_start_date);
            postEndDate = itemView.findViewById(R.id.post_end_date);
            postServices = itemView.findViewById(R.id.post_services);


            btnEdit.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    boolean success = databaseHelper.deletePost(post);
                    if (success){
                        Toast.makeText(context, post.getId() + " Deleted Successfully", Toast.LENGTH_SHORT).show();
                        adapter.posts.remove(getAdapterPosition());
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context, post.getId() + " Delete Failed", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddEditPost.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("postId", post.getId());
                    bundle.putInt("userId", post.getOwnerId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        public ViewHolder linkAdapter(PostRecViewAdaptor adapter) {
            this.adapter = adapter;
            return this;
        }

    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }
}
