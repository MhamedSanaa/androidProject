package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.appbar.MaterialToolbar;

public class DashboardActivity extends AppCompatActivity {
//    MaterialToolbar toolbar = findViewById(R.id.topAppBar);
//    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
//            drawerLayout.openDrawer(GravityCompat.START);
//        }
//    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }
}