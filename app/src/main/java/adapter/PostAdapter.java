package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private List<Post> posts;
    private Context context;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_view, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        Post post = posts.get(position);
        Log.d( "onBindViewHolder: ---------------",post.UserId);
        holder.userIdTextView.setText(post.UserId);
        holder.addressTextView.setText(post.address);
        holder.userTextView.setText(post.user);
        holder.subjectTextView.setText(post.subject);

        holder.mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Set the address location on the map
                LatLng addressLocation = new LatLng(post.latitude, post.longitude);
                googleMap.addMarker(new MarkerOptions().position(addressLocation).title("Marker"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressLocation, 15));
                Log.d("lets see if other maps work-------------------------", googleMap.getCameraPosition().target.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView userIdTextView;
        private TextView addressTextView;
        private TextView userTextView;
        private TextView subjectTextView;
        private FrameLayout mapFrame;
        private SupportMapFragment mapFragment;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userId);
            userTextView = itemView.findViewById(R.id.user);
            subjectTextView = itemView.findViewById(R.id.subject);
            addressTextView = itemView.findViewById(R.id.address);

            // Create a new instance of the Google Map fragment
            mapFrame = itemView.findViewById(R.id.map_frame);

            // Create a new instance of the Google Map fragment
            mapFragment = new SupportMapFragment();

            // Add the fragment to the FrameLayout using the FragmentManager
            ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager().beginTransaction().add(mapFrame.getId(), mapFragment).commit();
        }
    }
}
