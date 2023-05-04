package com.example.androidproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import adapter.PostAdapter;
import models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;
    String name;
    String username;
    int number_posts;

    private EditText profile_fragment_name;
    private TextView profile_fragment_email;
    private TextView profile_fragment_nbposts;
    private EditText profile_fragment_username;
    private ExtendedFloatingActionButton floating_action_button;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_fragment_name = view.findViewById(R.id.profile_fragment_name);
        profile_fragment_email = view.findViewById(R.id.profile_fragment_email);
        profile_fragment_username = view.findViewById(R.id.profile_fragment_username);
        profile_fragment_nbposts = view.findViewById(R.id.profile_fragment_nbposts);
        floating_action_button = view.findViewById(R.id.profile_fragment_floating_button);
        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("username",profile_fragment_username.getText().toString());
                updates.put("fullName",profile_fragment_name.getText().toString());
                db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText( getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getContext(), "Couldn't update profile", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        getUserInfo();

        return view;
    }

    public void getUserInfo() {
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        profile_fragment_email.setText(email);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d( "onComplete: PROFILE FRAGEMENT",task.getResult().getData().toString());
                    name = task.getResult().toObject(User.class).fullName.toString();
                    username = task.getResult().toObject(User.class).username.toString();
                    profile_fragment_name.setText(name);
                    profile_fragment_username.setText(username);
                    Log.d( "onComplete: PROFILE FRAGEMENT",name);
                }
            }
        });
        db.collection("posts").whereEqualTo("UserId", FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("onComplete: Profile fragement liste of posts", Integer.toString(task.getResult().getDocuments().size()));
                number_posts = task.getResult().size();
                profile_fragment_nbposts.setText(Integer.toString(number_posts));
            }
        });
    }
}