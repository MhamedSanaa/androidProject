package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.User;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn_signup, btn_login;
    TextInputEditText username, password;

    public void getViewIds(){
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        btn_signup=findViewById(R.id.btn_signup);
        btn_login=findViewById(R.id.btn_login);
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
                String susername,spassword;
                susername= String.valueOf(username.getText());
                spassword=String.valueOf(password.getText());
                Log.i("username", String.valueOf(susername));
                db.collection("users")
                        .whereEqualTo("username",susername)
                        .whereEqualTo("password",spassword)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                Log.d("TAG", "onClick: ");
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    Log.d("TAG", "onClick: ");
                                    Intent loginIntent=new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(loginIntent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "there is no user with these credentials", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "No Result", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}