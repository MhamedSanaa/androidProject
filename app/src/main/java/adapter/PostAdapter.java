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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

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
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        QueryDocumentSnapshot queryDocumentSnapshot = posts.get(position);
        Post post = queryDocumentSnapshot.toObject(Post.class);
        Log.d( "onBindViewHolder: ---------------",post.UserId);
        holder.userIdTextView.setText(post.UserId);
        holder.addressTextView.setText(post.address);
        holder.userTextView.setText(post.user);
        holder.subjectTextView.setText(post.subject);

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
        private TextView userIdTextView;
        private TextView addressTextView;
        private TextView userTextView;
        private TextView subjectTextView;

        private MapView mapView;
        private GoogleMap googleMap;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userId);
            userTextView = itemView.findViewById(R.id.user);
            subjectTextView = itemView.findViewById(R.id.subject);
            addressTextView = itemView.findViewById(R.id.address);
            mapView = itemView.findViewById(R.id.map_view);

            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
            }
        }
    }
}
