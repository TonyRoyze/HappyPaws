package com.example.happypaws.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.happypaws.Classes.Pet;
import com.example.happypaws.Classes.Post;
import com.example.happypaws.Classes.Review;
import com.example.happypaws.Classes.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "happyPaws.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, phone TEXT, address TEXT, type TEXT, password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS pets( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER, type TEXT, breed TEXT, sex TEXT, color TEXT, note TEXT, userid INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS posts( id INTEGER PRIMARY KEY AUTOINCREMENT, ownerid INTEGER, caretakerid INTEGER, petid INTEGER, phone TEXT, location TEXT, services TEXT, status TEXT, startDate TEXT, endDate TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS reviews( id INTEGER PRIMARY KEY AUTOINCREMENT, postid INTEGER, forid INTEGER, fromid INTEGER, status TEXT, rating REAL, review TEXT)");
        sqLiteDatabase.execSQL("INSERT INTO users (name, email, phone, address, type, password) VALUES " +
                "('Olivia', 'olivia@gmail.com', '0771122334', '1234 Elm Street, New York', 'Caretaker', 'asd')," +
                "('Benjamin', 'benjamin@gmail.com', '0719988776', '15 Oxford Street, London', 'Owner', 'asd')," +
                "('Grace', 'grace@gmail.com', '0765544332', '42 Rue de la Paix, Paris', 'Caretaker', 'graceful12')," +
                "('Liam', 'liam@gmail.com', '0701122334', '50 Sydney Harbour, Sydney', 'Owner', 'liamD123')," +
                "('Eleanor', 'eleanor@gmail.com', '0723344556', '17 Friedrichstraße, Berlin', 'Caretaker', 'el123')");
        sqLiteDatabase.execSQL("INSERT INTO pets (name, age, type, breed, sex, color, note, userid) VALUES " +
                "('Buddy', 2, 'Dog', 'Golden Retriever', 'Male', 'Golden', 'Loves to swim', 2)," +
                "('Whiskers', 5, 'Cat', 'Persian', 'Female', 'White', 'Enjoys catnip', 4)," +
                "('Rocky', 4, 'Dog', 'German Shepherd', 'Male', 'Black and Tan', 'Likes long walks', 2)," +
                "('Mia', 3, 'Dog', 'Labrador', 'Female', 'Chocolate', 'Very friendly', 4)," +
                "('Charlie', 6, 'Dog', 'Poodle', 'Male', 'Apricot', 'Great with kids', 2)");
        sqLiteDatabase.execSQL("INSERT INTO posts (ownerid, caretakerid, petid, phone, location, services, status, startDate, endDate) VALUES " +
                "(2, 1, 1, '0776665554', 'Central Park, New York', 'Walking, Playtime', 'Completed', 'JAN 3 2024', 'JAN 5 2024')," +
                "(4, 3, 2, '0712345678', 'Covent Garden, London', 'Feeding, Litter Box Cleaning', 'Completed', 'JAN 3 2024', 'JAN 5 2024')," +
                "(2, 5, 3, '0765432198', 'Louvre Museum, Paris', 'Training, Exercise', 'Completed', 'JAN 3 2024', 'JAN 5 2024')," +
                "(4, -1, 4, '0709876543', 'Opera House, Sydney', 'Grooming, Vet Visit', 'Pending', 'JAN 3 2024', 'JAN 5 2024')," +
                "(2, -1, 5, '0723456789', 'Berlin Wall Memorial, Berlin', 'Playtime, Medication', 'Pending', 'JAN 3 2024', 'JAN 5 2024')");
        sqLiteDatabase.execSQL("INSERT INTO reviews (postid, forid, fromid, status, rating, review) VALUES " +
                "(1, 1, 2, 'Posted', 5.0, 'Olivia did an exceptional job with Buddy!')," +
                "(1, 2, 1, 'Posted', 4.0, 'Benjamin took great care of Buddy.')," +
                "(2, 3, 4, 'Posted', 4.5, 'Grace provided excellent care for Whiskers.')," +
                "(2, 4, 3, 'Posted', 4.0, 'Liam is a responsible owner.')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS pets");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS posts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS reviews");
        onCreate(sqLiteDatabase);
    }

    public boolean insertPet(Pet pet) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", pet.getName());
        contentValues.put("age", pet.getAge());
        contentValues.put("type", pet.getType());
        contentValues.put("breed", pet.getBreed());
        contentValues.put("sex", pet.getSex());
        contentValues.put("color", pet.getColor());
        contentValues.put("note", pet.getNote());
        contentValues.put("userid", pet.getUserid());
        long insertToPet = sqLiteDatabase.insert("pets", null, contentValues);


        if (insertToPet == -1) {
            return false;
        } else {
            Log.e(TAG, "insert data :" + insertToPet);
            return true;
        }

    }

    public List<Pet> getAllPets() {
        List<Pet> returnList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM pets", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String petName = cursor.getString(1);
                int petAge = cursor.getInt(2);
                String petType = cursor.getString(3);
                String petBreed = cursor.getString(4);
                String petSex = cursor.getString(5);
                String petColor = cursor.getString(6);
                String petNote = cursor.getString(7);
                int petUserid = cursor.getInt(8);

                Pet pet = new Pet(id, petName, petAge, petType, petBreed, petSex, petColor, petNote, petUserid);
                returnList.add(pet);
            } while (cursor.moveToNext());
        } else {
            Log.e(TAG, "data retrieval failed :");
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public List<Pet> getMyPets(int userid) {
        List<Pet> returnList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM pets WHERE userid =" + userid, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String petName = cursor.getString(1);
                int petAge = cursor.getInt(2);
                String petType = cursor.getString(3);
                String petBreed = cursor.getString(4);
                String petSex = cursor.getString(5);
                String petColor = cursor.getString(6);
                String petNote = cursor.getString(7);
                int petUserid = cursor.getInt(8);

                Pet pet = new Pet(id, petName, petAge, petType, petBreed, petSex, petColor, petNote, petUserid);
                returnList.add(pet);
            } while (cursor.moveToNext());
        } else {
            Log.e(TAG, "data retrieval failed :");
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public boolean deletePet(Pet pet) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long deleteFromPet = sqLiteDatabase.delete("pets", "id = " + pet.getId(), null);
        return deleteFromPet != -1;
    }

    public boolean updatePet(Pet pet) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", pet.getName());
        contentValues.put("age", pet.getAge());
        contentValues.put("type", pet.getType());
        contentValues.put("breed", pet.getBreed());
        contentValues.put("sex", pet.getSex());
        contentValues.put("color", pet.getColor());
        contentValues.put("note", pet.getNote());
        contentValues.put("userid", pet.getUserid());
        long updatePet = sqLiteDatabase.update("pets", contentValues, "id = " + pet.getId(), null);

        if (updatePet == -1) {
            return false;
        }

        Log.e(TAG, "update data :" + updatePet);
        return true;

    }

    public boolean insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("phone", user.getPhone());
        contentValues.put("address", user.getAddress());
        contentValues.put("type", user.getType());
        contentValues.put("password", user.getPassword());
        long insertToUser = sqLiteDatabase.insert("users", null, contentValues);

        if (insertToUser == -1) {
            return false;
        } else {
            Log.e(TAG, "insert data :" + insertToUser);
            return true;
        }

    }

    public List<User> getAllUsers() {
        List<User> returnList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String userName = cursor.getString(1);
                String userEmail = cursor.getString(2);
                String userPhone = cursor.getString(3);
                String userAddress = cursor.getString(4);
                String userType = cursor.getString(5);
                String userPassword = cursor.getString(6);

                User user = new User(id, userName, userEmail, userPhone, userAddress, userType, userPassword);
                returnList.add(user);
            } while (cursor.moveToNext());
        } else {
            Log.e(TAG, "data retrieval failed :");
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long deleteFromUser = sqLiteDatabase.delete("users", "id = " + user.getId(), null);

        return deleteFromUser != -1;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("phone", user.getPhone());
        contentValues.put("address", user.getAddress());
        contentValues.put("type", user.getType());
        contentValues.put("password", user.getPassword());
        long updateUser = sqLiteDatabase.update("users", contentValues, "id = " + user.getId(), null);

        if (updateUser == -1) {
            return false;
        } else {
            Log.e(TAG, "update data :" + updateUser);
            return true;
        }
    }

    public boolean insertPost(Post post) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ownerid", post.getOwnerId());
        contentValues.put("caretakerid", post.getCaretakerId());
        contentValues.put("petid", post.getPetId());
        contentValues.put("phone", post.getPhone());
        contentValues.put("location", post.getLocation());
        contentValues.put("services", post.getServices());
        contentValues.put("status", post.getStatus());
        contentValues.put("startDate", post.getStartDate());
        contentValues.put("endDate", post.getEndDate());
        long insertToPost = sqLiteDatabase.insert("posts", null, contentValues);

        if (insertToPost == -1) {
            return false;
        } else {
            Log.e(TAG, "insert data :" + insertToPost);
            return true;
        }

    }

    public List<Post> getAllPosts() {
        List<Post> returnList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM posts WHERE status != 'Completed'", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int ownerId = cursor.getInt(1);
                int caretakerId = cursor.getInt(2);
                int petId = cursor.getInt(3);
                String postPhone = cursor.getString(4);
                String postLocation = cursor.getString(5);
                String postServices = cursor.getString(6);
                String postStatus = cursor.getString(7);
                String postStartDate = cursor.getString(8);
                String postEndDate = cursor.getString(9);


                Post post = new Post(id, ownerId, caretakerId, petId, postPhone, postLocation, postServices, postStatus, postStartDate, postEndDate);
                returnList.add(post);
            } while (cursor.moveToNext());
        } else {
            Log.e(TAG, "data retrieval failed :");
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public List<Post> getMyActivePosts(int userid) {
        List<Post> returnList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM posts WHERE status != 'Completed' AND ownerid = " + userid, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int ownerId = cursor.getInt(1);
                int caretakerId = cursor.getInt(2);
                int petId = cursor.getInt(3);
                String postPhone = cursor.getString(4);
                String postLocation = cursor.getString(5);
                String postServices = cursor.getString(6);
                String postStatus = cursor.getString(7);
                String postStartDate = cursor.getString(8);
                String postEndDate = cursor.getString(9);

                Post post = new Post(id, ownerId, caretakerId, petId, postPhone, postLocation, postServices, postStatus, postStartDate, postEndDate);
                returnList.add(post);
            } while (cursor.moveToNext());
        } else {
            Log.e(TAG, "data retrieval failed :");
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public boolean deletePost(Post post) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long deleteFromPost = sqLiteDatabase.delete("posts", "id = " + post.getId(), null);

        return deleteFromPost != -1;
    }

    public boolean updatePost(Post post) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ownerid", post.getOwnerId());
        contentValues.put("caretakerid", post.getOwnerId());
        contentValues.put("petid", post.getPetId());
        contentValues.put("phone", post.getPhone());
        contentValues.put("location", post.getLocation());
        contentValues.put("services", post.getServices());
        contentValues.put("status", post.getStatus());
        contentValues.put("startDate", post.getStartDate());
        contentValues.put("endDate", post.getEndDate());

        long updatePost = sqLiteDatabase.update("posts", contentValues, "id = " + post.getId(), null);

        if (updatePost == -1) {
            return false;
        } else {
            Log.e(TAG, "update data :" + updatePost);
            return true;
        }
    }

    public boolean insertReview(Review review) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("postid", review.getPostId());
        contentValues.put("forid", review.getForId());
        contentValues.put("fromid", review.getFromId());
        contentValues.put("status", review.getStatus());
        contentValues.put("rating", review.getRating());
        contentValues.put("review", review.getReview());
        long insertToReview = sqLiteDatabase.insert("reviews", null, contentValues);

        if (insertToReview == -1) {
            return false;
        } else {
            Log.e(TAG, "insert data :" + insertToReview);
            return true;
        }
    }

    public List<Review> getPendingReviews(int userid) {
        List<Review> returnList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM reviews WHERE status = 'Pending' AND fromid = " + userid, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int postId = cursor.getInt(1);
                int forId = cursor.getInt(2);
                int fromId = cursor.getInt(3);
                String status = cursor.getString(4);
                Double reviewRating = cursor.getDouble(5);
                String reviewComment = cursor.getString(6);

                Review review = new Review(id, postId, forId, fromId, status, reviewRating, reviewComment);
                returnList.add(review);
            } while (cursor.moveToNext());
        } else {
            Log.e(TAG, "data retrieval failed :");
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public List<Review> getReviews(int userid) {
        List<Review> returnList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM reviews WHERE status != 'Pending' AND forid = " + userid, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int postId = cursor.getInt(1);
                int forId = cursor.getInt(2);
                int fromId = cursor.getInt(3);
                String status = cursor.getString(4);
                Double reviewRating = cursor.getDouble(5);
                String reviewComment = cursor.getString(6);

                Review review = new Review(id, postId, forId, fromId, status, reviewRating, reviewComment);
                returnList.add(review);
            } while (cursor.moveToNext());
        } else {
            Log.e(TAG, "data retrieval failed :");
        }
        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public boolean updateReview(Review review) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("postid", review.getPostId());
        contentValues.put("forid", review.getForId());
        contentValues.put("fromid", review.getFromId());
        contentValues.put("status", review.getStatus());
        contentValues.put("rating", review.getRating());
        contentValues.put("review", review.getReview());

        long updateReview = sqLiteDatabase.update("reviews", contentValues, "id = " + review.getId(), null);

        if (updateReview == -1) {
            return false;
        } else {
            Log.e(TAG, "update data :" + updateReview);
            return true;
        }
    }


}
