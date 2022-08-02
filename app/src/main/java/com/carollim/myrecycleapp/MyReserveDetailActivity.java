package com.carollim.myrecycleapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyReserveDetailActivity extends AppCompatActivity {

    TextView txtStatus, txtSelectedAddress,txtSelectedCategory,
            txtSelectedDate,txtSelectedTime, txtCollector;
    //Button button;

    SupportMapFragment supportMapFragment;
    GoogleMap map;
    Marker marker;
    List<Marker> markers = new ArrayList<Marker>();
    private FirebaseAuth fAuth;
    private final int REQUEST_CODE = 111;
    public static final String TAG = "MyReserveDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reserve_detail);

        txtStatus = findViewById(R.id.txtStatus);
        txtSelectedAddress = findViewById(R.id.txtSelectedAddress);
        txtSelectedCategory = findViewById(R.id.txtSelectedCategory);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtSelectedTime = findViewById(R.id.txtSelectedTime);
        txtCollector = findViewById(R.id.txtCollector);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String addr = extras.getString("address");
        txtSelectedAddress.setText(addr);
        String status = extras.getString("status");
        txtStatus.setText(status);
        txtSelectedCategory.setText(extras.getString("category"));
        txtSelectedDate.setText(extras.getString("date"));
        txtSelectedTime.setText(extras.getString("time"));
        txtCollector.setText(extras.getString("collector"));

        //button = findViewById(R.id.buttonCancelReserve);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAddr);
        removeAllMarkers();

        List<Address>addressList = null;
        Geocoder geocoder = new Geocoder(MyReserveDetailActivity.this);
        try {
            addressList = geocoder.getFromLocationName(addr, 1);
        }catch (IOException e){
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(addr);

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                marker = map.addMarker(markerOptions);
                marker.showInfoWindow();
                markers.add(marker);
            }
        });

    }

    // remove marker
    private void removeAllMarkers() {
        for (Marker marker: markers) {
            marker.remove();
        }
        markers.clear();

    }
}