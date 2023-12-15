package com.example.suit_case_application.screens;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suit_case_application.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Color;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Geocoder geo;
    TextView txtMarkers;
    Button  btntype2;
    String name,description;
    String Address;
    Button  confirm;
    Double longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        txtMarkers = (TextView) findViewById(R.id.txtMarkerText);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
        confirm = findViewById(R.id.confirmationBtn);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapsActivity.this,ItemAdd.class);
                intent.putExtra("lat", latitude);
                intent.putExtra("lon", longitude);
                intent.putExtra("address", Address);
                intent.putExtra("Name", name);
                intent.putExtra("Description",description);
                startActivity(intent);
            }
        });



        Intent intent = getIntent();
        String name1 = intent.getStringExtra("Name");
        String description1 = intent.getStringExtra("Description");

        name = name1;
        description = description1;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            geo = new Geocoder(MapsActivity.this, Locale.getDefault());

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    try {
                        if (geo == null)
                            geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                        List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (address.size() > 0) {
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Name:" + address.get(0).getCountryName()
                                    + ". Address:" + address.get(0).getAddressLine(0)));
                            txtMarkers.setText("Name:" + address.get(0).getCountryName()
                                    + ". Address:" + address.get(0).getAddressLine(0));
                            Address = address.get(0).getCountryName() + address.get(0).getAddressLine(0);
                            latitude = address.get(0).getLatitude();
                            longitude= address.get(0).getLongitude();
                        }
                    } catch (IOException ex) {
                        if (ex != null)
                            Toast.makeText(MapsActivity.this, "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });




        }

    }


}
