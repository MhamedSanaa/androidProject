package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.ListFragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ;
    private FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    MenuItem logout, account, myposts, navhome, addnewpost;
    TextView nav_header_textView_name, nav_header_textView_email;

    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.naview);
        toolbar = findViewById(R.id.topAppBar);


        NavigationView navigationView = findViewById(R.id.naview);
        Menu menu = navigationView.getMenu();
        nav_header_textView_name = navigationView.findViewById(R.id.nav_header_textView_name);
        nav_header_textView_email = navigationView.findViewById(R.id.nav_header_textView_email);
        logout = menu.findItem(R.id.logout);
        account = menu.findItem(R.id.account);
        myposts = menu.findItem(R.id.myPosts);
        navhome = menu.findItem(R.id.nav_home);
        addnewpost = menu.findItem(R.id.create_new_post);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        myposts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {


                mDrawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyPostsListFragment()).addToBackStack(null).commit();
                return true;
            }
        });
        navhome.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {


                mDrawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsListFragment()).addToBackStack(null).commit();
                return true;
            }
        });
        addnewpost.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {


                mDrawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FormActivity()).addToBackStack(null).commit();
                return true;
            }
        });

        account.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                return true;
            }
        });
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                mDrawerLayout.closeDrawers();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                SessionManagement sessionManagement = new SessionManagement(DashboardActivity.this);
                Log.d("onMenuItemClick: ", sessionManagement.getSession());
                sessionManagement.removeSession();
                Log.d("onMenuItemClick: ", sessionManagement.getSession());
                Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                return true;
            }
        });


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsListFragment()).commit();
            //navigationView.setCheckedItem(R.id.nav_home);
        }
    }
}