package com.example.androidproject;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

import models.User;

public class SignupActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User newUser;
    TextInputEditText email, password, fullname, confirmpassword, username;
    Button buttonreg, accountExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getViewIds();
        buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String semail, spassword, sfullname, sconfirmpassword, susername;
                semail = String.valueOf(email.getText());
                spassword = String.valueOf(password.getText());
                sfullname = String.valueOf(fullname.getText());
                sconfirmpassword = String.valueOf(confirmpassword.getText());
                susername = String.valueOf(username.getText());
                areFieldsEmpty(semail,susername,spassword,sconfirmpassword);
                if (!isValidEmail(semail)){
                    String message = "This is a multiline message.\n" +
                            "enter valid email\n" +
                            "and what does the email must contain ";
                    email.setError(message);
                    email.requestFocus();
                }
                else if (!isValidUsername(susername)){
                    String message = "This is a multiline message.\n" +
                            "enter valid username\n" +
                            "and what does the username must contain ";
                    username.setError(message);
                    username.requestFocus();
                }
                else if (!isValidPwd(spassword)){
                    String message = "This is a multiline message.\n" +
                            "enter valid password\n" +
                            "and what does the password must contain ";
                    password.setError(message);
                    password.requestFocus();
                }
                else if (!isPasswordMatch(spassword,sconfirmpassword)){
                    String message = "passwords doesnt match";
                    confirmpassword.setError(message);
                    confirmpassword.requestFocus();
                }
                else{
                    newUser = new User(sfullname, susername, semail, spassword);
                    db.collection("users")
                            .add(newUser)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }

            }
        });
        accountExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getViewIds(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fullname = findViewById(R.id.fullname);
        confirmpassword = findViewById(R.id.confirmpassword);
        username = findViewById(R.id.username);
        buttonreg = findViewById(R.id.btn_register);
        accountExist = findViewById(R.id.accountExist);
    }
    public boolean isValidEmail(String email){
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }
    public boolean isValidPwd(String password){
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,30}$";
        return Pattern.matches(passwordRegex, password);
    }
    public boolean isValidUsername(String username) {
        String usernameRegex = "^[a-zA-Z0-9_-]{3,20}$";
        return Pattern.matches(usernameRegex, username);
    }
    public boolean isPasswordMatch(String password, String confirmPassword) {
        return confirmPassword.equals(password);
    }

    public void areFieldsEmpty(String semail,String susername,String spassword,String sconfirmpassword){
        if (TextUtils.isEmpty(semail)) {
            email.setError("This field is required");
            email.requestFocus();
            Toast.makeText(SignupActivity.this, "email required", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(susername)) {
            username.setError("This field is required");
            username.requestFocus();
            Toast.makeText(SignupActivity.this, "Enter username", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(spassword)) {
            password.setError("This field is required");
            password.requestFocus();
            Toast.makeText(SignupActivity.this, "Enter password", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(sconfirmpassword)) {
            confirmpassword.setError("This field is required");
            confirmpassword.requestFocus();
            Toast.makeText(SignupActivity.this, "Confirm password please", Toast.LENGTH_LONG).show();
        }
    }
}