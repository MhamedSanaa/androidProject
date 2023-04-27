package com.example.androidproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import adapter.PostAdapter;
import models.Post;

public class DashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private GoogleMap mMap;

    private RecyclerView recyclerView;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.naview);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        posts = new ArrayList<>();
        posts.add(new Post("6s5df46sd65f","math", "1600 Amphitheatre Parkway, Mountain View, CA 94043","John Doe"));
        posts.add(new Post("sd6f54s6d5f4","science", "1 Infinite Loop, Cupertino, CA 95014","Jane Smith"));
        posts.add(new Post("9sd8g89ggnjk","engeneering", "350 Rhode Island St, San Francisco, CA 94103","Bob Johnson"));


        recyclerView = findViewById(R.id.recycler_view);

        // Create a new LinearLayoutManager and set it on the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Create a new adapter and set it on the RecyclerView
        PostAdapter adapter = new PostAdapter(posts);
        Log.d("is there an adapter--------------------------------", Integer.toString(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);
        //FrameLayout mapFrame = findViewById(R.id.map_frame);

        // Create a new instance of the Google Map fragment
//        SupportMapFragment mapFragment = new SupportMapFragment();
//
//        getSupportFragmentManager().beginTransaction().add(mapFrame.getId(), mapFragment).commit();
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                mMap = googleMap;
//
//                // Customize the map as needed, e.g. add markers, polygons, etc.
//                LatLng sydney = new LatLng(-34, 151);
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            }
//        });

    }


}