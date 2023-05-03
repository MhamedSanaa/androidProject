package com.example.androidproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.Post;

public class FormActivity extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button calendarButton;
    Button timeButton;
    Button form_submit;
    TextView textViewToChange;
    TextView show_selected_date, show_time_date;
    TextInputLayout subjectNameI, locationNameI;


    private GoogleMap mMap;
    private Marker mMarker;
    LatLng mLatLng =new LatLng(0,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.post_map);
        mapFragment.getMapAsync(this);



        textViewToChange = (TextView) findViewById(R.id.show_selected_date);
        textViewToChange.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        calendarButton = findViewById(R.id.pick_date_button);
        timeButton = findViewById(R.id.pick_time_button);
        form_submit=findViewById(R.id.form_submit);

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
                timePicker.show(getSupportFragmentManager(), "DayPicker");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hours = timePicker.getHour();
                        int minutes = timePicker.getMinute();
                        String hs = "00";
                        String ms = "00";
                        show_time_date = (TextView) findViewById(R.id.show_time_date);
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
                datePicker.show(getSupportFragmentManager(), "DayPicker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        textViewToChange = (TextView) findViewById(R.id.show_selected_date);
                        textViewToChange.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date((Long) selection)));
                    }
                });

            }
        });
        form_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectNameI =(TextInputLayout) findViewById(R.id.sn);
                locationNameI =(TextInputLayout) findViewById(R.id.ln);
                show_selected_date = (TextView) findViewById(R.id.show_selected_date);
                show_time_date =(TextView) findViewById(R.id.show_time_date);
                String subjectName=String.valueOf(subjectNameI.getEditText().getText());
                String locationName=String.valueOf(locationNameI.getEditText().getText());
                String date=String.valueOf(show_selected_date.getText());
                String time=String.valueOf(show_time_date.getText());
                String location=mLatLng.toString();
                Post newPost=new Post("156156165",subjectName,locationName,date,time,location,"default user");
                db.collection("posts")
                        .add(newPost)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                //Navigate to home page

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });


        ScrollView mainScrollView = (ScrollView) findViewById(R.id.main_scrollview);
        ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

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