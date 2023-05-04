package com.example.androidproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyFragment extends Fragment implements OnMapReadyCallback {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mMap;
    private Marker mMarker;
    LatLng mLatLng = new LatLng(0, 0);

    Button calendarButton;
    Button timeButton;
    Button form_submit;
    TextView textViewToChange;
    TextView show_selected_date, show_time_date;
    TextInputLayout subjectNameI, locationNameI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //get views


        View view = inflater.inflate(R.layout.activity_form, container, false);
        textViewToChange = (TextView) view.findViewById(R.id.show_selected_date);
        //textViewToChange.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        calendarButton = view.findViewById(R.id.pick_date_button);
        timeButton = view.findViewById(R.id.pick_time_button);
        form_submit = view.findViewById(R.id.form_submit);
        show_selected_date = (TextView) view.findViewById(R.id.show_selected_date);
        show_time_date = (TextView) view.findViewById(R.id.show_time_date);
        subjectNameI = (TextInputLayout) view.findViewById(R.id.sn);
        locationNameI = (TextInputLayout) view.findViewById(R.id.ln);




        //take data from API



        //Set Data




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
