package com.example.happypaws.Activities.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happypaws.Classes.Pet;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AddEditPet extends AppCompatActivity {
    private Spinner petTypeSpinner, petSexSpinner;
    private EditText petName, petAge, petBreed, petColor, petNote;
    private Pet pet;
    private int petId, userId;

    private final DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pet);

        ImageView backIcon = findViewById(R.id.ic_back_arrow);
        ImageView profileIcon = findViewById(R.id.ic_profile);
        petTypeSpinner = findViewById(R.id.pet_type);
        petSexSpinner = findViewById(R.id.pet_sex);
        petName = findViewById(R.id.pet_name);
        petAge = findViewById(R.id.pet_age);
        petBreed = findViewById(R.id.pet_breed);
        petColor = findViewById(R.id.pet_color);
        petNote = findViewById(R.id.pet_note);
        MaterialButton btnAdd = findViewById(R.id.btn_add_pet);

        String[] type = {"Dog", "Cat"};
        String[] sex = {"Male", "Female"};

        List<Pet> pets = databaseHelper.getAllPets();

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, type);
        petTypeSpinner.setAdapter(typeAdapter);
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, sex);
        petSexSpinner.setAdapter(sexAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            petId = bundle.getInt("petId", -1);
            userId = bundle.getInt("userId", -1);

            if (petId >= 0) {
                for (Pet f : pets) {
                    if (f.getId() == petId) {
                        pet = f;
                        break;
                    }
                }
                petName.setText(pet.getName());
                petAge.setText(String.valueOf(pet.getAge()));
                petBreed.setText(pet.getBreed());
                petColor.setText(pet.getColor());
                petNote.setText(pet.getNote());

                btnAdd.setText(R.string.update);


                if (pet.getType() == "Dog") {
                    petTypeSpinner.setSelection(0);
                } else if (pet.getType() == "Cat") {
                    petTypeSpinner.setSelection(1);
                } else {
                    petTypeSpinner.setSelection(0);
                }

                if (pet.getSex() == "Male") {
                    petSexSpinner.setSelection(0);
                } else if (pet.getSex() == "Female") {
                    petTypeSpinner.setSelection(1);
                } else {
                    petSexSpinner.setSelection(0);
                }
            }
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String petType = "Dog";
                String petSex = "Male";

                if (petTypeSpinner.getSelectedItemPosition() == 0) {
                    petType = "Dog";
                } else if (petTypeSpinner.getSelectedItemPosition() == 1) {
                    petType = "Cat";
                }

                if (petSexSpinner.getSelectedItemPosition() == 0) {
                    petSex = "Male";
                } else if (petSexSpinner.getSelectedItemPosition() == 1) {
                    petSex = "Female";
                }

                if (!(petName.getText().toString().trim().isEmpty() ||
                        petAge.getText().toString().trim().isEmpty() ||
                        petBreed.getText().toString().trim().isEmpty() ||
                        petNote.getText().toString().trim().isEmpty() ||
                        petColor.getText().toString().trim().isEmpty())) {
                    Pet pet = null;
                    if (petId >= 0) {
                        try {
                            pet = new Pet(petId, petName.getText().toString(), Integer.parseInt(petAge.getText().toString()), petType, petBreed.getText().toString(), petSex, petColor.getText().toString(), petNote.getText().toString(), userId);
                        } catch (Exception e) {
                            Toast.makeText(AddEditPet.this, "Error Updating Pet", Toast.LENGTH_SHORT).show();
                        }

                        boolean success = databaseHelper.updatePet(pet);
                        if (success) {
                            Toast.makeText(AddEditPet.this, "Pet Details Updated Successfully", Toast.LENGTH_SHORT).show();
                            moveToMainPage();
                        } else {
                            Toast.makeText(AddEditPet.this, "Error", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        try {
                            pet = new Pet(-1, petName.getText().toString(), Integer.parseInt(petAge.getText().toString()), petType, petBreed.getText().toString(), petSex, petColor.getText().toString(), petNote.getText().toString(), userId);
                        } catch (Exception e) {
                            Toast.makeText(AddEditPet.this, "Error Updating Pet", Toast.LENGTH_SHORT).show();
                        }


                        boolean success = databaseHelper.insertPet(pet);
                        if (success) {
                            Toast.makeText(AddEditPet.this, "Pet Registered Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddEditPet.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        moveToMainPage();
                    }
                } else {
                    Toast.makeText(AddEditPet.this, "Fields Cannot be Left Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToMainPage();
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage = new Intent(AddEditPet.this, ProfilePage.class);
                profilePage.putExtra("id", userId);
                startActivity(profilePage);
            }
        });

    }

    private void moveToMainPage() {
        Intent ownerMainPage = new Intent(AddEditPet.this, OwnerMainPage.class);
        ownerMainPage.putExtra("id", userId);
        startActivity(ownerMainPage);
    }

}