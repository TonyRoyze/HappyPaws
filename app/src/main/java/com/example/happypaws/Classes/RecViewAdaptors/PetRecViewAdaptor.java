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

import com.example.happypaws.Activities.Owner.AddEditPet;
import com.example.happypaws.Activities.Owner.OwnerMainPage;
import com.example.happypaws.Classes.Pet;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetRecViewAdaptor extends RecyclerView.Adapter<PetRecViewAdaptor.ViewHolder>{
    List<Pet> pets = new ArrayList<>();
    Context context;
    public PetRecViewAdaptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet =  pets.get(position);

        holder.petName.setText(pet.getName());
        holder.petBreed.setText(pet.getBreed());

        holder.pet = pet;

        if(Objects.equals(pet.getType(), "Cat")){
            holder.petIcon.setImageResource(R.mipmap.ic_cat);
        } else if (Objects.equals(pet.getType(), "Dog")) {
            holder.petIcon.setImageResource(R.mipmap.ic_dog);
        }

    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout petCard;
        MaterialButton btnEdit, btnDelete;
        TextView petName, petBreed;
        ImageView petIcon;
        Pet pet;
        PetRecViewAdaptor adapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            petCard = itemView.findViewById(R.id.pet_card);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            petIcon = itemView.findViewById(R.id.pet_icon);
            petName = itemView.findViewById(R.id.user_name);
            petBreed = itemView.findViewById(R.id.pet_breed);

            btnDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    boolean success = databaseHelper.deletePet(pet);
                    if (success){
                        Toast.makeText(context, pet.getName() + " Deleted Successfully", Toast.LENGTH_SHORT).show();
                        adapter.pets.remove(getAdapterPosition());
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context, pet.getName() + " Delete Failed", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddEditPet.class);
                    Bundle bundle = new Bundle();
                    int userId = OwnerMainPage.id;
                    bundle.putInt("petId", pet.getId());
                    bundle.putInt("userId", userId);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        public ViewHolder linkAdapter(PetRecViewAdaptor adapter) {
            this.adapter = adapter;
            return this;
        }

    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
        notifyDataSetChanged();
    }
}
