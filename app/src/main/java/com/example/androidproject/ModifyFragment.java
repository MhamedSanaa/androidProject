package com.example.androidproject;

import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Post;

public class ModifyFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mMap;
    private Marker mMarker;
    LatLng mLatLng = new LatLng(0, 0);
    String postId="";
    Button calendarButton;
    Button timeButton;
    Button form_submit;

    TextView show_selected_date, show_time_date;
    TextInputLayout subjectNameI, locationNameI;

    Double latitude=0d;
    Double longitude=0d;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //get views
        View view = inflater.inflate(R.layout.activity_form, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager ().findFragmentById(R.id.post_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                Bundle bundle = getArguments();
                mMap = googleMap;
                LatLng defaultLocation = new LatLng(Double.parseDouble(bundle.getString("latitude")),Double.parseDouble(bundle.getString("longitude")));
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(defaultLocation)
                        .title("Old location");
                mMap.addMarker(markerOptions);

                // Set the map camera to the default location
             googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 8));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // Remove any existing markers
                        mMap.clear();
                        if (mMarker != null) {
                            mMarker.remove();
                        }

                        // Add a new marker at the clicked location
                        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("new location"));

                        // Save the marker coordinates in a variable
                        mLatLng = latLng;
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;

                    }
                });
            }
        });


        show_time_date = (TextView) view.findViewById(R.id.show_time_date);
        show_selected_date = (TextView) view.findViewById(R.id.show_selected_date);

        timeButton = view.findViewById(R.id.pick_time_button);
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
                timePicker.show(getFragmentManager (), "DayPicker");
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

        calendarButton = view.findViewById(R.id.pick_date_button);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker datePicker =
                        MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select date")
                                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                                .build();
                datePicker.show(getFragmentManager (), "DayPicker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        show_selected_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date((Long) selection)));
                    }
                });

            }
        });

        form_submit = view.findViewById(R.id.form_submit);
        show_selected_date = (TextView) view.findViewById(R.id.show_selected_date);
        show_time_date = (TextView) view.findViewById(R.id.show_time_date);
        subjectNameI = (TextInputLayout) view.findViewById(R.id.sn);
        locationNameI = (TextInputLayout) view.findViewById(R.id.ln);
        LatLng location = mLatLng;







        //take data from API
        Bundle bundle = getArguments();

        String subject="";
        String date="";
        String time="";
        String address="";


        if (bundle != null) {
            // Get the string value from the Bundle using the key

            postId = bundle.getString("postId");
            subject= bundle.getString("subject");
            date= bundle.getString("date");
            time= bundle.getString("time");
            address= bundle.getString("address");
            latitude= Double.parseDouble(bundle.getString("latitude"));
            longitude= Double.parseDouble(bundle.getString("longitude"));

            // Use the string value as needed
            Log.d("ReceivingFragment", date);
        }
        form_submit.setText("Update");
        form_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("subject",subjectNameI.getEditText().getText().toString());
                updates.put("address",locationNameI.getEditText().getText().toString());
                updates.put("date",show_selected_date.getText().toString());
                updates.put("time",show_time_date.getText().toString());
                updates.put("latitude",latitude);
                updates.put("longitude",longitude);
                db.collection("posts")
                        .document(postId)
                        .update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText( getContext(), "post Updated", Toast.LENGTH_SHORT).show();

                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getContext(), "Couldn't update post", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        //Set Data
        show_selected_date.setText(date);


        show_time_date.setText(time);
        locationNameI.getEditText().setText(address);
        subjectNameI.getEditText().setText(subject);

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
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        Bundle bundle = getArguments();
//        mMap = googleMap;
//        LatLng defaultLocation = new LatLng(Double.parseDouble(bundle.getString("latitude")),Double.parseDouble(bundle.getString("longitude")));
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(defaultLocation)
//                .title("Default Marker");
//        mMap.addMarker(markerOptions);
//
//        // Set the map camera to the default location
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
//
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                // Remove any existing markers
//                if (mMarker != null) {
//                    mMarker.remove();
//                }
//
//                // Add a new marker at the clicked location
//                mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
//
//                // Save the marker coordinates in a variable
//                mLatLng = latLng;
//
//            }
//        });
//    }
}
