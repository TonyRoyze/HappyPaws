package com.example.happypaws.Classes.RecViewAdaptors;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypaws.Activities.Caretaker.CaretakerMainPage;
import com.example.happypaws.Classes.Pet;
import com.example.happypaws.Classes.Post;
import com.example.happypaws.Classes.Review;
import com.example.happypaws.Classes.User;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JobRecViewAdaptor extends RecyclerView.Adapter<JobRecViewAdaptor.ViewHolder> {
    List<Post> posts = new ArrayList<>();
    List<Pet> pets = new ArrayList<>();
    List<User> users = new ArrayList<>();
    Context context;
    DatabaseHelper databaseHelper;
    Dialog dialog;
    Button btnDialogPost, btnDialogCancel;

    public JobRecViewAdaptor(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
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


        for (User user : users) {
            if (user.getId() == post.getOwnerId()) {
                holder.userName.setText(user.getName());
            }
        }

        holder.userContact.setText(post.getPhone());
        holder.userLocation.setText(post.getLocation());

        holder.postStartDate.setText(post.getStartDate());
        holder.postEndDate.setText(post.getEndDate());

        holder.postServices.setText(post.getServices());

        if (Objects.equals(post.getStatus(), "In Progress")) {
            holder.btnSelect.setText(R.string.applied);
            holder.btnSelect.setTextColor(context.getResources().getColor(R.color.success));
            holder.btnSelect.setStrokeColorResource(R.color.success);
        } else {
            holder.btnSelect.setText(R.string.apply);
            holder.btnSelect.setTextColor(context.getResources().getColor(R.color.error));
            holder.btnSelect.setStrokeColorResource(R.color.error);
        }

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout postCard;
        MaterialButton btnSelect;
        TextView petName, petBreed, petSex, petAge, petNote,
                userName, userLocation, userContact,
                postStartDate, postEndDate, postServices;
        ImageView petIcon;
        EditText postReview;
        RatingBar postRating;
        Pet pet;
        Post post;
        JobRecViewAdaptor adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postCard = itemView.findViewById(R.id.post_card);
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

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.review_dialog_box);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
            dialog.setCancelable(false);

            btnDialogCancel = dialog.findViewById(R.id.btn_cancel);
            btnDialogPost = dialog.findViewById(R.id.btn_post_review);

            postRating = dialog.findViewById(R.id.post_rating);
            postReview = dialog.findViewById(R.id.post_review);


            btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnDialogPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int caretakerId = CaretakerMainPage.id;

                    Review review = null;
                    try {
                        review = new Review(-1, post.getId(), post.getOwnerId(), caretakerId, "Posted", postRating.getRating(), postReview.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(context, "Must Add a Review", Toast.LENGTH_SHORT).show();
                    }

                    boolean success = databaseHelper.insertReview(review);
                    if (success) {
                        Toast.makeText(context, "Review Posted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error Inserting Data", Toast.LENGTH_SHORT).show();
                    }

                    try {
                        review = new Review(-1, post.getId(), caretakerId, post.getOwnerId(), "Pending");
                    } catch (Exception e) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }

                    success = databaseHelper.insertReview(review);
                    if (!success) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }
            });

            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Objects.equals(post.getStatus(), "In Progress")) {
                        post.setStatus("In Progress");
                        int caretakerId = CaretakerMainPage.id;
                        post.setCaretakerId(caretakerId);
                        btnSelect.setText(R.string.applied);
                        btnSelect.setTextColor(context.getResources().getColor(R.color.success));
                        btnSelect.setStrokeColorResource(R.color.success);
                    } else {
                        post.setStatus("Pending");
                        post.setCaretakerId(-1);
                        btnSelect.setText(R.string.apply);
                        btnSelect.setTextColor(context.getResources().getColor(R.color.error));
                        btnSelect.setStrokeColorResource(R.color.error);
                    }

                    Post updatedPost = post;

                    boolean success = databaseHelper.updatePost(updatedPost);
                    if (success) {
                        Toast.makeText(context, "Post Status: " + post.getStatus(), Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                    }
                }

            });

            btnSelect.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    post.setStatus("Completed");
                    Post updatedPost = post;

                    boolean success = databaseHelper.updatePost(updatedPost);
                    if (success) {
                        Toast.makeText(context, "Post Status: " + post.getStatus(), Toast.LENGTH_SHORT).show();
                        adapter.posts.remove(getAdapterPosition());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                    }

                    dialog.show();
                    return true;
                }
            });
        }

        public ViewHolder linkAdapter(JobRecViewAdaptor adapter) {
            this.adapter = adapter;
            return this;
        }

    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }
}
