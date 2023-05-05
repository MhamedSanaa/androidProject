package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import adapter.PostAdapter;
import models.Post;
import models.User;

public class PostsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<QueryDocumentSnapshot> posts = new ArrayList<>();

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
//                Intent intent = new Intent(getContext(), FormActivity.class);
//                startActivity(intent);


                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new FormActivity()).addToBackStack(null).commit();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        // Set adapter
        postAdapter = new PostAdapter(getContext(),posts);
        recyclerView.setAdapter(postAdapter);
        getPosts();
        // Inflate the layout for this fragment
        return view;
    }
    public void getPosts(){
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            posts.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                posts.add(document);
                                Log.d("posts list fragement////////////////", document.getId() + " => " + document.getData());
                            }
                            postAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting documents: /////////////////////", task.getException());
                        }
                    }
                });
    }
}