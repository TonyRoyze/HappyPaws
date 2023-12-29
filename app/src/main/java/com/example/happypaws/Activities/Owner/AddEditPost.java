package com.example.happypaws.Activities.Owner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happypaws.Classes.Pet;
import com.example.happypaws.Classes.Post;
import com.example.happypaws.Database.DatabaseHelper;
import com.example.happypaws.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEditPost extends AppCompatActivity {
    private ImageView backIcon, profileIcon;
    private Spinner petSpinner;
    private EditText postPhone, postLocation, postServices;
    private Button btnStartDate, btnEndDate;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private Post selectedPost;
    private int postId, userId;
    private String startDate, endDate;
    private final DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_post);

        backIcon = findViewById(R.id.ic_back_arrow);
        profileIcon = findViewById(R.id.ic_profile);
        petSpinner = findViewById(R.id.pet);
        postPhone = findViewById(R.id.post_phone);
        postLocation = findViewById(R.id.post_location);
        postServices = findViewById(R.id.post_services);
        MaterialButton btnAdd = findViewById(R.id.btn_add_post);

        btnStartDate = findViewById(R.id.post_start_date);
        btnEndDate = findViewById(R.id.post_end_date);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        postId = bundle.getInt("postId", -1);
        userId = bundle.getInt("userId", -1);

        List<Pet> pets = databaseHelper.getMyPets(userId);
        List<Post> posts = databaseHelper.getMyActivePosts(userId);
        List<String> petNames = new ArrayList<>();

        for (Pet pet : pets) {
            petNames.add(pet.getName());
        }

        ArrayAdapter<String> petAdapter = new ArrayAdapter<>(this, R.layout.spinner_list, petNames);
        petSpinner.setAdapter(petAdapter);


        if (postId >= 0) {
            for (Post f : posts) {
                if (f.getId() == postId) {
                    selectedPost = f;
                    break;
                }
            }
            postPhone.setText(selectedPost.getPhone());
            postLocation.setText(selectedPost.getLocation());
            postServices.setText(selectedPost.getServices());
            startDate = selectedPost.getStartDate();
            endDate = selectedPost.getEndDate();
            btnStartDate.setText(startDate);
            btnEndDate.setText(endDate);
            initDatePicker();


            btnAdd.setText("Update");

            int selectedPet = 0;
            for (Pet pet : pets) {
                if (pet.getId() == selectedPost.getPetId()) {
                    break;
                }
                selectedPet += 1;
            }

            petSpinner.setSelection(selectedPet);

        } else {
            initDatePicker();
            btnStartDate.setText(getTodayDate());
            btnEndDate.setText(getTodayDate());
        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = null;
                Object petName = petSpinner.getSelectedItem();


                int petId = 0;
                for (Pet pet : pets) {
                    if (petName == pet.getName()) {
                        petId = pet.getId();
                    }
                }
                if (!(postPhone.getText().toString().trim().isEmpty() ||
                        postLocation.getText().toString().trim().isEmpty() ||
                        postServices.getText().toString().trim().isEmpty() ||
                        btnStartDate.getText().toString().trim().isEmpty() ||
                        btnEndDate.getText().toString().trim().isEmpty())) {
                    if (postId >= 0) {
                        try {
                            post = new Post(postId, selectedPost.getOwnerId(), selectedPost.getCaretakerId(), petId, postPhone.getText().toString(), postLocation.getText().toString(), postServices.getText().toString(), selectedPost.getStatus(), btnStartDate.getText().toString(), btnEndDate.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(AddEditPost.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        boolean success = databaseHelper.updatePost(post);
                        if (success) {
                            Toast.makeText(AddEditPost.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            moveToMainPage();
                        } else {
                            Toast.makeText(AddEditPost.this, "Error Updating Data", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        try {
                            post = new Post(-1, userId, -1, petId, postPhone.getText().toString(), postLocation.getText().toString(), postServices.getText().toString(), "Pending", btnStartDate.getText().toString(), btnEndDate.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(AddEditPost.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        boolean success = databaseHelper.insertPost(post);
                        if (success) {
                            Toast.makeText(AddEditPost.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            moveToMainPage();
                        } else {
                            Toast.makeText(AddEditPost.this, "Error Inserting Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(AddEditPost.this, "Fields Cannot be Left Empty", Toast.LENGTH_SHORT).show();
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
                Intent profilePage = new Intent(AddEditPost.this, ProfilePage.class);
                profilePage.putExtra("id", userId);
                startActivity(profilePage);
            }
        });

    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                System.out.println("Date: " + date);
                btnStartDate.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnEndDate.setText(date);
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        if (startDate != null && endDate != null) {
            int[] date = makeDateInt(startDate);
            startDatePickerDialog = new DatePickerDialog(this, style, startDateSetListener, date[0], date[1], date[2]);
            date = makeDateInt(endDate);
            endDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, date[0], date[1], date[2]);
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            startDatePickerDialog = new DatePickerDialog(this, style, startDateSetListener, year, month, day);
            endDatePickerDialog = new DatePickerDialog(this, style, endDateSetListener, year, month, day);
        }

        // Get the current time
        long currentTime = new Date().getTime() - 10000;

        // Set the minimum date for the DatePicker to the current time
        startDatePickerDialog.getDatePicker().setMinDate(currentTime);
        endDatePickerDialog.getDatePicker().setMinDate(currentTime);


    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private int[] makeDateInt(String date) {
        String[] dateParts = date.split(" ");
        int[] dateIntParts = {Integer.parseInt(dateParts[2]), getMonthFormat(dateParts[0]), Integer.parseInt(dateParts[1])};
        return dateIntParts;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        return "JAN";
    }

    private Integer getMonthFormat(String month) {
        switch (month) {
            case "JAN":
                return 0;
            case "FEB":
                return 1;
            case "MAR":
                return 2;
            case "APR":
                return 3;
            case "MAY":
                return 4;
            case "JUN":
                return 5;
            case "JUL":
                return 6;
            case "AUG":
                return 7;
            case "SEP":
                return 8;
            case "OCT":
                return 9;
            case "NOV":
                return 10;
            case "DEC":
                return 11;
            default:
                return 0; // or handle the default case accordingly
        }
    }


    private void moveToMainPage() {
        Intent ownerMainPage = new Intent(AddEditPost.this, OwnerMainPage.class);
        ownerMainPage.putExtra("id", userId);
        startActivity(ownerMainPage);
    }

    public void openStartDatePicker(View view) {
        startDatePickerDialog.show();
    }

    public void openEndDatePicker(View view) {
        endDatePickerDialog.show();
    }
}