package com.example.androidproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.Post;

public class FormActivity extends Fragment implements OnMapReadyCallback {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button calendarButton;
    Button timeButton;
    Button form_submit;
    TextView show_selected_date, show_time_date;
    TextInputLayout subjectNameI, locationNameI;

    private FirebaseAuth mAuth;

    private GoogleMap mMap;
    private Marker mMarker;
    LatLng mLatLng = new LatLng(0, 0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_form, container, false);
//        setContentView(R.layout.activity_form);

        mAuth = FirebaseAuth.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.post_map);
        mapFragment.getMapAsync(this);


        subjectNameI = view.findViewById(R.id.sn);
        locationNameI = view.findViewById(R.id.ln);
        show_selected_date = view.findViewById(R.id.show_selected_date);
        show_time_date = view.findViewById(R.id.show_time_date);
        show_selected_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        calendarButton = view.findViewById(R.id.pick_date_button);
        timeButton = view.findViewById(R.id.pick_time_button);
        form_submit = view.findViewById(R.id.form_submit);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(0)
                        .setMinute(0)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                        .setTitleText("Select Appointment time")
                        .build();
                timePicker.show(getFragmentManager(), "DayPicker");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hours = timePicker.getHour();
                        int minutes = timePicker.getMinute();
                        String hs = "00";
                        String ms = "00";
                        if (hours < 10) {
                            hs = "0" + Integer.toString(hours);
                        } else {
                            hs = Integer.toString(hours);
                        }
                        if (minutes < 10) {
                            ms = "0" + Integer.toString(minutes);
                        } else {
                            ms = Integer.toString(minutes);
                        }
                        show_time_date.setText(hs + ":" + ms);
                    }
                });
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker datePicker =
                        MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select date")
                                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                                .build();
                datePicker.show(getFragmentManager(), "DayPicker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        show_selected_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date((Long) selection)));
                    }
                });

            }
        });
        form_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectName = String.valueOf(subjectNameI.getEditText().getText());
                String locationName = String.valueOf(locationNameI.getEditText().getText());
                String date = String.valueOf(show_selected_date.getText());
                String time = String.valueOf(show_time_date.getText());
                LatLng location = mLatLng;
                String userId = mAuth.getCurrentUser().getUid();


                db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
//                                Log.d(TAG, "DocumentSnapshot data: " + document.getString("fullName") );
                                Post newPost = new Post(userId, subjectName, locationName, date, time, location.latitude, location.longitude, document.getString("fullName"));
                                db.collection("posts")
                                        .add(newPost)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                //Navigate to home page


//                                                Intent loginIntent = new Intent(FormActivity.this, DashboardActivity.class);
//                                                startActivity(loginIntent);
//                                                finish();

                                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsListFragment()).commit();


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


            }
        });


        ScrollView mainScrollView = (ScrollView) view.findViewById(R.id.main_scrollview);
        ImageView transparentImageView = (ImageView) view.findViewById(R.id.transparent_image);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }

            }
        });


        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Remove any existing markers
                if (mMarker != null) {
                    mMarker.remove();
                }

                // Add a new marker at the clicked location
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng));

                // Save the marker coordinates in a variable
                mLatLng = latLng;

            }
        });
    }
}