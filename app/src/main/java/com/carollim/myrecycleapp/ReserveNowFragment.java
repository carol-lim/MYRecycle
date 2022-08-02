package com.carollim.myrecycleapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Locale;

public class ReserveNowFragment extends Fragment /*implements OnMapReadyCallback */ {
    Button buttonReserve;
    SearchView searchView;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    GoogleMap map;
    Marker marker;
    List<Marker> markers = new ArrayList<Marker>();
    String searchLocation, userAddress, currentAddress, finalAddress;
    private FirebaseAuth fAuth;
    private final int REQUEST_CODE = 111;
    public static final String TAG = "ReserveNowFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reserve_now, container, false);

        buttonReserve = view.findViewById(R.id.buttonReserve);
        searchView = view.findViewById(R.id.addressMap);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(getContext());

        removeAllMarkers();
        getCurrentLocation();

        // search address
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                removeAllMarkers();

                searchLocation = searchView.getQuery().toString();
                List<Address>addressList = null;

                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(searchLocation, 1);
                }catch (IOException e){
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        map = googleMap;
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(searchLocation);

                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                        marker = map.addMarker(markerOptions);
                        marker.showInfoWindow();
                        markers.add(marker);
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // reserve collector button
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();

        retrieveAddress(firebaseUser);

        buttonReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // assign searched address
                searchLocation = searchView.getQuery().toString();

                // prepare extras
                Bundle extras = new Bundle();
                String[] addrOptions = null;

                if (!TextUtils.isEmpty(searchLocation)){
                    // if user searched address, 3 options
                    addrOptions = new String[]{"Searched: "+searchLocation, "Current: " + currentAddress,"Stored: " + userAddress};
                }else{
                    // if not searching address, 2 options
                    addrOptions = new String[]{"Current: " + currentAddress, "Stored: " + userAddress};
                }

                // display dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Pick an address");

                // finalize address option array
                String[] finalAddrOptions = addrOptions;

                // listen to dialog option
                builder.setItems(addrOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // remove unwanted string in addresses
                        String[] colon = {"Searched: ", "Current: " ,"Stored: "};
                            for (int j = 0; j < colon.length; j++){
                                if (finalAddrOptions[which].contains(colon[j])){
                                    finalAddrOptions[which] = finalAddrOptions[which].replace(colon[j], "");
                                }
                            }
                        finalAddress = finalAddrOptions[which];
                        extras.putString("address", finalAddress);// selected address
                        //extras.putString(TAG,TAG); // reserveNowFragment indicator

                        Intent intent = new Intent(getActivity(), ReserveFormActivity.class);
//                        Intent intent = new Intent(getActivity(), ReserveSelectCategoryActivity.class);
                        intent.putExtras(extras);
                        getContext().startActivity(intent);
                    }
                });

                builder.show();

            }
        });
        return view;
    }

    // read user-stored address from db
    private void retrieveAddress(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference refAddress = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("user");
        refAddress.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userHelperClass = snapshot.getValue(UserHelperClass.class);
                if (userHelperClass!= null){
                    userAddress = userHelperClass.address + ", "+ userHelperClass.postalCode + ", "+ userHelperClass.city + ", "+ userHelperClass.state;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // get current location from google map
    public void getCurrentLocation(){
        removeAllMarkers();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            firstMap();

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            firstMap();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }else{
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    // remove marker
    private void removeAllMarkers() {
        for (Marker marker: markers) {
            marker.remove();
        }
        markers.clear();

    }


    public void firstMap(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location) {
                if (location!=null){

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map = googleMap;

                            Geocoder geocoder;
                            List<Address> addresses = null;
                            geocoder = new Geocoder(getContext(), Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                /*String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();*/

                            currentAddress = address ;

                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");

                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            marker = map.addMarker(markerOptions);
                            marker.showInfoWindow();
                            markers.add(marker);

                        }
                    });
                }
            }
        });
    }


}