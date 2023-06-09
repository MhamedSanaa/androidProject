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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

import models.User;

public class SignupActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    User newUser;
    TextInputEditText email, password, fullname, confirmpassword, username;
    Button buttonreg, accountExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getViewIds();
        mAuth = FirebaseAuth.getInstance();
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
                    String message =
                            "enter valid email\n" ;
                    email.setError(message);
                    email.requestFocus();
                }
                else if (!isValidUsername(susername)){
                    String message =
                            "enter valid username : \n"+"8 digit" ;
                    username.setError(message);
                    username.requestFocus();
                }
                else if (!isValidPwd(spassword)){
                    String message =
                            "enter valid password : \n"+"1 lower, 1 upper, 8 digit" ;
                    password.setError(message);
                    password.requestFocus();
                }
                else if (!isPasswordMatch(spassword,sconfirmpassword)){
                    String message = "passwords doesn t match";
                    confirmpassword.setError(message);
                    confirmpassword.requestFocus();
                }
                else
                if(!isValidEmail(semail))
                { Toast.makeText(SignupActivity.this, "email required", Toast.LENGTH_LONG).show();

                }


                else
                {
                    mAuth.createUserWithEmailAndPassword(semail, spassword)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("createUserWithEmail:success",task.getResult().getUser().getUid());
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        newUser = new User(sfullname, susername, semail, spassword);
                                        db.collection("users")
                                                .document(user.getUid())
                                                .set(newUser)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
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
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        fullname = findViewById(R.id.signup_fullname);
        confirmpassword = findViewById(R.id.signup_confirmpassword);
        username = findViewById(R.id.signup_username);
        buttonreg = findViewById(R.id.signup_btn_register);
        accountExist = findViewById(R.id.signup_accountExist);
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