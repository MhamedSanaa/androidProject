package com.example.androidproject;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Path;

import models.User;

public class SignupActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mobile-project-75387-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference();
    User newUser;
    TextInputEditText email,password,fullname,confirmpassword,username;
    Button buttonreg,accountExist;
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
        accountExist=findViewById(R.id.accountExist);
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
                String id = myRef.push().getKey();
                newUser=new User(id,sfullname,susername,semail,spassword);
                myRef.child("users").push().setValue(newUser);

                Path ssss = myRef.child("users").child("email").getPath();
                Log.i("key", String.valueOf(ssss));
                //myRef.child("users").setValue(newUser);
                //myRef.setValue(newUser);
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
}