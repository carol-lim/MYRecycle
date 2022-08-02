package com.carollim.myrecycleapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {
    TextView txtStatus, txtSelectedAddress,txtSelectedCategory,
            txtSelectedDate,txtSelectedTime, txtCollector, txtClient;
    Button buttonAcceptCollect;

    SupportMapFragment supportMapFragment;
    GoogleMap map;
    Marker marker;
    List<Marker> markers = new ArrayList<Marker>();
    private FirebaseAuth fAuth;
    private final int REQUEST_CODE = 111;
    public static final String TAG = "ReservationActivity";
    private DatabaseReference ref1, ref2;
    private ArrayList<UserHelperClass> list;
    private String fullName,ccp,phoneNum, rId,addr, col;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        txtStatus = findViewById(R.id.txtStatus);
        txtSelectedAddress = findViewById(R.id.txtSelectedAddress);
        txtSelectedCategory = findViewById(R.id.txtSelectedCategory);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtSelectedTime = findViewById(R.id.txtSelectedTime);
        txtCollector = findViewById(R.id.txtCollector);
        txtStatus = findViewById(R.id.txtStatus);
        txtClient = findViewById(R.id.txtClient);
        buttonAcceptCollect = findViewById(R.id.buttonAcceptCollect);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        rId = extras.getString("rId");
        addr = extras.getString("address");
        txtSelectedAddress.setText(addr);
        String status = extras.getString("status");
        txtStatus.setText(status);
        txtSelectedCategory.setText(extras.getString("category"));
        txtSelectedDate.setText(extras.getString("date"));
        txtSelectedTime.setText(extras.getString("time"));
        txtCollector.setText(extras.getString("collector"));

        String user = extras.getString("user");

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapReserved);
        removeAllMarkers();

        List<Address>addressList = null;
        Geocoder geocoder = new Geocoder(ReservationActivity.this);
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

        ref1 = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("user").child(user);
//        list = new ArrayList<>();
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    UserHelperClass userHelperClass = new UserHelperClass();
                userHelperClass.setName(dataSnapshot.child("name").getValue().toString());
                userHelperClass.setCountryCode(dataSnapshot.child("countryCode").getValue().toString());
                userHelperClass.setPhNum(dataSnapshot.child("phNum").getValue().toString());
                list.add(userHelperClass);*/
                UserHelperClass userHelperClass = snapshot.getValue(UserHelperClass.class);
                if (userHelperClass!= null){
                    txtClient.setText(userHelperClass.name+" (+"+userHelperClass.countryCode+userHelperClass.phNum+")");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // control the button change accept to collect

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();

        getCollector(firebaseUser);

        if (status.equals("Submitted")){
            // accept: update data status collector, refresh
            buttonAcceptCollect.setText("Accept");
            buttonAcceptCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.db));
                    DatabaseReference ref2 = database.getReference("reservation");

                    ref2.child(rId).child("status").setValue("Accepted");
                    ref2.child(rId).child("collectorId").setValue(col);
                    Toast.makeText(ReservationActivity.this, "Request accepted.", Toast.LENGTH_SHORT).show();

                    finish();
                    Intent intent = new Intent(ReservationActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            });

        } else if (status.equals("Accepted")){
            // collect: start collectionActivity
            buttonAcceptCollect.setText("Collect");
            buttonAcceptCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReservationActivity.this,CollectionActivity.class);
                    startActivity(intent);
                }
            });

        }

    }
    private void getCollector(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        ref2 = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("user").child(userID);
//        list = new ArrayList<>();
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userHelperClass = snapshot.getValue(UserHelperClass.class);
                if (userHelperClass!= null){
                   col = userHelperClass.name;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*DatabaseReference refProfile = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("user");
        refProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userHelperClass = snapshot.getValue(UserHelperClass.class);
                if (userHelperClass!= null){
                    fullName = userHelperClass.name;
                    ccp= userHelperClass.countryCode;
                    phoneNum= userHelperClass.phNum;

                }else {
                    Toast.makeText(ReservationActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReservationActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    // remove marker
    private void removeAllMarkers() {
        for (Marker marker: markers) {
            marker.remove();
        }
        markers.clear();

    }
}