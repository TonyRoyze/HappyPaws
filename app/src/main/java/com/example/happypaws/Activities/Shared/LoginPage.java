package com.example.happypaws.Activities.Shared;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happypaws.Activities.Caretaker.CaretakerMainPage;
import com.example.happypaws.Activities.Owner.OwnerMainPage;
import com.example.happypaws.Classes.User;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class LoginPage extends AppCompatActivity {

    private EditText userEmail, userPass;
    private final DatabaseHelper  databaseHelper = new DatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        MaterialButton btnLogin = findViewById(R.id.btn_login);
        userEmail = findViewById(R.id.user_email);
        userPass = findViewById(R.id.password);
        TextView regLink = findViewById(R.id.register_link);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = userEmail.getText().toString();
                    String pass = userPass.getText().toString();
                    boolean loginSuccess = false;

                    List<User> users = databaseHelper.getAllUsers();
                    for (User u : users) {
                        if (u.getEmail().equals(email) && u.getPassword().equals(pass)) {
                            Intent intent;
                            loginSuccess = true;
                            if (u.getType().equals("Caretaker")) {
                                intent = new Intent(LoginPage.this, CaretakerMainPage.class);
                            } else {
                                intent = new Intent(LoginPage.this, OwnerMainPage.class);
                            }
                            intent.putExtra("id", u.getId());
                            startActivity(intent);
                        }
                    }
                    if (!loginSuccess) {
                        Toast.makeText(LoginPage.this, "Entered Email or Password is Incorrect", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginPage.this, "Fields Cannot be Left Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }
}