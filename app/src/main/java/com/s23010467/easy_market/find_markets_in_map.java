package com.s23010467.easy_market;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class find_markets_in_map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference marketRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_markets_in_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        marketRef = FirebaseDatabase.getInstance()
                .getReference("market_places");

        // Initializee google  map.....
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Fetch market data from Firebase
        marketRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMap.clear(); // Clear old markers

                for (DataSnapshot marketSnapshot : snapshot.getChildren()) {
                    String name = marketSnapshot.child("market_name").getValue(String.class);
                    Double lat = marketSnapshot.child("latitude").getValue(Double.class);
                    Double lng = marketSnapshot.child("longitude").getValue(Double.class);

                    if (lat != null && lng != null) {
                        LatLng marketLocation = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions()
                                .position(marketLocation)
                                .title(name != null ? name : "Market"));

                        // Optionally move camera to first market
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marketLocation, 10));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(find_markets_in_map.this, "Failed to load markets.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}