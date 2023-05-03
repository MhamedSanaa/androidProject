package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import adapter.PostAdapter;
import models.Post;

public class PostsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FloatingActionButton floating_action_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);
        recyclerView = view.findViewById(R.id.posts_list_recycler_view);
        floating_action_button=view.findViewById(R.id.floating_action_button);
        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FormActivity.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        posts.add(new Post("6s5df46sd65f","math", "1600 Amphitheatre Parkway, Mountain View, CA 94043","20/10/2023","12:00","adresse","John Doe"));
        posts.add(new Post("9sd8g89ggnjk","math", "1600 Amphitheatre Parkway, Mountain View, CA 94043","20/10/2023","12:00","adresse","John Doe"));
        posts.add(new Post("sd6f54s6d5f4","math", "1600 Amphitheatre Parkway, Mountain View, CA 94043","20/10/2023","12:00","adresse","John Doe"));


        // Set adapter
        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}