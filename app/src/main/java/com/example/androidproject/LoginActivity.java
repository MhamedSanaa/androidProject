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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import models.User;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn_signup, btn_login;
    TextInputEditText email, password;
    private FirebaseAuth mAuth;

    public void getViewIds() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(loginIntent);
            finish();
        }
//        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
//        String UserId = sessionManagement.getSession();
//        if (!UserId.equals("NaN")){
//            Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
//            startActivity(loginIntent);
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
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
                String semail, spassword;
                semail = String.valueOf(email.getText());
                spassword = String.valueOf(password.getText());
                Log.i("email", String.valueOf(semail));

                mAuth.signInWithEmailAndPassword(semail, spassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Log.d("signInWithEmail:success", user.getUid());
                                    Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(loginIntent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

//                db.collection("users")
//                        .whereEqualTo("email", semail)
//                        .whereEqualTo("password", spassword)
//                        .get()
//                        .addOnCompleteListener(
//                                new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            QuerySnapshot querySnapshot = task.getResult();
//                                            Log.d("Login Query snapshot", querySnapshot.toString());
//                                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                                    User user = document.toObject(User.class);
//                                                    SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
//                                                    sessionManagement.saveSession(user,document.getId());
//                                                    Log.d("User as raw", document.getId() + " => " + document.getData());
//                                                    Log.d("User as java object", user.email.toString());
//                                                }
//                                                Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
//                                                startActivity(loginIntent);
//                                                finish();
//                                            } else {
//                                                Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
//                                            }
//                                        } else {
//                                            Log.e("Task error", "Error getting documents: ", task.getException());
//                                        }
//                                    }
//                                });
            }
        });
    }
}