package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import models.User;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn_signup, btn_login;
    TextInputEditText username, password;

    public void getViewIds() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        String UserId = sessionManagement.getSession();
        if (!UserId.equals("NaN")){
            Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getViewIds();


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String susername, spassword;
                susername = String.valueOf(username.getText());
                spassword = String.valueOf(password.getText());
                Log.i("username", String.valueOf(susername));
                db.collection("users")
                        .whereEqualTo("username", susername)
                        .whereEqualTo("password", spassword)
                        .get()
                        .addOnCompleteListener(
                                new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot querySnapshot = task.getResult();
                                            Log.d("Login Query snapshot", querySnapshot.toString());
                                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    User user = document.toObject(User.class);
                                                    SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                                                    sessionManagement.saveSession(user,document.getId());
                                                    Log.d("User as raw", document.getId() + " => " + document.getData());
                                                    Log.d("User as java object", user.email.toString());
                                                }
                                                Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                                startActivity(loginIntent);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Log.e("Task error", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
            }
        });
    }
}