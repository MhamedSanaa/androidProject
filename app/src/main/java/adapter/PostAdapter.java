package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private List<QueryDocumentSnapshot> posts;
    private Context context;

    public PostAdapter(Context context, List<QueryDocumentSnapshot> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_view, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        QueryDocumentSnapshot queryDocumentSnapshot = posts.get(position);

        String idOfPost = queryDocumentSnapshot.getString("UserId");
        if (idOfPost.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.joinButton.setVisibility(View.GONE);
        } else {
            holder.modifyButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }
        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("aaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                db.collection("posts")
                        .document(queryDocumentSnapshot.getId())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                List<String> joinersIds = (List<String>) documentSnapshot.get("joinersIds");
                                if (joinersIds != null && joinersIds.contains(mAuth.getCurrentUser().getUid())) {
                                    Log.w("join ereur", "user already a member");
                                } else {
                                    // Add the user to the member list and update the document
                                    db.collection("posts")
                                            .document(queryDocumentSnapshot.getId())
                                            .update("joinersIds", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()));
                                    Log.w("joining log", "user has joined");
                                }
                            }
                        });
            }
        });

        Post post = queryDocumentSnapshot.toObject(Post.class);
        Log.d("onBindViewHolder: ---------------", post.UserId);
        holder.date.setText(post.date);
        holder.userTextView.setText(post.user);
        holder.subjectTextView.setText(post.subject);
        holder.time.setText(post.time);
        holder.numOfPart.setText(Integer.toString(post.numberOfParticipants));
        holder.post_card_location.setText(post.address);

        holder.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                holder.googleMap = map;
                LatLng addressLocation = new LatLng(post.latitude, post.longitude);
                holder.googleMap.addMarker(new MarkerOptions().position(addressLocation).title("Marker"));
                holder.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressLocation, 8));
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView userTextView;
        private TextView subjectTextView;
        private TextView time;
        private TextView numOfPart;
        private TextView post_card_location;
        private MapView mapView;
        private Button modifyButton;
        private Button joinButton;
        private Button deleteButton;
        private GoogleMap googleMap;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
//            userIdTextView = itemView.findViewById(R.id.userId);
            userTextView = itemView.findViewById(R.id.user);
            subjectTextView = itemView.findViewById(R.id.subject);
            date = itemView.findViewById(R.id.post_card_date);
            mapView = itemView.findViewById(R.id.map_view);
            time = itemView.findViewById(R.id.post_card_time);
            numOfPart = itemView.findViewById(R.id.post_card_participation);
            post_card_location = itemView.findViewById(R.id.post_card_location);
            modifyButton = itemView.findViewById(R.id.post_card_modify);
            joinButton = itemView.findViewById(R.id.post_card_join);
            deleteButton = itemView.findViewById(R.id.post_card_delete);
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
            }
        }
    }
}
