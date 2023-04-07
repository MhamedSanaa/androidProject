package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.User;

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobile-project-75387-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference();
    Button btn_signup, btn_login;
    TextInputEditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        btn_signup=findViewById(R.id.btn_signup);
        btn_login=findViewById(R.id.btn_login);

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
                String susername,spassword;
                susername= String.valueOf(username.getText());
                spassword=String.valueOf(password.getText());
                Log.i("username", String.valueOf(susername));
                myRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            if(String.valueOf(task.getResult().getValue()).contains(susername) && String.valueOf(task.getResult().getValue()).contains(spassword)){
                                Log.d("contains username", "true");
                                Intent loginIntent=new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(loginIntent);
                                finish();
                            }
                            else Log.e("contains username", "false");
                        }
                    }
                });
            }
        });
    }
}