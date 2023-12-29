package com.example.happypaws.Activities.Shared;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happypaws.Activities.Caretaker.CaretakerMainPage;
import com.example.happypaws.Activities.Owner.OwnerMainPage;
import com.example.happypaws.Classes.User;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Objects;

public class RegisterPage extends AppCompatActivity {

    private Spinner userTypeSpinner;
    private EditText userName, userEmail, userPhone, userAddress, userPassword, userRePassword;
    private User user;
    private int id;
    private final DatabaseHelper databaseHelper = new DatabaseHelper(this);


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        ImageView backIcon = findViewById(R.id.ic_back_arrow);
        userTypeSpinner = findViewById(R.id.user_type);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        userAddress = findViewById(R.id.user_address);
        userPassword = findViewById(R.id.user_password);
        userRePassword = findViewById(R.id.user_repassword);
        MaterialButton btnAdd = findViewById(R.id.btn_add_user);

        String[] type = {"Owner", "Caretaker"};

        List<User> users = databaseHelper.getAllUsers();

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, type);
        userTypeSpinner.setAdapter(typeAdapter);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        if (id >= 0) {
            for (User f : users) {
                if (f.getId() == id) {
                    this.user = f;
                }
            }
            userName.setText(user.getName());
            userEmail.setText(user.getEmail());
            userPhone.setText(user.getPhone());
            userAddress.setText(user.getAddress());
            userPassword.setText(user.getPassword());

            btnAdd.setText("Update");


            if (Objects.equals(user.getType(), "Owner")) {
                userTypeSpinner.setSelection(0);
            } else if (Objects.equals(user.getType(), "Caretaker")) {
                userTypeSpinner.setSelection(1);
            } else {
                userTypeSpinner.setSelection(0);
            }


        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userType = "Owner";

                if (userTypeSpinner.getSelectedItemPosition() == 0) {
                    userType = "Owner";
                } else if (userTypeSpinner.getSelectedItemPosition() == 1) {
                    userType = "Caretaker";
                }

                if (!(userName.getText().toString().trim().isEmpty() ||
                        userEmail.getText().toString().trim().isEmpty() ||
                        userPhone.getText().toString().trim().isEmpty() ||
                        userAddress.getText().toString().trim().isEmpty())) {

                    if (userPassword.getText().toString().equals(userRePassword.getText().toString())) {

                        if (id >= 0) {
                            try {
                                user = new User(id, userName.getText().toString(), userEmail.getText().toString(), userPhone.getText().toString(), userAddress.getText().toString(), userType, userPassword.getText().toString());
                            } catch (Exception e) {
                                Toast.makeText(RegisterPage.this, "Error Inserting User", Toast.LENGTH_SHORT).show();
                            }

                            boolean success = databaseHelper.updateUser(user);
                            if (success) {
                                Toast.makeText(RegisterPage.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                moveToPreviousPage(user);
                            } else {
                                Toast.makeText(RegisterPage.this, "Error", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            try {
                                user = new User(-1, userName.getText().toString(), userEmail.getText().toString(), userPhone.getText().toString(), userAddress.getText().toString(), userType, userPassword.getText().toString());

                            } catch (Exception e) {
                                Toast.makeText(RegisterPage.this, "Error Inserting User", Toast.LENGTH_SHORT).show();
                            }


                            boolean success = databaseHelper.insertUser(user);
                            if (success) {
                                Toast.makeText(RegisterPage.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterPage.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(RegisterPage.this, "Passwords Should Match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterPage.this, "Fields Cannot be Left Empty", Toast.LENGTH_SHORT).show();
                }


            }
        });


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id >= 0) {
                    moveToPreviousPage(user);
                } else {
                    moveToPreviousPage();
                }
            }
        });


    }

    private void moveToPreviousPage() {
        Intent loginPage = new Intent(RegisterPage.this, LoginPage.class);
        startActivity(loginPage);
    }

    private void moveToPreviousPage(User user) {
        if (Objects.equals(user.getType(), "Owner")) {
            Intent ownerMainPage = new Intent(RegisterPage.this, OwnerMainPage.class);
            ownerMainPage.putExtra("id", id);
            startActivity(ownerMainPage);
        } else {
            Intent caretakerMainPage = new Intent(RegisterPage.this, CaretakerMainPage.class);
            caretakerMainPage.putExtra("id", id);
            startActivity(caretakerMainPage);
        }

    }
}