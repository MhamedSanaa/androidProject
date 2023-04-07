package com.example.androidproject;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.User;

public class SignupActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;
    User newUser;
    TextInputEditText email,password,fullname,confirmpassword,username;
    Button buttonreg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        fullname=findViewById(R.id.fullname);
        confirmpassword=findViewById(R.id.confirmpassword);
        username=findViewById(R.id.username);
        buttonreg=findViewById(R.id.btn_register);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String semail,spassword,sfullname,sconfirmpassword,susername;
                semail=String.valueOf(email.getText());
                spassword=String.valueOf(password.getText());
                sfullname=String.valueOf(fullname.getText());
                sconfirmpassword=String.valueOf(confirmpassword.getText());
                susername=String.valueOf(username.getText());
                Log.i("message",semail);
                if(TextUtils.isEmpty(sfullname)){
                    Toast.makeText(SignupActivity.this,"Enter Full name",Toast.LENGTH_LONG).show();
                    return;}
                if(TextUtils.isEmpty(semail)){
                    Toast.makeText(SignupActivity.this,"Enter email",Toast.LENGTH_LONG).show();
                    return;}
                if(TextUtils.isEmpty(susername)){
                    Toast.makeText(SignupActivity.this,"Enter username",Toast.LENGTH_LONG).show();
                    return;}
                if(TextUtils.isEmpty(spassword)){
                    Toast.makeText(SignupActivity.this,"Enter password",Toast.LENGTH_LONG).show();
                    return;}

                if(TextUtils.isEmpty(sconfirmpassword)){
                    Toast.makeText(SignupActivity.this,"Confirm password please",Toast.LENGTH_LONG).show();
                    return;}

                if(!spassword.equals(sconfirmpassword))
                {
                    Toast.makeText(SignupActivity.this,"Confirm Password does not match the password",Toast.LENGTH_LONG).show();
                }
                newUser=new User(sfullname,susername,semail,spassword);
                databaseReference.setValue(newUser);

                /*mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });*/

            }
        });
    }
}